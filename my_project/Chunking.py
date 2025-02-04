from langchain_text_splitters import RecursiveCharacterTextSplitter
import json

# Funzione per caricare e suddividere un file Java
def process_file(file_path):
    with open(file_path, "r", encoding="utf-8") as f:
     lines = f.readlines()

    # Ricostruisce il testo mantenendo le informazioni sulle linee
    text = ''.join(lines)

    splitter = RecursiveCharacterTextSplitter(
    chunk_size=512,
    chunk_overlap=128,
    separators=[
        "\n}\n\npublic",  # I seguenti separatori sono stati usati per provare a mantenere i metodi uniti
        "\n}\n\nprivate",
        "\n}\n\nprotected",
        "\n}\n\n//",       # Nuovo separatore per commenti
        "\nclass ",
        "\n@Override",     # Cattura le implementazioni di interfacce
        "\n@Test",         # Per eventuali test case
        "\n/**",           # Separatore per Javadoc
        "\n * ",
        "\n"
    ],
    keep_separator=True,
    is_separator_regex=False
)

    chunks = splitter.split_text(text)
    # Calcola le linee esatte per ogni chunk
    chunk_metadata = []
    cursor = 0
    for chunk in chunks:
        start_line = text.count('\n', 0, cursor) + 1
        chunk_length = len(chunk)
        end_line = text.count('\n', 0, cursor + chunk_length) + 1
        chunk_metadata.append({
            "start_line": start_line,
            "end_line": end_line,
            "text": chunk
        })
        cursor += chunk_length
    
    return chunk_metadata

# Carica e suddividi i file Java
files = ["my_project/DateUtilCustom.java", "my_project/GiorniMagici.java", "my_project/BasketballStats.java"]
all_chunks = []

for file_path in files:
    chunks_info = process_file(file_path)
    for chunk_info in chunks_info:
        chunk_text = chunk_info["text"]
        
        # Aggiungi contesto strutturale
        class_context = ""
        if "class " in chunk_text:
            class_name = chunk_text.split("class ")[1].split("{")[0].strip()
            class_context = f"Classe: {class_name}\n"
        
        all_chunks.append({
            "id": len(all_chunks) + 1,
            "text": f"// File: {file_path}\n{class_context}{chunk_text}",
            "source": file_path,
            "type": "code",
            "start_line": chunk_info["start_line"],
            "end_line": chunk_info["end_line"],
            "class": class_context.replace("Classe: ", "") if class_context else ""
        })

# Salva i chunk in un file JSON
with open("chunks.json", "w", encoding="utf-8") as f:
    json.dump(all_chunks, f, indent=4, ensure_ascii=False)