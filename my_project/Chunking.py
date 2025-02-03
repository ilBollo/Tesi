from langchain_text_splitters import RecursiveCharacterTextSplitter
import json

# Funzione per caricare e suddividere un file Java
def process_file(file_path):
    with open(file_path, "r", encoding="utf-8") as f:
        java_code = f.read()

    splitter = RecursiveCharacterTextSplitter(
        chunk_size=1024,  # Dimensione di ogni chunk
        chunk_overlap=256,  # Overlap tra chunk
        separators=["\n\n", "\n", " ", ""]  # Separatori per il codice
    )

    chunks = splitter.split_text(java_code)
    return chunks

# Carica e suddividi i file Java
files = ["my_project/DateUtil.java", "my_project/GiorniMagici.java"]
all_chunks = []

for file_path in files:
    chunks = process_file(file_path)
    for i, chunk in enumerate(chunks):
        all_chunks.append({
            "id": len(all_chunks) + 1,
            "text": chunk,
            "source": file_path,  # Nome del file
            "type": "code",       # Tipo di contenuto
            "start_line": 0,      # Linea di inizio (da calcolare)
            "end_line": 0         # Linea di fine (da calcolare)
        })

# Salva i chunk in un file JSON
with open("chunks.json", "w", encoding="utf-8") as f:
    json.dump(all_chunks, f, indent=4, ensure_ascii=False)