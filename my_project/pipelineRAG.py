from langchain_community.vectorstores import FAISS
from langchain_huggingface import HuggingFaceEmbeddings
from langchain_ollama import OllamaLLM
from langchain.chains import RetrievalQA
from langchain.prompts import PromptTemplate

# 1. Configurazione embedding
embedder = HuggingFaceEmbeddings(
    model_name="BAAI/bge-m3",
    model_kwargs={'device': 'cpu'},
    encode_kwargs={'normalize_embeddings': True}
)

# 2. Carica il database FAISS
vector_store = FAISS.load_local(
    folder_path="./faiss_db",
    embeddings=embedder,
    allow_dangerous_deserialization=True
)

# 3. Configurazione Template del prompt
LLAMA_TEMPLATE = """<|begin_of_text|>
<|start_header_id|>system<|end_header_id|>
Sei un esperto di programmazione. Rispondi in italiano.
Contesto: {context}<|eot_id|>
<|start_header_id|>user<|end_header_id|>
Domanda: {question}<|eot_id|>
<|start_header_id|>assistant<|end_header_id|>"""

CODEQWEN_TEMPLATE = """<|im_start|>system
{{ .System }}<|im_end|>
{{ if .Functions }}<|im_start|>functions
{{ .Functions }}<|im_end|>{{ end }}
<|im_start|>user
{{ .Question }}<|im_end|>
<|im_start|>assistant
"""

# 4. Funzione per selezionare il modello
def load_model(model_name):
    models = {
        "llama3.2": {
            "template": LLAMA_TEMPLATE,
            "params": {
                "temperature": 0.3,
                "top_p": 0.85, #Bilancia creatività/controllo nei token generati
                "system": "Rispondi in italiano come esperto di programmazione ma solo se sei sicuro."
            }
        },
        "codeqwen": {
            "template": CODEQWEN_TEMPLATE,
            "params": {
                "temperature": 0.3,
                "top_p": 0.85, #Bilancia creatività/controllo nei token generati
                "system": "Rispondi in italiano come esperto di programmazione ma solo se sei sicuro."
            }
        }
    }
    
    if model_name not in models:
        raise ValueError(f"Modello non supportato: {model_name}")
    
    return OllamaLLM(
        model=model_name,
        **models[model_name]["params"]
    ), PromptTemplate(
        template=models[model_name]["template"],
        input_variables=["context", "question"] 
    )

# 5. Inizializza il modello
llm, prompt = load_model("llama3.2")

# 6. Catena RAG corretta
rag_chain = RetrievalQA.from_chain_type(
    llm=llm,
    chain_type="stuff",
    retriever=vector_store.as_retriever(
        search_kwargs={
            "k": 5,                   # Più documenti per contesto
            "score_threshold": 0.80, # medio-bassa similarità inizialmente era 0.90
            "search_type" :"similarity",  # Più efficace per il codice usare mmr per diversità
            "lambda_mult":0.5       # Bilancia diversità/rilevanza
        }
    ),
    chain_type_kwargs={"prompt": prompt},
    return_source_documents=True,
    verbose=False
)

# 7. Funzione query
def ask_ollama(question):
    try:
        retrieved_docs = vector_store.similarity_search(question, k=3)
        if not retrieved_docs:
            print("Nessun documento rilevante trovato!")
            return        
        print("DOCUMENTI RECUPERATI:")
        for doc in retrieved_docs:
            print(f"- {doc.page_content[:200]}...\n")
        result = rag_chain.invoke({"query": question})
        
        print("DOMANDA:", question)
        print("RISPOSTA:")
        print(result["result"])
        
        print("FONTI:")
        for i, doc in enumerate(result["source_documents"], 1):
            print(f"{i}. {doc.page_content[:150]}...")
            if 'source' in doc.metadata:
                print(f" Fonte: {doc.metadata['source']}")
            print("-" * 80)
    except Exception as e:
        print(f"ERRORE: {str(e)}")

# 8. Esempio d'uso
if __name__ == "__main__":
    #ask_ollama("Cosa ritorna il metodo segnaleWow(LocalDate.of(2025, 1, 10)) che utilizza la funzione getMessaggioMagico() della libreria DateUtilCustom?")
    ask_ollama("Cosa ritorna il metodo segnaleWow(LocalDate.of(2025, 1, 10))?")