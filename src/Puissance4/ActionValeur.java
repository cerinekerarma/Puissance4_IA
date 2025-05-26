package Puissance4;

public class ActionValeur {
    private Double u; // Valeur (utilité)
    private Integer a; // Action (colonne choisie)

    /**
     * Constructeur pour initialiser la valeur et l'action.
     *
     * @param u Valeur de l'évaluation
     * @param a Numéro de la colonne représentant l'action
     */
    public ActionValeur(Double u, Integer a) {
        this.u = u;
        this.a = a;
    }

    public Double getU() {
        return u;
    }

    public void setU(Double u) {
        this.u = u;
    }

    public Integer getA() {
        return a;
    }

    public void setA(Integer a) {
        this.a = a;
    }

}
