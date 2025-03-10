from transformers import XLMRobertaTokenizerFast
from FlagEmbedding import BGEM3FlagModel
import numpy as np
import matplotlib.pyplot as plt
from sklearn.manifold import TSNE

# Inizializza il tokenizer
tokenizer = XLMRobertaTokenizerFast.from_pretrained('xlm-roberta-base')

# Inizializza il modello BGE-M3
model = BGEM3FlagModel('BAAI/bge-m3', use_fp16=True)

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

# Tokenizza le frasi
tokens = tokenizer(frasi, padding=True, truncation=True, return_tensors='pt')

# Genera embeddings per ogni frase
embeddings = model.encode(frasi)['dense_vecs']

# Salva gli embeddings come matrice in un file .npy
np.save('embeddings.npy', embeddings)

# Carica gli embeddings salvati (opzionale, per conferma)
loaded_embeddings = np.load('embeddings.npy')

# Riduzione dimensionale con TSNE
tsne = TSNE(n_components=2, random_state=42, perplexity=5)
embeddings_2d = tsne.fit_transform(loaded_embeddings)

# Plot degli embeddings
plt.figure(figsize=(10, 8))
plt.scatter(embeddings_2d[:, 0], embeddings_2d[:, 1], c='blue', alpha=0.7)
for i, frase in enumerate(frasi):
    plt.text(embeddings_2d[i, 0] + 0.5, embeddings_2d[i, 1] + 0.5, frase[:20], fontsize=8)
plt.title('Visualizzazione degli embeddings con TSNE')
plt.xlabel('Dimensione 1')
plt.ylabel('Dimensione 2')
plt.grid(True)
plt.show()
