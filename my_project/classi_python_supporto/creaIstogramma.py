import matplotlib.pyplot as plt
import numpy as np

# Configurazione dati
punteggi_max = [5,5,5,5,5,5,4,4,4,4,5,4,5,3,2,2,3,2,2,2,5,4,3,5,4,5,4,5,5,5]

modelli = {
    "CodeQwen1": [0,5,5,5,5,5,4,4,4,4,5,0,5,0,2,2,3,2,2,2,5,4,3,5,4,5,4,5,5,5],
    "LLAMA08": [0,5,0,5,5,5,4,4,0,4,5,4,5,0,2,2,3,0,0,2,0,4,3,5,0,5,0,5,5,5],
    "LLAMA1" : [ 0,5,0,5,5,
                5,4,4,0,4,
                5,4,5,0,2,
                2,3,0,2,2,
                0,4,3,5,4,
                5,4,5,5,5]
}

# Calcolo percentuali di completezza
percentuali = {nome: [(p/max_)*100 for p, max_ in zip(punti, punteggi_max)] for nome, punti in modelli.items()}

# Inizializzazione grafico
plt.figure(figsize=(16, 10))
plt.suptitle("Confronto Prestazioni Modelli RAG", y=0.95, fontsize=20, fontweight='bold')

# Grafico a barre (Medie)
ax1 = plt.subplot(2, 2, (1, 3))
colors = ['#2ecc71', '#3498db', '#e74c3c', '#f1c40f', '#9b59b6']
medie = [np.mean(p) for p in percentuali.values()]

bars = ax1.bar(modelli.keys(), medie, 
               color=colors[:len(modelli)], 
               edgecolor='black',
               width=0.7)

ax1.set_ylim(0, 100)
ax1.set_ylabel('Completezza Media (%)', fontsize=14)
ax1.tick_params(axis='both', labelsize=12)
ax1.grid(axis='y', alpha=0.3)

# Annotazioni valori medi
for bar, media in zip(bars, medie):
    ax1.text(bar.get_x() + bar.get_width()/2, 5,
            f'{media:.1f}%',
            ha='center', va='bottom',
            color='black' if media < 50 else 'white',
            fontsize=14,
            fontweight='bold')

# Boxplot (Distribuzioni)
ax2 = plt.subplot(2, 2, 2)
positions = range(1, len(modelli)+1)

box = ax2.boxplot(percentuali.values(),
                 vert=False,
                 patch_artist=True,
                 widths=0.6,
                 showfliers=True,
                 boxprops=dict(facecolor='#3498db', alpha=0.7),
                 medianprops=dict(color='white'))

ax2.set_yticklabels(modelli.keys())
ax2.set_xlim(0, 100)
ax2.set_xlabel('Percentuale Completezza', fontsize=14)
ax2.tick_params(axis='both', labelsize=12)
ax2.grid(axis='x', alpha=0.3)

# Tabella riassuntiva
ax3 = plt.subplot(2, 2, 4)
ax3.axis('off')

stats_data = []
for nome, p in percentuali.items():
    stats_data.append([
        nome,
        f'{np.mean(p):.1f} ± {np.std(p):.1f}%',
        f'{np.median(p):.1f}%',
        f'{np.min(p):.1f}% - {np.max(p):.1f}%'
    ])

table = ax3.table(
    cellText=stats_data,
    colLabels=['Modello', 'Media ± Dev.Std', 'Mediana', 'Range'],
    colColours=['#eeeeee']*4,
    cellLoc='center',
    loc='center'
)

table.auto_set_font_size(False)
table.set_fontsize(12)
table.scale(1.2, 1.5)

# Salvataggio e visualizzazione
plt.tight_layout(pad=4)
plt.savefig('confronto_modelli.png', dpi=300, bbox_inches='tight')

plt.show()