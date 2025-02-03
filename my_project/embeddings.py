import json
from sentence_transformers import SentenceTransformer
from langchain_community.vectorstores import FAISS

import re

def extract_method_name(text):
    # Cerca firme di metodi Java standard
    method_pattern = r'(?:public|private|protected|static|final|synchronized|abstract|native)\s+[\w<>\[\]]+\s+(\w+)\s*\([^)]*\)'
    
    # Cerca costruttori
    constructor_pattern = r'(?:public|private|protected)\s+(\w+)\s*\([^)]*\)'
    
    matches = re.findall(method_pattern, text)
    if matches:
        return matches[0]  # Restituisce il primo metodo trovato
    
    constr_matches = re.findall(constructor_pattern, text)
    if constr_matches:
        return constr_matches[0] + " (costruttore)"
    
    # Cerca chiamate a metodi nel chunk
    method_calls = re.findall(r'\.(\w+)\s*\(', text)
    if method_calls:
        return f"Chiamata a: {method_calls[-1]}"
    
    return "unknown_method"  # Default se non trova nulla


# 1. Carica i chunk dal file JSON
with open("chunks.json", "r", encoding="utf-8") as f:
    chunks_data = json.load(f)

chunks = [item["text"] for item in chunks_data]

# 2. Carica il modello BGE-M3 e genera gli embedding
embedder = SentenceTransformer('BAAI/bge-m3')
embeddings = embedder.encode(
    [f"CLASSE:{c['class']} METODO:{extract_method_name(c['text'])} CONTENUTO:{c['text']}" 
     for c in chunks_data],
    show_progress_bar=True
)

# 3. Crea un database FAISS
vector_store = FAISS.from_embeddings(
    text_embeddings=list(zip(chunks, embeddings)),  # Abbina testi e embedding
    embedding=embedder,  # Modello per future operazioni
)

# 4. Salva il database
vector_store.save_local("./faiss_db")
print("Database FAISS creato e salvato in ./faiss_db.")