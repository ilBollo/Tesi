from langchain_community.vectorstores import FAISS
from langchain_huggingface import HuggingFaceEmbeddings
from langchain_community.llms import Ollama
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
Sei un esperto di programmazione. Rispondi in italiano basandoti esclusivamente sul contesto fornito.
Contesto: {context}<|eot_id|>
<|start_header_id|>user<|end_header_id|>
Domanda: {question}<|eot_id|>
<|start_header_id|>assistant<|end_header_id|>"""

CODEQWEN_TEMPLATE = """<|im_start|>system
Analizza i seguenti frammenti di codice correlati:
{context}

Segui queste regole:
1. Combina le implementazioni da classi diverse
2. Considera sia le definizioni che le chiamate
3. Mostra le relazioni tra i metodi<|im_end|>
<|im_start|>user
{question}<|im_end|>
<|im_start|>assistant
"""

# 4. Funzione per selezionare il modello
def load_model(model_name):
    models = {
        "llama3.2": {
            "template": LLAMA_TEMPLATE,
            "params": {
                "temperature": 0.7,
                "system": "Rispondi in italiano come esperto di programmazione"
            }
        },
        "codeqwen": {
            "template": CODEQWEN_TEMPLATE,
            "params": {
                                # Temperature 0.4: più deterministica, risposte più conservative
                # Range temperature:
                # 0.0: completamente deterministica
                # 0.1-0.4: conservativa, buona per codice
                # 0.5-0.7: bilanciata
                # 0.8-1.0: molto creativa
                "temperature": 0.6,  # Valore intermedio
                "top_p": 0.9,        # Aggiungere sampling nucleare
                #"system": "Fornisci codice basato sul contesto"
                "system": "Fornisci la miglior risposta possibile basandoti sul contesto disponibile, anche se parziale"
            }
        }
    }
    
    if model_name not in models:
        raise ValueError(f"Modello non supportato: {model_name}")
    
    return Ollama(
        model=model_name,
        **models[model_name]["params"]
    ), PromptTemplate(
        template=models[model_name]["template"],
        input_variables=["context", "question"] 
    )

# 5. Inizializza il modello
llm, prompt = load_model("codeqwen")

# 6. Catena RAG corretta
rag_chain = RetrievalQA.from_chain_type(
    llm=llm,
    chain_type="stuff",
    retriever=vector_store.as_retriever(
        search_kwargs={
            "k": 8,                   # Più documenti per contesto
            "score_threshold": 0.90,   # bassa similarità
            "search_type": "similarity",  # Più efficace per il codice
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
            print("\033[1;31mNessun documento rilevante trovato!\033[0m")
            return        
        print("\n\033[1;36mDOCUMENTI RECUPERATI:\033[0m")
        for doc in retrieved_docs:
            print(f"- {doc.page_content[:200]}...\n")
        result = rag_chain.invoke({"query": question})
        
        print("\n\033[1;34mDOMANDA:\033[0m", question)
        print("\n\033[1;32mRISPOSTA:\033[0m")
        print(result["result"])
        
        print("\n\033[1;33mFONTI:\033[0m")
        for i, doc in enumerate(result["source_documents"], 1):
            print(f"{i}. {doc.page_content[:150]}...")
            if 'source' in doc.metadata:
                print(f"   Fonte: {doc.metadata['source']}")
            print("-" * 80)
    except Exception as e:
        print(f"\033[1;31mERRORE:\033[0m {str(e)}")

# 8. Esempio d'uso
if __name__ == "__main__":
   # ask_ollama("Cosa ritorna sorpresa (LocalDate.of(2025, 1, 10))")
    ask_ollama("Cosa ritorna GiorniMagici.getMessaggioMagico(LocalDate.of(2025, 1, 10))?")