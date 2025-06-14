package Puissance4;

import Algorithmes.MCTS;

public class Joueur {
    private Algorithmes algorithme; // Type d'algorithme utilisé par le joueur
    private Integer parametre; // Paramètre spécifique à l'algorithme utilisé
    private Double constanteCMCTS; // Constante pour l'algorithme MCTS (Monte Carlo Tree Search)
    private Integer nbSimulationsMCTS; // Nombre de simulations pour l'algorithme MCTS
    private transient MCTS instanceMCTS; // Nouveau champ: référence à l'instance MCTS (transient pour éviter la sérialisation)

    // Constructeur
    public Joueur(Algorithmes algorithme, Integer parametre) {
        this.algorithme = algorithme;
        this.parametre = parametre;
        this.constanteCMCTS = Math.sqrt(2); // Valeur par défaut recommandée
        this.nbSimulationsMCTS = 1; // Valeur par défaut
        if (algorithme == Algorithmes.MCTS) {
            this.instanceMCTS = new MCTS(1, 2); // À adapter selon tes besoins (jetonJoueur/jetonAdversaire)
        }
    }

    // --- Méthodes existantes (inchangées) ---
    public void setNbSimulationsMCTS(Integer nbSimulationsMCTS) {
        this.nbSimulationsMCTS = nbSimulationsMCTS;
        if (instanceMCTS != null) instanceMCTS.definirNbSimulations(nbSimulationsMCTS);
    }

    public Integer getNbSimulationsMCTS() {
        return nbSimulationsMCTS;
    }

    public Algorithmes getAlgorithme() {
        return algorithme;
    }

    public void setAlgorithme(Algorithmes algorithme) {
        this.algorithme = algorithme;
    }

    public Integer getParametre() {
        return parametre;
    }

    public void setParametre(Integer parametre) {
        this.parametre = parametre;
    }

    public void setConstanteCMCTS(Double constanteCMCTS) {
        this.constanteCMCTS = constanteCMCTS;
        if (instanceMCTS != null) instanceMCTS.definirConstanteC(constanteCMCTS);
    }

    public Double getConstanteCMCTS() {
        return constanteCMCTS;
    }

    public boolean estHumain() {
        return algorithme == null;
    }

    // --- Nouvelle méthode pour accéder à MCTS ---
    public MCTS getInstanceMCTS() {
        return instanceMCTS;
    }
}