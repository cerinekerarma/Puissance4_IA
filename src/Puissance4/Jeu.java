package Puissance4;

import Algorithmes.*;
import java.util.List;
import java.util.Random;

public class Jeu {

    private Plateau plateau;

    public Jeu(Plateau plateau) {
        this.plateau = plateau;
    }

    public Plateau getPlateau() {
        return plateau;
    }

    // Initialisation avec un plateau par défaut
    public static Jeu initialiserJeu() {
        Plateau plateauParDefaut = new Plateau(6, 7, 1, null);
        return new Jeu(plateauParDefaut);
    }

    public void afficherEtatJeu() {
        this.plateau.afficherGrille();
    }

    public List<Integer> getColonnesValides() {
        return this.plateau.colonnesValides();
    }

    public boolean colonneValide(int colonne) {
        return this.plateau.colonneValide(colonne);
    }

    // Joue un jeton dans une colonne, retourne true si la partie est terminée après ce coup
    public boolean jouerDansColonne(int colonne, int jeton) {
        int ligne = this.plateau.ajouterJetonDansColonne(colonne, jeton);
        return this.plateau.getEtat().estTermine();
    }

    // Joue un coup pour une IA donnée
    public boolean jouerIA(Joueur joueur, int jeton, int jetonAdverse, boolean afficher) {
        String infoAffichage = "";
        Noeud noeudRacine = null;
        int action = -1;

        Algorithmes algoIA = joueur.getAlgorithme();
        int paramIA = joueur.getParametre();

        if (algoIA == Algorithmes.RANDOM) {
            Random r = new Random();
            List<Integer> colonnesValides = getColonnesValides();
            action = colonnesValides.get(r.nextInt(colonnesValides.size()));
        } else if (algoIA == Algorithmes.MINIMAX) {
            noeudRacine = Noeud.creerNoeudRacine(this.plateau.clone());
            action = new Minimax(jeton, jetonAdverse).minimax(noeudRacine, paramIA);
            infoAffichage = ", p = " + paramIA;
        } else if (algoIA == Algorithmes.ALPHA_BETA) {
            noeudRacine = Noeud.creerNoeudRacine(this.plateau.clone());
            action = new AlphaBeta(jeton, jetonAdverse).alphaBeta(noeudRacine, paramIA);
            infoAffichage = ", p = " + paramIA;
        } else if (algoIA == Algorithmes.MCTS) {
            Plateau nouveauPlateau = this.plateau.clone();
            MCTS mcts = new MCTS(jeton, jetonAdverse);
            if (joueur.getConstanteCMCTS() != null) {
                mcts.definirConstanteC(joueur.getConstanteCMCTS());
            }
            if (joueur.getNbSimulationsMCTS() != null) {
                mcts.definirNbSimulations(joueur.getNbSimulationsMCTS());
            }
            action = mcts.executer(nouveauPlateau, paramIA);
        }

        if (afficher) {
            System.out.println("IA " + jeton + " (" + algoIA.name() + infoAffichage + ") joue en " + action);
        }

        return jouerDansColonne(action, jeton);
    }
}
