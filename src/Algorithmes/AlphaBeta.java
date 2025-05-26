package Algorithmes;

import Puissance4.*;

public class AlphaBeta extends Minimax{

    /**
     * Constructeur initialisant l'algorithme Alpha-Bêta avec les jetons des joueurs.
     *
     * @param tokenJoueurMax Jeton du joueur maximisant
     * @param tokenJoueurMin Jeton du joueur minimisant
     */
    // Constructeur initialisant l'algorithme à Alpha-Bêta
    public AlphaBeta(int tokenJoueurMax, int tokenJoueurMin) {
        super(tokenJoueurMax, tokenJoueurMin); // Appel du constructeur de la classe parent
        this.algo = Algorithmes.ALPHA_BETA; // Définition de l'algorithme comme Alpha-Bêta
    }

    /**
     * Lance l'algorithme Alpha-Bêta à partir du nœud racine.
     *
     * @param root      Nœud racine de l'arbre de recherche
     * @param maxDepth  Profondeur maximale de recherche
     * @return Le meilleur coup trouvé (colonne à jouer)
     */
    public int alphaBeta(Noeud root, int maxDepth) {
        return JoueurMax(root, maxDepth, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY).getA();
    }

    public ActionValeur JoueurMax(Noeud n, int p, Double alpha, Double beta) {
        return JoueurMaxBase(n, p, alpha, beta);
    }

    public ActionValeur JoueurMin(Noeud n, int p, Double alpha, Double beta) {
        return JoueurMinBase(n, p, alpha, beta);
    }

}
