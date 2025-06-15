package Puissance4;

// Classe représentant l'état du jeu
public class Etat {

    private int gagnant; // Le joueur gagnant
    private int derniereColonneJouee; // Dernière colonne dans laquelle un jeton a été placé
    private int dernierJoueur; // Dernier joueur ayant joué
    private boolean termine; // Voir si le jeu est terminé

    public Etat(int gagnant, int derniereColonneJouee, int dernierJoueur, boolean termine) {
        this.gagnant = gagnant;
        this.derniereColonneJouee = derniereColonneJouee;
        this.dernierJoueur = dernierJoueur;
        this.termine = termine;
    }

    public void setGagnant(int gagnant) {
        this.gagnant = gagnant;
    }

    public void setDerniereColonneJouee(int derniereColonneJouee) {
        this.derniereColonneJouee = derniereColonneJouee;
    }

    public void setDernierJoueur(int dernierJoueur) {
        this.dernierJoueur = dernierJoueur;
    }

    public void setTermine(boolean termine) {
        this.termine = termine;
    }

    public int getGagnant() {
        return gagnant;
    }

    public int getDerniereColonneJouee() {
        return derniereColonneJouee;
    }

    public int getDernierJoueur() {
        return dernierJoueur;
    }

    public boolean estTermine() {
        return termine;
    }

    public boolean verifierPartieTerminee(Plateau plateau) {
        if (termine) {
            return true;
        }

        if (dernierJoueurGagnant(plateau)) {
            this.termine = true;
            this.gagnant = this.dernierJoueur;
            return true;
        }

        if (plateau.estGrillePleine()) {
            this.termine = true;
            this.gagnant = 0; // match nul
            return true;
        }

        return false;
    }

    // Vérifie si le dernier joueur a gagné
    private boolean dernierJoueurGagnant(Plateau plateau) {
        int ligne = obtenirLigneDansColonne(plateau);
        int[][] grille = plateau.getGrille();
        int nbColonnes = plateau.getNbColonnes();
        int nbLignes = plateau.getNbLignes();

        int compteurColonne = 0;
        int compteurLigne = 0;
        int compteurDiag1 = 0;
        int compteurDiag2 = 0;

        for (int i = -3; i <= 3; i++) {
            // Vérification colonne
            if (ligne + i >= 0 && ligne + i < nbLignes && grille[ligne + i][derniereColonneJouee] == dernierJoueur) {
                compteurColonne++;
            } else {
                compteurColonne = 0;
            }
            if (compteurColonne == 4) {
                return true;
            }

            // Vérification ligne
            if (derniereColonneJouee + i >= 0 && derniereColonneJouee + i < nbColonnes && grille[ligne][derniereColonneJouee + i] == dernierJoueur) {
                compteurLigne++;
            } else {
                compteurLigne = 0;
            }
            if (compteurLigne == 4) {
                return true;
            }

            // Vérification diagonale \
            if (derniereColonneJouee + i >= 0 && derniereColonneJouee + i < nbColonnes && ligne + i >= 0 && ligne + i < nbLignes && grille[ligne + i][derniereColonneJouee + i] == dernierJoueur) {
                compteurDiag1++;
            } else {
                compteurDiag1 = 0;
            }
            if (compteurDiag1 == 4) {
                return true;
            }

            // Vérification diagonale /
            if (derniereColonneJouee - i >= 0 && derniereColonneJouee - i < nbColonnes && ligne + i >= 0 && ligne + i < nbLignes && grille[ligne + i][derniereColonneJouee - i] == dernierJoueur) {
                compteurDiag2++;
            } else {
                compteurDiag2 = 0;
            }
            if (compteurDiag2 == 4) {
                return true;
            }
        }
        return false;
    }

    // Obtient la ligne où le dernier jeton a été placé dans la colonne
    private int obtenirLigneDansColonne(Plateau plateau) {
        int ligneDisponible = plateau.ligneDisponible(derniereColonneJouee);
        if (ligneDisponible != plateau.getNbLignes() - 1) {
            return ligneDisponible + 1;
        }
        return -1;
    }

    // Calcule le nombre de jetons alignés pour un joueur donné
    public int[] calculerJetons(Plateau plateau, int jeton) {
        int[][] grille = plateau.getGrille();
        int nbLignes = plateau.getNbLignes();
        int nbColonnes = plateau.getNbColonnes();
        int[] compteJetons = new int[5];

        for (int i = 0; i < nbLignes; i++) {
            for (int j = 0; j < nbColonnes; j++) {
                if ((grille[i][j] != jeton) && (grille[i][j] != 0)) {
                    continue;
                }
                if (j <= nbColonnes - 4) {
                    compteJetons[traduire(jeton, grille[i][j], grille[i][j + 1], grille[i][j + 2], grille[i][j + 3])] ++;
                }
                if (i <= nbLignes - 4) {
                    compteJetons[traduire(jeton, grille[i][j], grille[i + 1][j], grille[i + 2][j], grille[i + 3][j])] ++;
                }
                if (i <= nbLignes - 4 && j <= nbColonnes - 4) {
                    compteJetons[traduire(jeton, grille[i][j], grille[i + 1][j + 1], grille[i + 2][j + 2], grille[i + 3][j + 3])] ++;
                }
                if (i <= nbLignes - 4 && j >= 3) {
                    compteJetons[traduire(jeton, grille[i][j], grille[i + 1][j - 1], grille[i + 2][j - 2], grille[i + 3][j - 3])] ++;
                }
            }
        }
        return compteJetons;
    }

    // Traduit les jetons alignés en un score
    public static int traduire(int jeton, int a, int b, int c, int d) {
        if ((a != jeton && a != 0) || (b != jeton && b != 0) || (c != jeton && c != 0) || (d != jeton && d != 0)) {
            return 0;
        }
        return (a + b + c + d) / jeton;
    }
}
