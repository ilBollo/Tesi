import numpy as np
from sklearn.manifold import TSNE
import matplotlib.pyplot as plt
from sklearn.metrics.pairwise import cosine_similarity
from FlagEmbedding import BGEM3FlagModel
from transformers import XLMRobertaTokenizerFast

# Inizializza il tokenizer
from transformers import XLMRobertaTokenizerFast

# Inizializza il modello BGE-M3
model = BGEM3FlagModel('BAAI/bge-m3', use_fp16=True)

def index_database(data):
    # Calcolo degli embeddings
    embeddings = model.encode(data)['dense_vecs']
    # Salvataggio degli embeddings come file .npy
    np.save('embeddings.npy', embeddings)

def load_embedding_matrix(dembeddings_path):
    # Caricamento degli embeddings per verifica
    loaded_embeddings = np.load(dembeddings_path)
    return loaded_embeddings

def search(query, embedding_matrix):
    query_embedding = model.encode([query])['dense_vecs'][0]
    similarities = cosine_similarity([query_embedding], embedding_matrix)[0]
    similarity_results = sorted(enumerate(similarities), key=lambda x: x[1], reverse=True)
    return similarity_results

def visualize_space_query(data, query, embedding_matrix):
    query_embedding = model.encode([query])['dense_vecs'][0]

    jointed_matrix = np.vstack([embedding_matrix, query_embedding])

    # Riduzione dimensionale con TSNE
    n_samples = jointed_matrix.shape[0]
    perplexity = min(n_samples - 1, 30)  # Ensure perplexity < n_samples
    tsne = TSNE(n_components=2, perplexity=perplexity, random_state=42)
    embeddings_2d = tsne.fit_transform(jointed_matrix)

    # Plotting dei risultati
    plt.figure(figsize=(8, 6))

    # Plot delle frasi
    plt.scatter(embeddings_2d[:-1, 0], embeddings_2d[:-1, 1], c='blue', edgecolor='k', label='Frasi')

    # Plot della query
    plt.scatter(embeddings_2d[-1, 0], embeddings_2d[-1, 1], c='red', edgecolor='k', label='Query')

    # Annotazioni con le frasi originali
    for i, frase in enumerate(data):
        plt.text(embeddings_2d[i, 0] + 0.1, embeddings_2d[i, 1] + 0.1, frase, fontsize=9)

    # Annotazione per la query
    plt.text(embeddings_2d[-1, 0] + 0.1, embeddings_2d[-1, 1] + 0.1, query, fontsize=9, color='red')

    plt.title('Visualizzazione degli Embeddings con t-SNE')
    plt.xlabel('Dimensione 1')
    plt.ylabel('Dimensione 2')
    plt.grid(True)
    plt.legend()
    plt.show()
"""
# Matrice di frasi
frasi = [
    "<cis-ui:button skin='red' text='Bottone rosso'/>",
    "<cis-ui:button skin='green' text='Bottone verde'/>",
    "<cis-ui:button skin='blue' text='Bottone blu'/>",
    "<cis-ui:button skin='orange' text='Bottone arancione'/>",
    "<cis-ui:button skin='gray' text='Bottone grigio'/>",
    "<cis-ui:button skin='yellow' text='Bottone giallo'/>",
    "<cis-ui:button skin='purple' text='Bottone violetto'/>",
    "<cis-ui:button skin='smaller' text='smaller' icon='fas fa-edit'/>",
    "<cis-ui:button skin='small' text='small' icon='fas fa-edit'/>",
    "<cis-ui:button skin='big' text='big' icon='fas fa-edit'/>",
    "<cis-ui:button skin='bigger' text='bigger' icon='fas fa-edit'/>",
    "<cis-ui:button skin='green small' text='green small' icon='fas fa-edit'/>",
    "<cis-ui:button text='con testo'>\n\t<beforeclick>\n\t\tfunction() {\n\t\t\tconsole.log('before click!');\n\t\t\treturn false;\n\t\t}\n\t</beforeclick>\n\t<click>\n\t\tfunction() {\n\t\t\tconsole.log('ciao');\n\t\t}\n\t</click>\n</cis-ui:button>",
    "<cis-ui:button icon='fas fa-edit'/>",
    "<cis-ui:button text='con testo e icona' icon='fas fa-edit'/>",
    "<cis-ui:button text='a sinistra' icon='fas fa-edit'/>",
    "<cis-ui:button text='a destra' icon='fas fa-edit' iconposition='right'/>",
    "<cis-ui:button text='in alto' icon='fas fa-edit' iconposition='top'/>",
    "<cis-ui:button text='in basso' icon='fas fa-edit' iconposition='bottom'/>",
    "<cis-ui:button text='non permesso' icon='fas fa-edit'>\n\t<permessi forzaabilitato='false'/>\n</cis-ui:button>",
    "<cis-ui:submit text='Submit' icon='fas fa-save'/>",
    "<cis-ui:window caption='window' url='HWTpAlleg'>\n\t<button text='Window'/>\n</cis-ui:window>",
    "<cis-ui:button text='Frame in frame'>\n\t<click>\n\t\tfunction(sender) {\n\t\t\tvar wnd = new CIS.UI.FramedWindow({\n\t\t\t\tcaption: 'framed window',\n\t\t\t\turl: document.location.href + '?r=' + Math.random()\n\t\t\t});\n\t\t\twnd.show();\n\t\t}\n\t</click>\n</cis-ui:button>",
    "<cis-ui:nuovo url='HWTpAlleg'>\n\t<button text='Nuovo'/>\n\t<params>\n\t\t<param name='IdPratica' value='5'/>\n\t</params>\n</cis-ui:nuovo>",
    "<cis-ui:dialog caption='test' height='500' width='700' modal='true'>\n\t<markup>\n\t\t<div>test riga 1</div>\n\t\t<br/>\n\t\t<div>test riga 2</div>\n\t</markup>\n\t<button text='Dialog'/>\n</cis-ui:dialog>",
    "<cis-ui:dialog caption='test' height='500' width='700' modal='true' varname='dlg00'>\n\t<markup>\n\t\t<div>test riga 1</div>\n\t\t<br/>\n\t\t<div>test riga 2</div>\n\t</markup>\n\t<button/>\n</cis-ui:dialog>\n<cis-ui:button text='Apri dialog'>\n\t<click>\n\t\tfunction() {\n\t\t\tdlg00.show();\n\t\t}\n\t</click>\n</cis-ui:button>"
]


query = "come si inserisce un icon in un cis-ui:button?"

index_database(frasi) #crea database vettoriale
matrix=load_embedding_matrix('embeddings.npy') #carica database vettoriale
search(query=query, embedding_matrix= matrix) #cerca query nel database
out = search(query=query, embedding_matrix= matrix)
print(out)

visualize_space_query(frasi, query, matrix) #visualizza spazio vettoriale con query
del model
"""