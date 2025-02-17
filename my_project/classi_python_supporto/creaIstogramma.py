import matplotlib.pyplot as plt
import numpy as np

# Configurazione dati
punteggi_max = [5,5,5,5,5,5,4,4,4,4,5,4,5,3,2,2,3,2,2,2,5,4,3,5,4,5,4,5,5,5]

modelli = {
    "CodeQwen1": [0,5,5,5,5,5,4,4,4,4,5,0,5,0,2,2,3,2,2,2,5,4,3,5,4,5,4,5,5,5],
    "LLAMA08": [0,5,0,5,5,5,4,4,0,4,5,4,5,0,2,2,3,0,0,2,0,4,3,5,0,5,0,5,5,5],
    "LLAMA1" : [ 0,5,0,5,5,5,4,4,0,4,5,4,5,0,2,2,3,0,2,2,0,4,3,5,4,5,4,5,5,5],
    "LLAMA04" : [0,0,0,0,0,
    0,0,0,0,0,
    0,0,0,0,2,
    2,3,0,2,2,
    5,0,3,5,4,
    0,0,0,0,0],
}

# Calcolo percentuali di completezza
percentuali = {nome: [(p/max_)*100 for p, max_ in zip(punti, punteggi_max)] for nome, punti in modelli.items()}

# Configurazione figura e assi per il grafico delle medie
plt.figure(figsize=(12, 6))
plt.title("Confronto Prestazioni Modelli RAG", fontsize=18, fontweight='bold', pad=20)

# Creazione diretta dell'asse (senza subplot)
ax = plt.gca()
colors = ['#2ecc71', '#3498db', '#e74c3c', '#f1c40f', '#9b59b6']
medie = [np.mean(p) for p in percentuali.values()]
# Barre più larghe e spaziate
bars = ax.bar(modelli.keys(), 
             medie, 
             color=colors[:len(modelli)], 
             edgecolor='black',
             width=0.6,
             zorder=3)

# Miglioramento dell'asse y
ax.set_ylim(0, 100)
ax.set_ylabel('Completezza Media (%)', fontsize=14, labelpad=15)
ax.tick_params(axis='both', which='major', labelsize=12)
ax.yaxis.set_major_formatter('{x}%')

# Griglia più evidente
ax.grid(axis='y', linestyle='--', alpha=0.7, zorder=0)

# Annotazioni riposizionate
for bar, media in zip(bars, medie):
    ax.text(bar.get_x() + bar.get_width()/2, 
            media + 2,  # Spostato sopra la barra
            f'{media:.1f}%',
            ha='center', 
            va='bottom',
            color='black',
            fontsize=12,
            fontweight='semibold')

# Aggiustamento spazi
plt.margins(y=0.1)
plt.tight_layout()
plt.savefig('confronto_valutazioni_modelli.png', dpi=300, bbox_inches='tight')
plt.show()

# Grafico 2: Punteggi per Domanda
plt.figure(figsize=(16, 10))
plt.title("Punteggi per Domanda - Confronto Modelli RAG", fontsize=20, fontweight='bold')

# Asse x: numeri delle domande (da 1 a 30)
domande = np.arange(1, len(punteggi_max) + 1)

# Traccia la linea dei punteggi per ciascun modello
for nome, punteggi in modelli.items():
    plt.plot(domande, punteggi, marker='o', label=nome, linewidth=2)

# Traccia la linea dei punteggi massimi per ogni domanda
plt.plot(domande, punteggi_max, marker='x', linestyle='--', color='black', 
         label='Punteggio Max', linewidth=2)

plt.xlabel("Numero Domanda", fontsize=14)
plt.ylabel("Punteggio", fontsize=14)
plt.xticks(domande)
plt.ylim(0, max(punteggi_max) + 1)
plt.grid(alpha=0.3)
plt.legend(fontsize=12)
plt.tight_layout()
plt.savefig('punteggi_per_domanda.png', dpi=300, bbox_inches='tight')
plt.show()
