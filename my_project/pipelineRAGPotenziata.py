import json
from typing import List, Dict

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
            "score_threshold": 0.80, # medio-bassa similarità inizialmente era 0.90
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
llm, prompt = load_model("codeqwen")

# Catena RAG
document_chain = create_stuff_documents_chain(llm, prompt)
rag_chain = create_retrieval_chain(
    retriever,
    document_chain
)

def process_questions(questions: List[Dict]) -> List[Dict]:
    results = []
    for q in questions:
        try:
            result = rag_chain.invoke({"input": q["question"]})
            
            entry = {
                "id": q["id"],
                "question": q["question"],
                "answer": result["answer"],
                "sources": [
                    {
                        "content": doc.page_content[:500]  # Limita la lunghezza
                    } 
                    for doc in result["context"]
                ]
            }
            results.append(entry)
            
            print(f"Processata {q['id']} - {q['context']}")
            
        except Exception as e:
            print(f"Errore su {q['id']}: {str(e)}")
            results.append({
                "id": q["id"],
                "error": str(e),
                "question": q["question"]
            })
    
    return results

# Definizione delle domande (puoi caricarle da CSV con pandas.read_csv se preferisci)
questions = [
    {
        "id": "Q1",
        "question": "Cosa ritorna il metodo segnaleWow(LocalDate.of(2025, 1, 10)) che utilizza la funzione getMessaggioMagico()?",
        "context": "GiorniMagici.java"
    },
    {
        "id": "Q2",
        "question": "Come calcolare la media battuta con 25 valide su 80 turni?",
        "context": "CalcolatoreStatisticheBaseball.java"
    },
    {
        "id": "Q3",
        "question": "Quale ERA risulta da 5 punti subiti in 7 inning?",
        "context": "CalcolatoreStatisticheBaseball.java"
    },
    {
        "id": "Q4",
        "question": "Come validare una password 'Secret123!'?",
        "context": "GestionePassword.java"
    },
    {
        "id": "Q5",
        "question": "Cosa restituisce invertiStringaMantenendoMaiuscole('AbCde')?",
        "context": "AnalizzatoreTesto.java"
    },
    {
        "id": "Q6",
        "question": "Come convertire 10km in miglia?",
        "context": "ConvertitoreUnita.java"
    },
    {
        "id": "Q7",
        "question": "Quale BMI risulta da 70kg e 1.75m?",
        "context": "CalcolatoreBMI.java"
    },
    {
        "id": "Q8",
        "question": "Cosa significa un momentum [12,15] vs [8,10]?",
        "context": "BasketballStats.java"
    },
    {
        "id": "Q9",
        "question": "Come cifrare 'HELLO' con Caesar shift 3?",
        "context": "StrumentiCrittografia.java"
    },
    {
        "id": "Q10",
        "question": "Quale temperatura a Roma (41.9°) a luglio?",
        "context": "SimulatoreMeteo.java"
    },
    {
        "id": "Q11",
        "question": "Cosa restituisce isDataMagica(6, 5, 2030)?",
        "context": "GiorniMagici.java"
    },
    {
        "id": "Q12",
        "question": "Come calcolare rata mutuo 100k€ al 4% in 20 anni?",
        "context": "GestioneFinanzePersonali.java"
    },
    {
        "id": "Q13",
        "question": "Cosa restituisce getMessaggioMagico(LocalDate.of(2024,12,25))?",
        "context": "DateUtilCustom.java"
    },
    {
        "id": "Q14",
        "question": "Come calcolare VAN al 5% per flussi [100,200,300]?",
        "context": "AnalizzatoreInvestimenti.java"
    },
    {
        "id": "Q15",
        "question": "Quale complessità ciclomatica per codice con 3 if e 2 while?",
        "context": "AnalizzatoreCodice.java"
    },
    {
        "id": "Q16",
        "question": "Cosa restituisce modelloPredaPredatore(100,50,0.1,0.05)?",
        "context": "SimulatoreEcologico.java"
    },
    {
        "id": "Q17",
        "question": "Come generare password sicura di 12 caratteri?",
        "context": "GestionePassword.java"
    },
    {
        "id": "Q18",
        "question": "Cosa restituisce convertiGrigio(0xFFAABB)?",
        "context": "StrumentiGrafica.java"
    },
    {
        "id": "Q19",
        "question": "Come calcolare log base 3 di 27?",
        "context": "CalcolatriceScientifica.java"
    },
    {
        "id": "Q20",
        "question": "Quale categoria BMI per 28.7?",
        "context": "CalcolatoreBMI.java"
    },
    {
        "id": "Q21",
        "question": "Cosa restituisce isPastDate(1/1/2020)?",
        "context": "DateUtilCustom.java"
    },
    {
        "id": "Q22",
        "question": "Come aggiungere 15 giorni al 1/1/2024?",
        "context": "DateUtilCustom.java"
    },
    {
        "id": "Q23",
        "question": "Quale ROI per guadagno 1500€ su costo 1000€?",
        "context": "AnalizzatoreInvestimenti.java"
    },
    {
        "id": "Q24",
        "question": "Cosa restituisce calcolaEfficienzaGiocatore(20,10,8,2,5)?",
        "context": "BasketballStats.java"
    },
    {
        "id": "Q25",
        "question": "Come simulare crescita popolazione 1000 al 2% in 5 anni?",
        "context": "SimulatoreEcologico.java"
    },
    {
        "id": "Q26",
        "question": "Cosa restituisce analisiStatisticheSquadra(85, 70, 12, 28)?",
        "context": "BasketballStats.java"
    },
    {
        "id": "Q27",
        "question": "Come decifrare 'KHOOR' con Caesar shift 3?",
        "context": "StrumentiCrittografia.java"
    },
    {
        "id": "Q28",
        "question": "Quale percussocritico per attività [5,8,3]?",
        "context": "GestoreProgetti.java"
    },
    {
        "id": "Q29",
        "question": "Cosa restituisce giorniAlmiocompleannoSpecial(1/1/2000, 'Mario')?",
        "context": "DateUtilCustom.java"
    },
    {
        "id": "Q30",
        "question": "Come calcolare radice 5a di 3125?",
        "context": "CalcolatriceScientifica.java"
    }
]


if __name__ == "__main__":
    # Processa tutte le domande
    all_results = process_questions(questions)
    
    # Salva i risultati in JSON
    with open("rag_results.json", "w", encoding="utf-8") as f:
        json.dump(all_results, f, ensure_ascii=False, indent=2)
    
    print(f"\nRisultati salvati in rag_results.json ({len(all_results)} domande processate)")