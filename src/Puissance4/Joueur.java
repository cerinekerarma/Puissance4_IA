package Puissance4;

public class Joueur {
    private Algorithmes algorithme; // Type d'algorithme utilisé par le joueur
    private Integer parametre; // Paramètre spécifique à l'algorithme utilisé
    private Double constanteCMCTS; // Constante pour l'algorithme MCTS (Monte Carlo Tree Search)
    private Integer nbSimulationsMCTS; // Nombre de simulations pour l'algorithme MCTS

    // Constructeur
    public Joueur(Algorithmes algorithme, Integer parametre) {
        this.algorithme = algorithme;
        this.parametre = parametre;
        this.constanteCMCTS = Math.sqrt(2); // Valeur par défaut recommandée
        this.nbSimulationsMCTS = 1; // Valeur par défaut
    }

    // Getters et setters
    public void setNbSimulationsMCTS(Integer nbSimulationsMCTS) {
        this.nbSimulationsMCTS = nbSimulationsMCTS;
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
    }

    public Double getConstanteCMCTS() {
        return constanteCMCTS;
    }

    // Vérifie si le joueur est humain (aucun algorithme défini)
    public boolean estHumain() {
        return algorithme == null;
    }
}
