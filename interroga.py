from langchain_community.vectorstores import Chroma

# Carica il database Chroma esistente
vector_store = Chroma(persist_directory="./chroma_db")

# Query di esempio
query = "Come formattare una data in Java?"

# Cerca i chunk più simili
docs = vector_store.similarity_search(query, k=3)  # k = numero di risultati

# Stampa i risultati
for i, doc in enumerate(docs):
    print(f"Risultato {i+1}:")
    print(doc.page_content)  # Contenuto del chunk
    print("-" * 40)