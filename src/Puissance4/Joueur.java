package Puissance4;

import Algorithmes.MCTS;

public class Joueur {
    private Algorithmes algorithme; // Type d'algorithme utilisé par le joueur
    private Integer parametre; // Paramètre spécifique à l'algorithme utilisé
    private Double constanteCMCTS; // Constante C de MCTS
    private Integer nbSimulationsMCTS; // Nombre de simulations pour MCTS
    private transient MCTS instanceMCTS; // Référence à l'instance MCTS (pour éviter la sérialisation)

    public Joueur(Algorithmes algorithme, Integer parametre) {
        this.algorithme = algorithme;
        this.parametre = parametre;
        this.constanteCMCTS = Math.sqrt(2); // Valeur par défaut
        this.nbSimulationsMCTS = 10; // Valeur par défaut
        if (algorithme == Algorithmes.MCTS) {
            this.instanceMCTS = new MCTS(1, 2); // jetonJoueur/jetonAdversaire
        }
    }

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

    /*public MCTS getInstanceMCTS() {
        return instanceMCTS;
    }*/
}