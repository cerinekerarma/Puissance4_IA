package Tests;

import Puissance4.*;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TestAlphaBetaVSMinimax {
    // Nombre de rounds par confrontation
    private static final int NB_ROUNDS = 1000;
    // Profondeurs max à tester
    private static final int PROFONDEUR_MAX = 6;

    public static void main(String[] args) {
        StringBuilder resultats = new StringBuilder();
        String fichierResultats = "resultats/resultats_tests_alphabeta_vs_minimax.txt";

        try (FileWriter writer = new FileWriter(fichierResultats)) {
            writer.write("Résultats des tests AlphaBeta vs Minimax (sur " + NB_ROUNDS + " rounds)\n\n");

            // Tests pour chaque profondeur de 1 à PROFONDEUR_MAX
            for (int profondeur = 1; profondeur <= PROFONDEUR_MAX; profondeur++) {
                // Statistiques pour cette profondeur
                int victoiresAlphaBeta = 0;
                int victoiresMinimax = 0;
                int matchsNuls = 0;

                writer.write("=== Profondeur " + profondeur + " ===\n");

                for (int round = 1; round <= NB_ROUNDS; round++) {
                    // Jouer une partie AlphaBeta vs Minimax à la même profondeur
                    int resultat = jouerPartieAlphaBetaVsMinimax(profondeur, profondeur);

                    // Mettre à jour les stats
                    if (resultat == 1) victoiresAlphaBeta++;
                    else if (resultat == 2) victoiresMinimax++;
                    else matchsNuls++;

                    // Afficher progression tous les 100 rounds
                    if (round % 100 == 0) {
                        System.out.printf("Profondeur %d - Round %d/%d\n",
                                profondeur, round, NB_ROUNDS);
                    }
                }

                // Calcul des pourcentages
                double pourcentageAlphaBeta = (victoiresAlphaBeta * 100.0) / NB_ROUNDS;
                double pourcentageMinimax = (victoiresMinimax * 100.0) / NB_ROUNDS;
                double pourcentageNuls = (matchsNuls * 100.0) / NB_ROUNDS;

                // Enregistrer les résultats pour cette profondeur
                String resultatProfondeur = String.format(
                        "AlphaBeta: %.1f%% victoires | Minimax: %.1f%% victoires | Nuls: %.1f%%\n\n",
                        pourcentageAlphaBeta, pourcentageMinimax, pourcentageNuls
                );

                writer.write(resultatProfondeur);
                System.out.print(resultatProfondeur);
            }

            System.out.println("Tests complétés. Résultats dans " + fichierResultats);
        } catch (IOException e) {
            System.err.println("Erreur fichier: " + e.getMessage());
        }
    }

    private static int jouerPartieAlphaBetaVsMinimax(int profondeurAlphaBeta, int profondeurMinimax) {
        Jeu jeu = Jeu.initialiserJeu();
        List<Joueur> joueurs = new ArrayList<>();

        // AlphaBeta (Joueur 1) vs Minimax (Joueur 2)
        joueurs.add(new Joueur(Algorithmes.ALPHA_BETA, profondeurAlphaBeta));
        joueurs.add(new Joueur(Algorithmes.MINIMAX, profondeurMinimax));

        return demarrerPartieSansInterface(jeu, joueurs);
    }

    private static int demarrerPartieSansInterface(Jeu jeu, List<Joueur> joueurs) {
        int jetonActuel = 1;
        int tours = 0;
        final int MAX_TOURS = 100;

        while (tours++ < MAX_TOURS) {
            int jetonAdversaire = 3 - jetonActuel;
            Joueur joueur = joueurs.get(jetonActuel - 1);

            boolean fin = jeu.jouerIA(joueur, jetonActuel, jetonAdversaire, false);
            if (fin) {
                return jeu.getPlateau().getVainqueur();
            }

            jetonActuel = 3 - jetonActuel;
        }
        return 0; // Match nul
    }
}