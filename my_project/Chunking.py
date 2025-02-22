import re
from langchain_text_splitters import RecursiveCharacterTextSplitter
import json
import glob

def extract_method_name(text):
    # Pattern per la firma di un metodo in Java
    method_pattern = r'(?:public|private|protected|static|final|synchronized|abstract|native)\s+[\w<>\[\]]+\s+(\w+)\s*\([^)]*\)'
    # Pattern per i costruttori
    constructor_pattern = r'(?:public|private|protected)\s+(\w+)\s*\([^)]*\)'
    
    # Cerca la firma di un metodo
    matches = re.findall(method_pattern, text)
    if matches:
        return matches[0]  # Restituisce il primo metodo trovato
    
    # Cerca costruttori
    constr_matches = re.findall(constructor_pattern, text)
    if constr_matches:
        return constr_matches[0] + " (costruttore)"
    
    # Cerca chiamate a metodi
    method_calls = re.findall(r'\.(\w+)\s*\(', text)
    if method_calls:
        return f"Chiamata a: {method_calls[-1]}"
    return "unknown_method"

def process_file_Java(file_path):
    with open(file_path, "r", encoding="utf-8") as f:
        lines = f.readlines()
    text = ''.join(lines)

    splitter = RecursiveCharacterTextSplitter(
        chunk_size=512,  # medio-basso per prevenire merge di metodi
        chunk_overlap=128,
        separators=[
            "\n}\n\npublic ",
            "\n}\n\nprivate ",
            "\n}\n\nprotected ",
            "\n}\n\nstatic ",
            "\n}\n\n// End of method", 
            "\nclass ",  # Inizio nuove classi
            "\n@",      # Annotazioni
            "\n/**",    # Javadoc
            "\n * ", 
            "\n"
        ],
        keep_separator=True,
        is_separator_regex=False
    )

    chunks = splitter.split_text(text)
    chunk_metadata = []
    cursor = 0
    for chunk in chunks:
        start_line = text.count('\n', 0, cursor) + 1
        chunk_length = len(chunk)
        end_line = text.count('\n', 0, cursor + chunk_length) + 1
        
        # Estrae il nome del metodo dal chunk corrente
        method_name = extract_method_name(chunk)
        
        chunk_metadata.append({
            "start_line": start_line,
            "end_line": end_line,
            "text": chunk,
            "methods": [method_name]
        })
        cursor += chunk_length
    
    return chunk_metadata

files = glob.glob("my_project/classi_java_custom/*.java")
all_chunks = []
current_class = ""

for file_path in files:
    chunks_info = process_file_Java(file_path)
    for chunk_info in chunks_info:
        chunk_text = chunk_info["text"]
        
        # Se nel chunk è presente una nuova dichiarazione di classe, aggiorna il contesto
        if "class " in chunk_text:
            class_name = chunk_text.split("class ")[1].split("{")[0].strip()
            current_class = class_name
        
        # Aggiunge il chunk alla lista utilizzando il current_class corrente
        all_chunks.append({
            "id": len(all_chunks) + 1,
            "text": chunk_text,
            "source": file_path,
            "type": "code",
            "start_line": chunk_info["start_line"],
            "end_line": chunk_info["end_line"],
            "class": current_class,
            "methods": chunk_info["methods"]
        })

with open("chunks.json", "w", encoding="utf-8") as f:
    json.dump(all_chunks, f, indent=4, ensure_ascii=False)
