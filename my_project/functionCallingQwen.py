from transformers import AutoTokenizer, AutoModelForCausalLM
from llama_index.core import VectorStoreIndex, SimpleDirectoryReader
from langchain.embeddings import HuggingFaceEmbeddings

# Caricamento del modello Qwen2.5-Coder:3B
model_name = "Qwen/Qwen2.5-Coder-3B"
tokenizer = AutoTokenizer.from_pretrained(model_name, trust_remote_code=True)
model = AutoModelForCausalLM.from_pretrained(model_name, device_map="auto")




# Configurazione embedding model
Settings.embed_model = HuggingFaceEmbeddings(model_name="BAAI/bge-small-zh-v1.5")

# Creazione indice da documenti
documents = SimpleDirectoryReader("data").load_data()
index = VectorStoreIndex.from_documents(documents)
query_engine = index.as_query_engine(similarity_top_k=3)