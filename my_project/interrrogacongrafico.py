from langchain_community.vectorstores import FAISS
from langchain_huggingface import HuggingFaceEmbeddings

import numpy as np
import matplotlib.pyplot as plt
from sklearn.manifold import TSNE

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
query = "Cosa restituisce modelloPredaPredatore(100,50,0.1,0.05)?"

# 4. Cerca i chunk più simili
docs = vector_store.similarity_search_with_score(
    query,
    k=5,
    score_threshold=0.80,
    search_type="similarity",
    lambda_mult=0.5
)

# 5. Stampa i risultati con relativo score
for i, (doc, score) in enumerate(docs):
    print(f"Risultato {i+1} (Score: {score:.4f}):")
    print(doc.page_content)
    print("-" * 40)

try:
    # Calcola gli embedding per la query e i documenti
    query_embedding = embedder.embed([query])[0]
    doc_embeddings = np.array([embedder.embed([doc.page_content])[0] for doc, _ in docs])

    # Aggiungi l'embedding della query alla lista degli embedding dei documenti
    all_embeddings = np.vstack([query_embedding, doc_embeddings])

    # Riduci la dimensionalità a 2D usando t-SNE
    tsne = TSNE(n_components=2, perplexity=3, random_state=42)
    embeddings_2d = tsne.fit_transform(all_embeddings)

    # Separa i risultati per la query e i documenti
    query_2d = embeddings_2d[0]
    docs_2d = embeddings_2d[1:]

    # Plotta i risultati
    plt.figure(figsize=(10, 6))
    plt.scatter(docs_2d[:, 0], docs_2d[:, 1], label='Documenti', c='blue')
    plt.scatter(query_2d[0], query_2d[1], label='Query', c='red', marker='x')

    # Aggiungi etichette e legenda
    for i, (x, y) in enumerate(docs_2d):
        plt.text(x, y, f'Doc {i+1}', fontsize=9)
    plt.text(query_2d[0], query_2d[1], 'Query', fontsize=12, color='red')

    plt.title('Visualizzazione 2D delle distanze tra query e documenti')
    plt.xlabel('Componente 1')
    plt.ylabel('Componente 2')
    plt.legend()
    plt.show()

except Exception as e:
    print(f"Si è verificato un errore durante l'embedding: {e}")
