import json
from sentence_transformers import SentenceTransformer
from langchain_community.vectorstores import Chroma

# 1. Carica i chunk dal file JSON
with open("chunks.json", "r", encoding="utf-8") as f:
    chunks_data = json.load(f)

# Estrai i testi dei chunk
chunks = [item["text"] for item in chunks_data]

# 2. Carica il modello BGE-M3 e genera gli embedding
embedder = SentenceTransformer('BAAI/bge-m3')
embeddings = embedder.encode(chunks, show_progress_bar=True)

# 3. Crea un database Chroma
vector_store = Chroma.from_texts(
    texts=chunks,  # Testi dei chunk
    embedding=embedder,  # Modello di embedding
    persist_directory="./chroma_db"  # Cartella per salvare il database
)

print("Database Chroma creato e salvato in ./chroma_db.")