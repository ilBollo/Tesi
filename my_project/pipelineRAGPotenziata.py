import json
from typing import List, Dict
from pathlib import Path
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
            "score_threshold": 0.8, # alzo la soglia per includere più file anche se fuori contesto
            "search_type" :"similarity",  # Più efficace per il codice usare mmr per diversità
            "lambda_mult":0.5       # Bilancia diversità/rilevanza
        }
    )

varStileLLM = "Rispondi come un programmatore esperto ma in modo sintetico."

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
    "temperature": 0.3,
    "top_p": 0.85  # Bilancia creatività/controllo nei token generati
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
llm, prompt = load_model("llama3.2")

# Catena RAG
document_chain = create_stuff_documents_chain(llm, prompt)
rag_chain = create_retrieval_chain(
    retriever,
    document_chain
)

def load_questions(file_path: str) -> List[Dict]:
    """Carica le domande dal file JSON esterno"""
    try:
        actual_path = Path(__file__).parent / file_path
        with actual_path.open('r', encoding='utf-8') as f:
            data = json.load(f)
            
        # Validazione della struttura
        required_keys = {'id', 'question', 'answer', 'punteggio'}
        for item in data:
            if not required_keys.issubset(item.keys()):
                raise ValueError("Struttura JSON non valida")
                
        return data
        
    except FileNotFoundError:
        raise Exception(f"File {file_path} non trovato")
    except json.JSONDecodeError:
        raise Exception("Errore nel parsing del JSON")

def process_questions(questions: List[Dict]) -> List[Dict]:
    results = []
    for q in questions:
        try:
            result = rag_chain.invoke({"input": q["question"]})
            
            entry = {
                "id": q["id"],
                "question": q["question"],
                "answerOK": q["answer"],  # Mantiene il contesto originale
                "answerRAG": result["answer"],
                "punteggio": q["punteggio"],
                "sources": [
                    {
                        "content": doc.page_content[:50]
                    } 
                    for doc in result["context"]
                ]
            }
            results.append(entry)
            
            print(f"Processata {q['id']}")
            
        except Exception as e:
            print(f"Errore su {q['id']}: {str(e)}")
            results.append({
                "id": q["id"],
                "punteggio": q["punteggio"],
                "error": str(e),
                "question": q["question"]
            })
    
    return results

if __name__ == "__main__":
    # Caricamento domande da file esterno
    questions = load_questions("domanderisposte.json")
    
    # Processamento e salvataggio risultati
    all_results = process_questions(questions)
    
    output_file = Path("rag_resultsllamascore1.json")
    output_file.write_text(
        json.dumps(all_results, ensure_ascii=False, indent=2), 
        encoding='utf-8'
    )
    
    print(f"\n✅ Risultati salvati in {output_file} ({len(all_results)} entries)")