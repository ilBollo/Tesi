from langchain_community.vectorstores import FAISS
from langchain_huggingface import HuggingFaceEmbeddings
from langchain_ollama import OllamaLLM
from langchain.chains import create_retrieval_chain
from langchain.chains.combine_documents import create_stuff_documents_chain
from langchain.prompts import PromptTemplate

# Configurazione embedding
embedder = HuggingFaceEmbeddings(
    model_name="BAAI/bge-m3",
    model_kwargs={'device': 'cpu'},
    encode_kwargs={'normalize_embeddings': True}
)

# Caricamento database FAISS
vector_store = FAISS.load_local(
    folder_path="./faiss_db",
    embeddings=embedder,
    allow_dangerous_deserialization=True
)
# Aggiunta del database FAISS al retriever
retriever=vector_store.as_retriever(
        search_kwargs={
            "k": 5,                   # Più documenti per contesto
            "score_threshold": 0.82, # medio-bassa similarità inizialmente era 0.90
            "search_type" :"similarity",  # Più efficace per il codice usare mmr per diversità
            "lambda_mult":0.5       # Bilancia diversità/rilevanza
        }
    )

varStileLLM = "Sei un assistente che combina codice Java e contesto strutturale per risposte precise. Rispondi solo se hai certezza sicura della risposta."

# Configurazione Template del prompt specifici per i modelli
LLAMA_TEMPLATE = """<|begin_of_text|>
<|start_header_id|>system""" + varStileLLM + """<|end_header_id|>
Contesto: {context}<|eot_id|>
<|start_header_id|>user<|end_header_id|>
Domanda: {input}<|eot_id|>
<|start_header_id|>assistant<|end_header_id|>"""

CODEQWEN_TEMPLATE = """<|im_start|>system """ + varStileLLM + """
{context}<|im_end|>
<|im_start|>user
{input}<|im_end|>
<|im_start|>assistant
"""

COMMON_PARAMS = {
    "temperature": 0.1, 
    "top_p": 0.60  # Bilancia creatività/controllo nei token generati
}

# Caricamento modello
def load_model(model_name):
    models = {
        "llama3.2": {
            "template": LLAMA_TEMPLATE,
            "params": COMMON_PARAMS
        },
        "codeqwen": {
            "template": CODEQWEN_TEMPLATE,
            "params": COMMON_PARAMS
        },
        "deepseek-r1": {
            "template": CODEQWEN_TEMPLATE,
            "params": COMMON_PARAMS
        }
    }
    if model_name not in models:
        raise ValueError(f"Modello non supportato: {model_name}")
    return OllamaLLM(
        model=model_name,
        **models[model_name]["params"]
    ), PromptTemplate(
        template=models[model_name]["template"],
        input_variables=["input", "context"]
    )

# Inizializza il modello
llm, prompt = load_model("codeqwen")

# Catena RAG
document_chain = create_stuff_documents_chain(llm, prompt)
rag_chain = create_retrieval_chain(
    retriever,
    document_chain
)

# Funzione query
def ask_ollama(question):
    try:
        result = rag_chain.invoke({"input": question})
        print("DOMANDA:", question)
        print("RISPOSTA:")
        print(result["answer"])
        print("FONTI:")
        for i, doc in enumerate(result["context"], 1):
            print(f"{i}. {doc.page_content[:150]}...")
            if 'source' in doc.metadata:
                print(f" Fonte: {doc.metadata['source']}")
            print("-" * 80)
    except Exception as e:
        print(f"ERRORE: {str(e)}")

# Esempio d'uso
if __name__ == "__main__":
    ask_ollama("Sto costruendo il seguente metodo: int calcolaGiorniLavorativi(Date dataA, Date dataB){ int numeroGiorni... return numeroGiorni} che trova la differenza in giorni lavorativi(escludendo sabato e domenica). Come posso costruirlo utilizzando la classe DataUtilCustom?")
        #"Sostituisci nella clase DateUtilCustom.java il costrutto \texttt{if-else} in \texttt{getMessaggioMagico()} con uno \texttt{switch} expression'")
        #"Aggiungi alla classe AdvancedBasketballStats un metodo calcolaEfficienzaSquadra() che valuti l'efficienza della squadra in base alle statistiche della squadra e alle partite vinte")
               #Cosa ritorna il metodo segnaleWow(LocalDate.of(2025, 2, 14)) che utilizza la funzione getMessaggioMagico() della libreria DateUtilCustom?")
    #"Cosa ritorna il metodo segnaleWow(LocalDate.of(2025, 2, 14))?")
    #ask_ollama("Che giorno della settimana è il 10 gennaio 2025?")
    #ask_ollama("Scrivimi una funzione per calcolare le statistiche di una squadra di basket")