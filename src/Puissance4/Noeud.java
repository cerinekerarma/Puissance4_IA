package Puissance4;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Noeud {
    private Plateau plateau;
    private HashMap<Integer, Boolean> actionsRealisees;
    private List<Noeud> fils;
    private Noeud parent;
    private int nbVisites;
    private double nbVictoires;
    private int dernierCoup;
    private int profondeur;

    public Noeud(Plateau plateau, HashMap<Integer, Boolean> actionsRealisees, List<Noeud> fils, Noeud parent, int nbVisites, double nbVictoires, int dernierCoup, int profondeur) {
        this.plateau = plateau;
        this.fils = fils;
        this.actionsRealisees = actionsRealisees;
        this.parent = parent;
        this.nbVisites = nbVisites;
        this.nbVictoires = nbVictoires;
        this.dernierCoup = dernierCoup;
        this.profondeur = profondeur;
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

    public HashMap<Integer, Boolean> getActionsRealisees() {
        return actionsRealisees;
    }

    public boolean toutesActionsRealisees() {
        for (Map.Entry<Integer, Boolean> action : actionsRealisees.entrySet()) {
            if (!action.getValue()) {
                return false;
            }
        }
        return true;
    }

    public static Noeud creerNoeudRacine(Plateau plateau) {
        Noeud nouveau = new Noeud(plateau, new HashMap<>(), new ArrayList<>(), null, 0, 0, 0, 0);
        nouveau.reinitialiserActions();
        return nouveau;
    }

    public void reinitialiserActions() {
        List<Integer> colonnesValides = this.plateau.colonnesValides();
        this.actionsRealisees = new HashMap<>();
        for (int colonne : colonnesValides) {
            actionsRealisees.put(colonne, false);
        }
    }

    public boolean estFeuille() {
        return this.plateau.colonnesValides().isEmpty();
    }

    public int jouerAction(int colonne, int jeton) {
        this.dernierCoup = colonne;
        return this.plateau.ajouterJetonDansColonne(colonne, jeton);
    }

    public Noeud creerFils(int colonne, int jeton) {
        Noeud enfant = Noeud.creerNoeudRacine(plateau.clone());
        enfant.jouerAction(colonne, jeton);
        enfant.parent = this;
        enfant.profondeur = this.profondeur + 1;
        this.fils.add(enfant);
        this.actionsRealisees.put(colonne, true);
        return enfant;
    }

    public void creerTousFils(int jeton) {
        this.fils.clear();
        reinitialiserActions();
        List<Integer> colonnesValides = this.plateau.colonnesValides();
        for (int colonne : colonnesValides) {
            this.creerFils(colonne, jeton);
        }
    }

    public int getUneActionNonTentee() {
        for (Map.Entry<Integer, Boolean> action : actionsRealisees.entrySet()) {
            if (!action.getValue()) {
                return action.getKey();
            }
        }
        return -1;
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
}
