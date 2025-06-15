# Projet Puissance 4 – IA avec Minimax, Alpha-Beta et MCTS

## Présentation du projet

Ce projet a pour objectif de développer une intelligence artificielle pour le jeu **Puissance 4** en mettant en œuvre et en comparant trois algorithmes de recherche bien connus :

- **Minimax**
- **Alpha-Beta Pruning**
- **Monte Carlo Tree Search (MCTS)**

L’implémentation permet non seulement de jouer contre un joueur ou une IA mais aussi de faire jouer deux IA entre elles tout en analysant les performances et la qualité des décisions prises par chaque algorithme.


## Lancer le jeu

Pour exécuter le jeu :

1. Lancez la classe **`Puissance4Vue`**.
2. Suivez l’interface intuitive :
   - Choix du **mode de jeu** : joueur vs joueur, joueur vs IA, ou IA vs IA.
   - Choix de l’**algorithme** utilisé pour chaque IA (Minimax, Alpha-Beta, ou MCTS) avec des valeurs par défault.


## Tests et évaluation

Des tests ont été réalisés pour comparer les algorithmes selon plusieurs critères : profondeur, nombre d’itérations, taux de victoire, etc.

### Classes de test :

- `TestAlphaBetaVsAlphaBeta` : évalue **Alpha-Beta** contre lui-même avec différentes profondeurs.
- `TestMinimaxVsMinimax` : évalue **Minimax** contre lui-même avec différentes profondeurs.
- `TestAlphaBetaVsMinimax` : compare **Alpha-Beta** à **Minimax**.
- `TestMinimaxVsMCTS` : compare **Minimax** à **MCTS**.
- `TestMCTS` : analyse la structure des arbres générés par **MCTS** (profondeur, nœuds, taille de branche...).

---

## Résultats

Les résultats de chaque test sont automatiquement enregistrés dans des fichiers `.txt` dans le dossier **`resultats/`**.


## Analyse statistique et visualisation

Un notebook Python permet d’analyser et de visualiser les résultats des tests :

> **`Puissance4_notebook.ipynb`**

Ce notebook lit les fichiers générés, calcule des statistiques (taux de réussite, comparaisons) et produit des graphiques pour illustrer les performances des algorithmes.

## Rapport

Notre rapport `Rapport_IA3_GALICHET_KERARMA.pdf` regroupe l’ensemble du travail réalisé, explique la logique de chaque partie, montre les résultats obtenus et propose une interprétation claire.
Il permet de comprendre les forces et limites de chaque algorithme à travers des confrontations simulées et l’analyse des arbres de recherche générés.

## Auteurs

**Amaury Galichet** & **Cerine KERARMA**  
4A Informatique, Polytech Lyon  
Dans le cadre du cours *Intelligence Artificielle - Partie 3*, encadré par **M. Stéphane Bonnevay**
