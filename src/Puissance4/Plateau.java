package Puissance4;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Plateau {
    private int nbLignes;
    private int nbColonnes;
    private int[][] grille;
    private Etat etat;

    public Plateau(int nbLignes, int nbColonnes, int dernierJoueur, int[][] grille) {
        this.nbLignes = nbLignes;
        this.nbColonnes = nbColonnes;
        if (grille == null) {
            this.grille = new int[nbLignes][nbColonnes];
        } else {
            this.grille = grille;
        }
        this.etat = new Etat(0, -1, dernierJoueur, false);
    }

    public Etat getEtat() {
        return etat;
    }

    public int getNbColonnes() {
        return nbColonnes;
    }

    public int getNbLignes() {
        return nbLignes;
    }

    public int[][] getGrille() {
        return grille;
    }

    public int getDernierJoueur() {
        return etat.getDernierJoueur();
    }

    public int getVainqueur() {
        return etat.getGagnant();
    }

    public int getJetonAdverse() {
        return etat.getDernierJoueur() % 2 + 1;
    }

    public boolean colonneValide(int colonne) {
        return grille[0][colonne] == 0;
    }

    public Plateau clone() {
        return new Plateau(this.nbLignes, this.nbColonnes, this.etat.getDernierJoueur(), Puissance4Logique.clonerTableau2D(this.grille));
    }

    public int[] compterJetonsAlignes(int jetonJoueur) {
        int[] compte = new int[5];
        Puissance4Logique.calculerJetonsAlignesLigne(this, jetonJoueur, compte);
        Puissance4Logique.calculerJetonsAlignesColonne(this, jetonJoueur, compte);
        Puissance4Logique.calculerJetonsAlignesDiagonale(this, jetonJoueur, compte);
        return compte;
    }

    public int ajouterJetonDansColonne(int colonne, int jeton) {
        int ligne = ligneDisponible(colonne);
        if (ligne != -1) {
            grille[ligne][colonne] = jeton;
            etat.setDernierJoueur(jeton);
            etat.setDerniereColonneJouee(colonne);
            etat.verifierPartieTerminee(this);
        }
        return ligne;
    }

    public int ligneDisponible(int colonne) {
        for (int i = nbLignes - 1; i >= 0; i--) {
            if (grille[i][colonne] == 0) {
                return i;
            }
        }
        return -1;
    }

    public List<Integer> colonnesValides() {
        List<Integer> colonnesDisponibles = new ArrayList<>();
        for (int i = 0; i < nbColonnes; i++) {
            if (grille[0][i] == 0) {
                colonnesDisponibles.add(i);
            }
        }
        return colonnesDisponibles;
    }

    public boolean estGrillePleine() {
        return colonnesValides().isEmpty();
    }

    public int ajouterJetonAleatoire(int[] jetonsJoueurs) {
        List<Integer> joueursDisponibles = new ArrayList<>();
        for (int joueur : jetonsJoueurs) {
            if (joueur != etat.getDernierJoueur()) {
                joueursDisponibles.add(joueur);
            }
        }

        Random random = new Random();
        List<Integer> colonnes = colonnesValides();
        int colonne = colonnes.get(random.nextInt(colonnes.size()));
        int joueur = joueursDisponibles.get(random.nextInt(joueursDisponibles.size()));

        ajouterJetonDansColonne(colonne, joueur);
        return colonne;
    }

    public void remplirAleatoirement(int[] jetons) {
        while (!etat.estTermine()) {
            ajouterJetonAleatoire(jetons);
        }
    }

    public void afficherGrille() {
        for (int i = 0; i < nbLignes; i++) {
            System.out.print("[");
            for (int j = 0; j < nbColonnes; j++) {
                System.out.print(grille[i][j]);
                if (j < nbColonnes - 1) {
                    System.out.print(", ");
                }
            }
            System.out.println("]");
        }
    }

    public int evaluation(int jetonMax, int jetonMin) {
        return evaluer(jetonMax) - evaluer(jetonMin);
    }

    // Différentes fonctions d'évaluations (à décommenter pour run les tests)
    public int evaluer(int jetonJoueur) {
        int[] jetonsAlignes = compterJetonsAlignes(jetonJoueur);
        // Version 1 - Évaluation standard (celle proposée dans le cours)
        // Poids exponentiels pour les alignements de jetons
        return 1000 * jetonsAlignes[4] + 50 * jetonsAlignes[3] + 5 * jetonsAlignes[2] + 1 * jetonsAlignes[1];


        // Version 2 - Évaluation offensive
        // Favorise davantage les alignements de 3 jetons (plus agressive)
        //return 10000 * jetonsAlignes[4] + 200 * jetonsAlignes[3] + 10 * jetonsAlignes[2] + 1 * jetonsAlignes[1];


        // Version 3 - Évaluation défensive
        // Ignore les alignements de 1 jeton et réduit l'importance des petits alignements
        //return 1000 * jetonsAlignes[4] + 30 * jetonsAlignes[3] + 2 * jetonsAlignes[2];



    }
}
