from langchain_community.vectorstores import Chroma

# Crea un database Chroma
vector_store = Chroma.from_texts(
    texts=chunks,
    embedding=embedder,
    persist_directory="./chroma_db"  # Cartella per salvare il database
)

print("Database Chroma creato e salvato in ./chroma_db.")