package Algorithmes;

import Puissance4.*;
import java.util.ArrayList;
import java.util.List;

public class MCTS {

    private int jetonJoueur;
    private int jetonAdversaire;
    private int nbSimulations;
    private int nbIterations;
    private double constanteC;
    private Noeud dernierNoeudConstruit;
    private int maxProfondeurArbre;
    private List<Integer> noeudsParNiveau;
    private int taillePlusGrandeBranche;

    public MCTS(int jetonJoueur, int jetonAdversaire) {
        this.jetonJoueur = jetonJoueur;
        this.jetonAdversaire = jetonAdversaire;
        this.constanteC = Math.sqrt(2); // Constante C par défaut
        this.dernierNoeudConstruit = null;
        this.nbSimulations = 10; // Nombre de simulations par défaut
        this.maxProfondeurArbre = 0;
        this.noeudsParNiveau = new ArrayList<>();
        this.taillePlusGrandeBranche = 0;
    }
    public int getMaxProfondeurArbre() {
        return this.maxProfondeurArbre;
    }

    public List<Integer> getNoeudsParNiveau() {
        return new ArrayList<>(this.noeudsParNiveau);
    }

    public int getTaillePlusGrandeBranche() {
        return this.taillePlusGrandeBranche;
    }

    private void analyserArbre(Noeud racine) {
        maxProfondeurArbre = 0;
        noeudsParNiveau.clear();
        taillePlusGrandeBranche = 0;

        if (racine != null) {
            analyserNoeud(racine, 0);
        }
    }

    private void analyserNoeud(Noeud noeud, int niveau) {
        // Ignorer les nœuds non visités
        if (noeud.getNbVisites() < 2) return;

        // Mise à jour de la profondeur max
        if (niveau > maxProfondeurArbre) {
            maxProfondeurArbre = niveau;
        }

        // Mise à jour du comptage par niveau
        while (noeudsParNiveau.size() <= niveau) {
            noeudsParNiveau.add(0);
        }
        noeudsParNiveau.set(niveau, noeudsParNiveau.get(niveau) + 1);

        // Analyse des enfants visités
        List<Noeud> enfantsVisites = noeud.getFils().stream()
                .filter(e -> e.getNbVisites() > 0)
                .toList();

        int nbEnfants = enfantsVisites.size();
        if (nbEnfants > taillePlusGrandeBranche) {
            taillePlusGrandeBranche = nbEnfants;
        }

        for (Noeud enfant : enfantsVisites) {
            analyserNoeud(enfant, niveau + 1);
        }
    }

    public void definirNbSimulations(int nbSimulations) {
        this.nbSimulations = nbSimulations;
    }

    public void definirConstanteC(double constanteC) {
        this.constanteC = constanteC;
    }

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

        for (int i = 0; i < nbIterations; i++) {
            if (i % 10 == 0) { // Log toutes les 10 itérations
                //System.out.printf("Iteration %d/%d (C=%.1f)%n",
                      //  i+1, nbIterations, constanteC);
            }

            Noeud noeudSelectionne = politiqueArbre(noeudRacine);
            double delta = politiqueDefaut(noeudSelectionne.getPlateau());
            sauvegarder(noeudSelectionne, delta);
        }

        analyserArbre(noeudRacine);
        return meilleurEnfant(noeudRacine, this.constanteC).getDernierCoup();
    }

    // Politique d'arbre pour sélectionner un noeud
    private Noeud politiqueArbre(Noeud noeud) {
        while (!noeud.getPlateau().getEtat().estTermine()) {
            if (!noeud.toutesActionsRealisees()) {
                Noeud expanded = expansion(noeud);
                if (expanded == null) break; // Plus d'actions valides
                return expanded;
            }
            noeud = meilleurEnfant(noeud, constanteC);
            if (noeud == null) break;
        }
        return noeud;
    }

    // Expansion d'un noeud
    private Noeud expansion(Noeud noeud) {
        // Seulement si pas toutes actions tentées ET avec probabilité dépendant de C
        if (noeud.toutesActionsRealisees() || Math.random() > (1.0/(constanteC+1))) {
            return null;
        }

        int action = noeud.getUneActionNonTentee();
        if (action == -1) return null;

        return noeud.creerFils(action, noeud.getPlateau().getJetonAdverse());
    }

    // Sélection du meilleur noeud enfant
    private Noeud meilleurEnfant(Noeud noeud, double c) {
        double maxScore = Double.NEGATIVE_INFINITY;
        Noeud meilleur = null;
        double logVisits = Math.log(noeud.getNbVisites() + 1e-6);

        for (Noeud enfant : noeud.getFils()) {
            if (enfant.getNbVisites() == 0) {
                return enfant; // Exploration prioritaire des nœuds non visités
            }

            double exploitation = enfant.getNbVictoires() / enfant.getNbVisites();
            double exploration = c * Math.sqrt(logVisits / enfant.getNbVisites());
            double score = exploitation + exploration;

            if (score > maxScore || (score == maxScore && Math.random() > 0.5)) {
                maxScore = score;
                meilleur = enfant;
            }
        }
        return meilleur;
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
            while (!copiePlateau.getEtat().estTermine()) {
                copiePlateau.ajouterJetonAleatoire(new int[]{joueurCourant, adversaireCourant});
            }
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

    public String getArbreStats() {
        return String.format(
                "Arbre MCTS - Profondeur: %d | Nœuds par niveau: %s | Plus grande branche: %d",
                maxProfondeurArbre,
                noeudsParNiveau.toString(),
                taillePlusGrandeBranche
        );
    }

}
