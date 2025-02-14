import json
from langchain_community.vectorstores import FAISS
from langchain_huggingface import HuggingFaceEmbeddings

# 1. Carica il modello di embedding nel formato corretto
embedder = HuggingFaceEmbeddings(
    model_name="BAAI/bge-m3",
    model_kwargs={'device': 'cpu'},  # Usa 'cuda' per GPU
    encode_kwargs={'normalize_embeddings': True}
)

# 2. Carica il database FAISS esistente
vector_store = FAISS.load_local(
    folder_path="./faiss_db",
    embeddings=embedder,
    allow_dangerous_deserialization=True
)

# 3. Domande

questions = [
    {
        "id": "Q1",
        "question": "Cosa ritorna il metodo segnaleWow(LocalDate.of(2025, 1, 10)) che utilizza la funzione getMessaggioMagico()?"
    },
    {
        "id": "Q2",
        "question": "Come calcolare la media battuta con 25 valide su 80 turni?"
    },
    {
        "id": "Q3",
        "question": "Quale ERA risulta da 5 punti subiti in 7 inning?"
    },
    {
        "id": "Q4",
        "question": "Come validare una password 'Secret123!'?"
    },
    {
        "id": "Q5",
        "question": "Cosa restituisce invertiStringaMantenendoMaiuscole('AbCde')?"
    },
    {
        "id": "Q6",
        "question": "Come convertire 10km in miglia?"
    },
    {
        "id": "Q7",
        "question": "Quale BMI risulta da 70kg e 1.75m?"
    },
    {
        "id": "Q8",
        "question": "Cosa significa un momentum [12,15] vs [8,10]?"
    },
    {
        "id": "Q9",
        "question": "Come cifrare 'HELLO' con Caesar shift 3?"
    },
    {
        "id": "Q10",
        "question": "Quale temperatura a Roma (41.9°) a luglio?"
    },
    {
        "id": "Q11",
        "question": "Cosa restituisce isDataMagica(6, 5, 2030)?"
    },
    {
        "id": "Q12",
        "question": "Come calcolare rata mutuo 100k€ al 4% in 20 anni?"
    },
    {
        "id": "Q13",
        "question": "Cosa restituisce getMessaggioMagico(LocalDate.of(2024,12,25))?"
    },
    {
        "id": "Q14",
        "question": "Come calcolare VAN al 5% per flussi [100,200,300]?"
    },
    {
        "id": "Q15",
        "question": "Quale complessità ciclomatica per codice con 3 if e 2 while?"
    },
    {
        "id": "Q16",
        "question": "Cosa restituisce modelloPredaPredatore(100,50,0.1,0.05)?"
    },
    {
        "id": "Q17",
        "question": "Come generare password sicura di 12 caratteri?"
    },
    {
        "id": "Q18",
        "question": "Cosa restituisce convertiGrigio(0xFFAABB)?"
    },
    {
        "id": "Q19",
        "question": "Come calcolare log base 3 di 27?"
    },
    {
        "id": "Q20",
        "question": "Quale categoria BMI per 28.7?"
    },
    {
        "id": "Q21",
        "question": "Cosa restituisce isPastDate(1/1/2020)?"
    },
    {
        "id": "Q22",
        "question": "Come aggiungere 15 giorni al 1/1/2024?"
    },
    {
        "id": "Q23",
        "question": "Quale ROI per guadagno 1500€ su costo 1000€?"
    },
    {
        "id": "Q24",
        "question": "Cosa restituisce calcolaEfficienzaGiocatore(20,10,8,2,5)?"
    },
    {
        "id": "Q25",
        "question": "Come simulare crescita popolazione 1000 al 2% in 5 anni?"
    },
    {
        "id": "Q26",
        "question": "Cosa restituisce analisiStatisticheSquadra(85, 70, 12, 28)?"
    },
    {
        "id": "Q27",
        "question": "Come decifrare 'KHOOR' con Caesar shift 3?"
    },
    {
        "id": "Q28",
        "question": "Quale percussocritico per attività [5,8,3]?"
    },
    {
        "id": "Q29",
        "question": "Cosa restituisce giorniAlmiocompleannoSpecial(1/1/2000, 'Mario')?"
    },
    {
        "id": "Q30",
        "question": "Come calcolare radice 5a di 3125?"
    }
]

# 4. Processa ogni domanda e salva i risultati
results = []

for q in questions:
    # Esegui la ricerca di similarità
    docs_and_scores = vector_store.similarity_search_with_score(
        q["question"], 
        k=5
    )
    
    # Filtra i risultati per score threshold (0.80) e prepara i contesti
    contexts = [
        {
            "chunk": doc.page_content,
            "score": float(score)  # Conversione per serializzazione JSON
        } 
        for doc, score in docs_and_scores 
        if score >= 0.80
    ]
    
    # Aggiungi al risultato finale
    results.append({
        "id": q["id"],
        "question": q["question"],
        "contexts": contexts
    })

# 5. Salva in un file JSON
with open("risultati_ricerche.json", "w", encoding="utf-8") as f:
    json.dump(results, f, ensure_ascii=False, indent=2)

print("Ricerca completata. Risultati salvati in risultati_ricerche.json")