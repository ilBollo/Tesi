from langchain_text_splitters import RecursiveCharacterTextSplitter
import json

# Carica il file Java
with open("my_project/DateUtil.java", "r") as f:
    java_code = f.read()

# Suddividi il codice in chunk
splitter = RecursiveCharacterTextSplitter(
    chunk_size=512,  # Dimensione di ogni chunk
    chunk_overlap=128,  # Overlap tra chunk
    separators=["\n\n", "\n", " ", ""]  # Separatori per il codice
)

chunks = splitter.split_text(java_code)


# Salva i chunk in un file JSON
chunks_data = [
    {
        "id": i + 1,
        "text": chunk,
        "source": "DateUtil.java",  # Nome del file
        "type": "code",             # Tipo di contenuto
        "start_line": 0,            # Linea di inizio (da calcolare)
        "end_line": 0               # Linea di fine (da calcolare)
    }
    for i, chunk in enumerate(chunks)
]
with open("chunks.json", "w", encoding="utf-8") as f:
    json.dump(chunks_data, f, indent=4, ensure_ascii=False)