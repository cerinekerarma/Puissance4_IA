package Algorithmes;

import Puissance4.*;
import java.util.List;

public class MCTS {

    private int jetonJoueur;
    private int jetonAdversaire;
    private int nbSimulations;
    private int nbIterations;
    private double constanteC;
    private Noeud dernierNoeudConstruit;

    // Constructeur initialisant les paramètres de MCTS
    public MCTS(int jetonJoueur, int jetonAdversaire) {
        this.jetonJoueur = jetonJoueur;
        this.jetonAdversaire = jetonAdversaire;
        this.constanteC = Math.sqrt(2); // Constante C par défaut
        this.dernierNoeudConstruit = null;
        this.nbSimulations = 1; // Nombre de simulations par défaut
    }

    // Méthode pour définir le nombre de simulations
    public void definirNbSimulations(int nbSimulations) {
        this.nbSimulations = nbSimulations;
    }

    // Méthode pour définir la constante C
    public void definirConstanteC(double constanteC) {
        this.constanteC = constanteC;
    }

    // Méthode pour obtenir le dernier noeud construit
    public Noeud obtenirDernierNoeudConstruit() {
        return dernierNoeudConstruit;
    }

    // Méthode principale d'exécution de MCTS
    public int executer(Plateau plateau, int nbIterations) {
        this.nbIterations = nbIterations;
        return rechercheUCT(plateau);
    }

    // Recherche UCT pour trouver le meilleur coup
    private int rechercheUCT(Plateau plateau) {
        Noeud noeudRacine = Noeud.creerNoeudRacine(plateau.clone());
        this.dernierNoeudConstruit = noeudRacine;

        Noeud noeudSelectionne;
        double delta;
        for (int i = 0; i < nbIterations; i++) {
            noeudSelectionne = politiqueArbre(noeudRacine);
            delta = politiqueDefaut(noeudSelectionne.getPlateau());
            sauvegarder(noeudSelectionne, delta);
        }

        return meilleurEnfant(noeudRacine, 0).getDernierCoup();
    }

    // Politique d'arbre pour sélectionner un noeud
    private Noeud politiqueArbre(Noeud noeud) {
        while (!noeud.getPlateau().getEtat().estTermine()) {
            if (!noeud.toutesActionsRealisees()) {
                return expansion(noeud);
            } else {
                noeud = meilleurEnfant(noeud, constanteC);
            }
        }
        return noeud;
    }

    // Expansion d'un noeud
    private Noeud expansion(Noeud noeud) {
        int action = noeud.getUneActionNonTentee();
        return noeud.creerFils(action, noeud.getPlateau().getJetonAdverse());
    }

    // Sélection du meilleur noeud enfant
    private Noeud meilleurEnfant(Noeud noeud, double c) {
        double max = Double.NEGATIVE_INFINITY;
        Noeud argMax = null;
        int visitesParent = noeud.getNbVisites();
        double n;
        double q;
        double formule;
        List<Noeud> enfants = noeud.getFils();
        for (Noeud enfant : enfants) {
            q = enfant.getNbVictoires();
            n = enfant.getNbVisites();
            formule = (q / n) + c * Math.sqrt(2 * Math.log(visitesParent) / n);
            if (formule >= max) {
                max = formule;
                argMax = enfant;
            }
        }
        return argMax;
    }

    // Politique par défaut pour la simulation
    private double politiqueDefaut(Plateau plateau) {
        if (plateau.getEtat().estTermine()) {
            return recompense(plateau.getVainqueur());
        }
        Plateau copiePlateau;
        int joueurCourant;
        int adversaireCourant;
        joueurCourant = plateau.getDernierJoueur();
        adversaireCourant = plateau.getJetonAdverse();
        double valeur = 0;
        for (int i = 0; i < nbSimulations; i++) {
            copiePlateau = plateau.clone();
            copiePlateau.ajouterJetonAleatoire(new int[]{joueurCourant, adversaireCourant});
            valeur += recompense(copiePlateau.getVainqueur()) / (float) nbSimulations;
        }
        return valeur;
    }

    // Mise à jour des récompenses des noeuds
    private void sauvegarder(Noeud noeud, Double deltaRecompense) {
        while (noeud != null) {
            noeud.setNbVisites(noeud.getNbVisites() + 1);
            noeud.setNbVictoires(noeud.getNbVictoires() + inverserRecompense(noeud, deltaRecompense));
            noeud = noeud.getParent();
        }
    }

    // Inversion de la récompense selon la profondeur dans l'arbre
    private double inverserRecompense(Noeud noeud, double recompense) {
        if (noeud.getProfondeur() % 2 == 0) {
            return -recompense;
        }
        return recompense;
    }

    // Attribution de la récompense en fonction du gagnant
    private double recompense(int gagnant) {
        if (gagnant == this.jetonJoueur) {
            return 1;
        } else if (gagnant == this.jetonAdversaire) {
            return -1;
        }
        return 0;
    }
}
