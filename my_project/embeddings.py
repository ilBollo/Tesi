import json
from sentence_transformers import SentenceTransformer
from langchain_community.vectorstores import FAISS

# 1. Carica i chunk dal file JSON
with open("chunks.json", "r", encoding="utf-8") as f:
    chunks_data = json.load(f)

chunks = [item["text"] for item in chunks_data]

# 2. Carica il modello BGE-M3 e genera gli embedding
embedder = SentenceTransformer('BAAI/bge-m3')
embeddings = embedder.encode(chunks, show_progress_bar=True)

# 3. Crea un database FAISS
vector_store = FAISS.from_embeddings(
    text_embeddings=list(zip(chunks, embeddings)),  # Abbina testi e embedding
    embedding=embedder,  # Modello per future operazioni
)

# 4. Salva il database
vector_store.save_local("./faiss_db")
print("Database FAISS creato e salvato in ./faiss_db.")