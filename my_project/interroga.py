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

# 3. Query di esempio
query = "Cosa ritorna il metodo GiorniMagici.getMessaggioMagico(LocalDate.of(2025, 1, 10))?"
#"Spiegami cosa fa la funzione String sopresa (LocalDate date)"

# 4. Cerca i chunk più simili
docs = vector_store.similarity_search_with_score(
    query,
    k=5,
    score_threshold=0.70,  # bassa similarità
    #search_type="similarity",  # Più efficace per il codice
    search_type= "mmr",      # Usare MMR per diversità

)

# 5. Stampa i risultati con relativo score
for i, (doc, score) in enumerate(docs):
    print(f"Risultato {i+1} (Score: {score:.4f}):")
    print(doc.page_content)
    print("-" * 40)