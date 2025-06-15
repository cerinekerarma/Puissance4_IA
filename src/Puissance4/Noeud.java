package Puissance4;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Noeud {
    private Plateau plateau;
    private List<Noeud> fils;
    private Noeud parent;
    private int nbVisites;
    private double nbVictoires;
    private int dernierCoup;
    private int profondeur;
    private List<Integer> actionsPossibles;
    private List<Integer> actionsTentees;
    private static final Random random = new Random();

    public Noeud(Plateau plateau, Noeud parent, int dernierCoup, int joueur) {
        this.plateau = plateau;
        this.parent = parent;
        this.dernierCoup = dernierCoup;
        this.profondeur = (parent == null) ? 0 : parent.getProfondeur() + 1;
        this.fils = new ArrayList<>();
        this.nbVisites = 0;
        this.nbVictoires = 0;
        this.actionsPossibles = plateau.colonnesValides();
        this.actionsTentees = new ArrayList<>();
    }

    // Création de la racine
    public static Noeud creerNoeudRacine(Plateau plateau) {
        return new Noeud(plateau, null, -1, 0);
    }

    public int getUneActionNonTentee() {
        List<Integer> nonTentees = new ArrayList<>(actionsPossibles);
        nonTentees.removeAll(actionsTentees);
        return nonTentees.isEmpty() ? -1 : nonTentees.get(random.nextInt(nonTentees.size()));
    }

    public Noeud creerFils(int colonne, int jeton) {
        Plateau nouveauPlateau = plateau.clone();
        nouveauPlateau.ajouterJetonDansColonne(colonne, jeton);

        Noeud fils = new Noeud(nouveauPlateau, this, colonne, jeton);
        this.fils.add(fils);
        this.actionsTentees.add(colonne);
        return fils;
    }

    public boolean toutesActionsRealisees() {
        return actionsTentees.size() == actionsPossibles.size();
    }

    public int getDernierCoup() {
        return dernierCoup;
    }

    public List<Noeud> getFils() {
        return fils;
    }

    public Plateau getPlateau() {
        return plateau;
    }

    public boolean estFeuille() {
        return this.plateau.colonnesValides().isEmpty();
    }

    public int jouerAction(int colonne, int jeton) {
        this.dernierCoup = colonne;
        return this.plateau.ajouterJetonDansColonne(colonne, jeton);
    }

    public void creerTousFils(int jeton) {
        this.fils.clear();
        this.actionsTentees.clear();
        for (int colonne : plateau.colonnesValides()) {
            this.creerFils(colonne, jeton);
        }
    }

    public int compterNoeuds() {
        int compteur = 1;
        for (Noeud enfant : fils) {
            compteur += enfant.compterNoeuds();
        }
        return compteur;
    }

    public int getProfondeurMaximale() {
        if (fils.isEmpty()) {
            return 0;
        } else {
            int max = 0;
            for (Noeud enfant : fils) {
                int profondeurEnfant = enfant.getProfondeurMaximale();
                if (profondeurEnfant > max) {
                    max = profondeurEnfant;
                }
            }
            return max + 1;
        }
    }

    public int getNbVisites() {
        return nbVisites;
    }

    public double getNbVictoires() {
        return nbVictoires;
    }

    public int getProfondeur() {
        return profondeur;
    }

    public Noeud getParent() {
        return parent;
    }

    public void setNbVisites(int nbVisites) {
        this.nbVisites = nbVisites;
    }

    public void setNbVictoires(double nbVictoires) {
        this.nbVictoires = nbVictoires;
    }

    public List<Integer> getActionsPossibles() {
        return new ArrayList<>(actionsPossibles);
    }

    public List<Integer> getActionsTentees() {
        return new ArrayList<>(actionsTentees);
    }
}