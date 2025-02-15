from langchain_community.vectorstores import FAISS
from langchain_huggingface import HuggingFaceEmbeddings
import numpy as np
import matplotlib.pyplot as plt
from sklearn.manifold import TSNE

# 1. Carica modello e database
embedder = HuggingFaceEmbeddings(
    model_name="BAAI/bge-m3",
    model_kwargs={'device': 'cpu'},
    encode_kwargs={'normalize_embeddings': True}
)

vector_store = FAISS.load_local(
    folder_path="./faiss_db",
    embeddings=embedder,
    allow_dangerous_deserialization=True
)

# 2. Esegui la query
query = "Cosa ritorna il metodo segnaleWow(LocalDate.of(2025, 1, 10)) che utilizza la funzione getMessaggioMagico() della libreria DateUtilCustom?"
docs = vector_store.similarity_search_with_score(query, k=5, score_threshold=0.80)

# 3. Controllo risultati
if not docs:
    print("Nessun documento trovato sopra la soglia specificata!")
    exit()

# 4. Genera embeddings
doc_texts = [doc.page_content for doc, _ in docs]
try:
    query_embedding = np.array(embedder.embed_query(query)).reshape(1, -1)
    doc_embeddings = np.array(embedder.embed_documents(doc_texts))
except Exception as e:
    print(f"Errore durante la generazione degli embedding: {e}")
    exit()

# 5. Prepara dati per t-SNE
all_embeddings = np.vstack([query_embedding, doc_embeddings])
n_samples = all_embeddings.shape[0]

# 6. Configura t-SNE in modo dinamico
perplexity = min(5, n_samples - 1)  # Adatta automaticamente la perplexity
if perplexity < 1:
    print("Troppi pochi campioni per la visualizzazione")
    exit()

tsne = TSNE(
    n_components=2,
    perplexity=perplexity,
    random_state=42,
    init='pca'  # Migliora la stabilità con pochi campioni
)

# 7. Esegui riduzione dimensionale
try:
    embeddings_2d = tsne.fit_transform(all_embeddings)
except Exception as e:
    print(f"Errore t-SNE: {e}")
    exit()

# 8. Crea visualizzazione
plt.figure(figsize=(10, 6))
plt.scatter(embeddings_2d[1:, 0], embeddings_2d[1:, 1], c='blue', label='Chunk')
plt.scatter(embeddings_2d[0, 0], embeddings_2d[0, 1], c='green', marker='X', s=200, label='Query')

# Aggiungi etichette
for i, (x, y) in enumerate(embeddings_2d[1:]):
    plt.annotate(f'Chunk {i+1}', (x, y), textcoords="offset points", xytext=(0,5), ha='center')
plt.annotate('Query', (embeddings_2d[0, 0], embeddings_2d[0, 1]), textcoords="offset points", xytext=(0,15), ha='center', color='blue')

plt.title(f'Relazione Query-Chunk (Trovati: {perplexity})')
plt.xlabel('Componente t-SNE 1')
plt.ylabel('Componente t-SNE 2')
plt.legend()
plt.grid(True, alpha=0.3)
plt.tight_layout()
plt.show()