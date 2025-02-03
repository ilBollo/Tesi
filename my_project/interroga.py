from langchain_community.vectorstores import FAISS
from langchain_community.embeddings import HuggingFaceEmbeddings

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
query = "Spiegami cosa fa la funzione String sopresa (LocalDate date)"

# 4. Cerca i chunk più simili
docs = vector_store.similarity_search(query, k=3)

# 5. Stampa i risultati
for i, doc in enumerate(docs):
    print(f"Risultato {i+1}:")
    print(doc.page_content)
    print("-" * 40)