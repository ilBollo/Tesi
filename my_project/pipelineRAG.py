from langchain_community.vectorstores import FAISS
from langchain_community.embeddings import HuggingFaceEmbeddings
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

# 3. Configurazione modelli Ollama
LLAMA_TEMPLATE = """<|begin_of_text|>
<|start_header_id|>system<|end_header_id|>
Sei un esperto di programmazione. Rispondi in italiano basandoti esclusivamente sul contesto fornito.
Contesto: {context}<|eot_id|>
<|start_header_id|>user<|end_header_id|>
Domanda: {question}<|eot_id|>
<|start_header_id|>assistant<|end_header_id|>"""

CODEQWEN_TEMPLATE = """<|im_start|>system
Sei uno sviluppatore esperto. Fornisci risposte concise con codice basato sul contesto.<|im_end|>
<|im_start|>user
Contesto: {context}
Domanda: {question}<|im_end|>
<|im_start|>assistant
"""

# 4. Funzione per selezionare il modello
def load_model(model_name):
    models = {
        "llama3": {
            "template": LLAMA_TEMPLATE,
            "params": {
                "temperature": 0.7,
                "system": "Rispondi in italiano come esperto di programmazione"
            }
        },
        "codeqwen": {
            "template": CODEQWEN_TEMPLATE,
            "params": {
                "temperature": 0.3,
                "system": "Fornisci solo codice basato sul contesto"
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
        search_kwargs={"k": 3, "score_threshold": 0.4}
    ),
    chain_type_kwargs={"prompt": prompt},
    return_source_documents=True,
    verbose=False
)

# 7. Funzione query (invariata)
def ask_ollama(question):
    try:
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
    ask_ollama("Mostrami un esempio di formattazione della data in Java usando SimpleDateFormat")