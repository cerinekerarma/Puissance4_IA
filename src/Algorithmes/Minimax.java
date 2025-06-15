package Algorithmes;

import Puissance4.*;

public class Minimax {
    protected Algorithmes algo;
    protected int tokenJoueurMax;
    protected int tokenJoueurMin;
    private static int noeudsCrees = 0; // Compteur statique pour tous les Minimax

    public Minimax(int tokenJoueurMax, int tokenJoueurMin) {
        this.tokenJoueurMax = tokenJoueurMax;
        this.tokenJoueurMin = tokenJoueurMin;
        this.algo = Algorithmes.MINIMAX;
    }

    public static void resetCompteurNoeuds() {
        noeudsCrees = 0;
    }

    // Le nombre total de nœuds créés lors de la dernière recherche
    public static int getNoeudsCrees() {
        return noeudsCrees;
    }

    public int minimax(Noeud racine, int profondeurMax) {
        resetCompteurNoeuds(); // Réinitialise avant chaque nouvelle recherche
        return JoueurMax(racine, profondeurMax).getA();
    }

    public ActionValeur JoueurMax(Noeud n, int p) {
        return JoueurMaxBase(n, p, null, null);
    }

    public ActionValeur JoueurMin(Noeud n, int p) {
        return JoueurMinBase(n, p, null, null);
    }

    protected ActionValeur JoueurMaxBase(Noeud n, int p, Double alpha, Double beta) {
        noeudsCrees++;

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
                alpha = Puissance4Logique.getMax(alpha, u);
            }
        }

        return new ActionValeur(u, a);
    }

    protected ActionValeur JoueurMinBase(Noeud n, int p, Double alpha, Double beta) {
        noeudsCrees++;

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
                beta = Puissance4Logique.getMin(beta, u);
            }
        }

        return new ActionValeur(u, a);
    }
}