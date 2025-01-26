import faiss
import numpy as np
from sentence_transformers import SentenceTransformer

# Carica embedding e snippet di codice
embedding_matrix = np.load("embeddings.npy")  # Embedding dei tuoi snippet di codice
code_snippets = [
    "def add(a, b): return a + b",
    "def subtract(a, b): return a - b",
    "def multiply(a, b): return a * b",
    # Associa gli snippet alla matrice
]

# Configura FAISS
dimension = embedding_matrix.shape[1]
index = faiss.IndexFlatL2(dimension)
index.add(embedding_matrix)

# Funzione di ricerca
def query_rag_system(query_embedding: np.ndarray, k=3):
    distances, indices = index.search(query_embedding, k)
    results = [code_snippets[i] for i in indices[0]]
    return results
