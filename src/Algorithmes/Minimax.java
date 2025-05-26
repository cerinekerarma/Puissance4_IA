package Algorithmes;

import Puissance4.*;

public class Minimax {
    protected Algorithmes algo; // Type d'algorithme utilisé (MINIMAX ou ALPHA_BETA)
    protected int tokenJoueurMax; // Token du joueur MAX
    protected int tokenJoueurMin; // Token du joueur MIN

    // Constructeur initialisant les paramètres du Minimax
    public Minimax(int tokenJoueurMax, int tokenJoueurMin) {
        this.tokenJoueurMax = tokenJoueurMax;
        this.tokenJoueurMin = tokenJoueurMin;
        this.algo = Algorithmes.MINIMAX; // Algorithme par défaut
    }

    /**
     * Lance l'algorithme Minimax à partir du nœud racine.
     *
     * @param racine      Nœud racine de l'arbre de recherche
     * @param profondeurMax  Profondeur maximale de recherche
     * @return Le meilleur coup trouvé
     */
    public int minimax(Noeud racine, int profondeurMax) {
        return JoueurMax(racine, profondeurMax).getA();
    }

    public ActionValeur JoueurMax(Noeud n, int p) {
        return JoueurMaxBase(n, p, null, null);
    }

    public ActionValeur JoueurMin(Noeud n, int p) {
        return JoueurMinBase(n, p, null, null);
    }

    protected ActionValeur JoueurMaxBase(Noeud n, int p, Double alpha, Double beta) {
        if (n.estFeuille() || p == 0) {
            return new ActionValeur((double) n.getPlateau().evaluation(tokenJoueurMax, tokenJoueurMin), null);
        }

        Double u = Double.NEGATIVE_INFINITY;
        Integer a = null;

        n.creerTousFils(this.tokenJoueurMax);
        for (Noeud f : n.getFils()) {
            double eval = this.JoueurMinBase(f, p - 1, alpha, beta).getU();
            if (eval > u) {
                u = eval;
                a = f.getDernierCoup();
            }

            if (algo == Algorithmes.ALPHA_BETA) {
                if (u >= beta) {
                    return new ActionValeur(u, a);
                }
                alpha = Puissance4_algos.getMax(alpha, u);
            }
        }

        return new ActionValeur(u, a);
    }

    protected ActionValeur JoueurMinBase(Noeud n, int p, Double alpha, Double beta) {
        if (n.estFeuille() || p == 0) {
            return new ActionValeur((double) n.getPlateau().evaluation(tokenJoueurMax, tokenJoueurMin), null);
        }

        Double u = Double.POSITIVE_INFINITY;
        Integer a = null;

        n.creerTousFils(this.tokenJoueurMin);
        for (Noeud f : n.getFils()) {
            double eval = this.JoueurMaxBase(f, p - 1, alpha, beta).getU();
            if (eval < u) {
                u = eval;
                a = f.getDernierCoup();
            }

            if (algo == Algorithmes.ALPHA_BETA) {
                if (u <= alpha) {
                    return new ActionValeur(u, a);
                }
                beta = Puissance4_algos.getMin(beta, u);
            }
        }

        return new ActionValeur(u, a);
    }

}

