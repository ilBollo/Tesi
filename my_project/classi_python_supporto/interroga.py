from langchain_community.vectorstores import FAISS
from langchain_huggingface import HuggingFaceEmbeddings

# 1. Carica il modello di embedding nel formato corretto
embedder = HuggingFaceEmbeddings(
    model_name="BAAI/bge-m3",
    model_kwargs={'device': 'cpu'},
    encode_kwargs={'normalize_embeddings': True}
)

# 2. Carica il database FAISS esistente
vector_store = FAISS.load_local(
    folder_path="./faiss_db",
    embeddings=embedder,
    allow_dangerous_deserialization=True
)

# 3. Query di esempio
query = "Cosa ritorna il metodo segnaleWow(LocalDate.of(2025, 1, 10)) che utilizza la funzione getMessaggioMagico() della libreria DateUtilCustom?"
#"Cosa ritorna il metodo segnaleWow(LocalDate.of(2025, 1, 10))?"
#"Scrivimi una funzione per calcolare le statistiche di una squadra di basket")


# 4. Cerca i chunk più simili
docs = vector_store.similarity_search_with_score(
    query,
    k=5,
    score_threshold=0.90,  # medio-bassa similarità
    search_type="similarity",  # Più efficace per il codice
    lambda_mult=0.5       # Bilancia diversità/rilevanza
)

# 5. Stampa i risultati con relativo score
for i, (doc, score) in enumerate(docs):
    print(f"Risultato {i+1} (Score: {score:.4f}):")
    print(doc.page_content)
    print("-" * 40)