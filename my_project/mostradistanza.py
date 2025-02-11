import numpy as np
import matplotlib.pyplot as plt
from sklearn.manifold import TSNE

# Supponiamo che `query_embedding` sia l'embedding della tua query
# e `doc_embeddings` sia una lista di embedding dei documenti trovati

# Esempio di embedding (sostituisci con i tuoi dati reali)
query_embedding = np.random.rand(768)  # Sostituisci con l'embedding reale della query
doc_embeddings = np.random.rand(5, 768)  # Sostituisci con gli embedding reali dei documenti

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