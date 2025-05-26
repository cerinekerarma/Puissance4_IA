package Puissance4;

// Classe utilitaire contenant des méthodes pour le jeu Puissance 4
public class Puissance4_algos {

    // Méthode pour cloner un tableau 2D
    public static int[][] clonerTableau2D(int[][] tableau) {
        int[][] copie = new int[tableau.length][];
        for (int i = 0; i < tableau.length; i++) {
            copie[i] = new int[tableau[i].length];
            for (int j = 0; j < tableau[i].length; j++)
                copie[i][j] = tableau[i][j];
        }
        return copie;
    }

    // Méthode pour obtenir le maximum entre deux nombres
    public static double getMax(double a, double b) {
        return (a > b) ? a : b;
    }

    // Méthode pour obtenir le minimum entre deux nombres
    public static double getMin(double a, double b) {
        return (a < b) ? a : b;
    }

    // Méthode pour afficher un texte si autorisé
    public static void afficher(boolean autorise, String texte) {
        if (autorise) {
            System.out.println(texte);
        }
    }

    // Calcul du nombre de jetons alignés en ligne (4 jetons max considérés)
    public static void calculerJetonsAlignesLigne(Plateau plateau, int jetonJoueur, int[] compteJetonsAlignes) {
        int nbColonnes = plateau.getNbColonnes();
        int nbLignes = plateau.getNbLignes();
        int[][] grille = plateau.getGrille();
        int[] compteLigne;

        for (int i = 0; i < nbLignes; i++) {
            for (int j = 0; j < nbColonnes - 3; j++) { // -3 car on regarde sur 4 positions
                compteLigne = new int[]{0, 0}; // [nombre de jetons joueur, nombre de cases vides]
                for (int k = 0; k < 4; k++) {
                    if (grille[i][j + k] == jetonJoueur) {
                        compteLigne[0]++;
                    } else if (grille[i][j + k] == 0) {
                        compteLigne[1]++;
                    } else {
                        break; // jeton adverse, on arrête de compter cette séquence
                    }
                }
                if (compteLigne[0] + compteLigne[1] == 4) {
                    compteJetonsAlignes[compteLigne[0]]++;
                }
            }
        }
    }

    // Calcul du nombre de jetons alignés en colonne
    public static void calculerJetonsAlignesColonne(Plateau plateau, int jetonJoueur, int[] compteJetonsAlignes) {
        int nbColonnes = plateau.getNbColonnes();
        int nbLignes = plateau.getNbLignes();
        int[][] grille = plateau.getGrille();
        int[] compteColonne;

        for (int j = 0; j < nbColonnes; j++) {
            for (int i = 0; i < nbLignes - 3; i++) { // -3 car on regarde 4 positions verticales
                compteColonne = new int[]{0, 0};
                for (int k = 0; k < 4; k++) {
                    if (grille[i + k][j] == jetonJoueur) {
                        compteColonne[0]++;
                    } else if (grille[i + k][j] == 0) {
                        compteColonne[1]++;
                    } else {
                        break;
                    }
                }
                if (compteColonne[0] + compteColonne[1] == 4) {
                    compteJetonsAlignes[compteColonne[0]]++;
                }
            }
        }
    }

    // Calcul du nombre de jetons alignés sur les diagonales
    public static void calculerJetonsAlignesDiagonale(Plateau plateau, int jetonJoueur, int[] compteJetonsAlignes) {
        int nbColonnes = plateau.getNbColonnes();
        int nbLignes = plateau.getNbLignes();
        int[][] grille = plateau.getGrille();
        int[] compteDiagonale1;
        int[] compteDiagonale2;

        for (int i = 0; i < nbLignes - 3; i++) {
            for (int j = 0; j < nbColonnes; j++) {
                compteDiagonale1 = new int[]{0, 0};
                compteDiagonale2 = new int[]{0, 0};

                for (int k = 0; k < 4; k++) {
                    // Diagonale descendante vers la droite
                    if (j <= nbColonnes - 4) {
                        if (grille[i + k][j + k] == jetonJoueur)
                            compteDiagonale1[0]++;
                        else if (grille[i + k][j + k] == 0)
                            compteDiagonale1[1]++;
                        else
                            break;
                    }
                    // Diagonale descendante vers la gauche
                    if (j >= 3) {
                        if (grille[i + k][j - k] == jetonJoueur)
                            compteDiagonale2[0]++;
                        else if (grille[i + k][j - k] == 0)
                            compteDiagonale2[1]++;
                        else
                            break;
                    }
                }

                if (compteDiagonale1[0] + compteDiagonale1[1] == 4) {
                    compteJetonsAlignes[compteDiagonale1[0]]++;
                }
                if (compteDiagonale2[0] + compteDiagonale2[1] == 4) {
                    compteJetonsAlignes[compteDiagonale2[0]]++;
                }
            }
        }
    }
}
