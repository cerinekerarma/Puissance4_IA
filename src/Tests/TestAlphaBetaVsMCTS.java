package Tests;

import Puissance4.*;
import Algorithmes.*;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TestAlphaBetaVsMCTS {
    public static void main(String[] args) {
        int profondeurAlphaBeta = 6;
        int iterationsMCTS = 1000;
        int simulationsMCTS = 5;
        double[] constantesC = {0.5, 2.0, 20.0};
        int nbParties = 100;
        String fichierResultats = "resultats/resultats_alphabeta_vs_mcts.txt";

        try (FileWriter writer = new FileWriter(fichierResultats)) {
            writer.write("Résultats AlphaBeta vs MCTS\n\n");
            writer.write("Paramètres:\n");
            writer.write("- AlphaBeta profondeur: " + profondeurAlphaBeta + "\n");
            writer.write("- MCTS: " + iterationsMCTS + " itérations, " + simulationsMCTS + " simulations\n\n");

            for (double c : constantesC) {
                writer.write("--- Tests avec C=" + c + " ---\n");
                int victoiresAlphaBeta = 0;
                int victoiresMCTS = 0;
                int matchsNuls = 0;

                for (int i = 1; i <= nbParties; i++) {
                    if (i % 100 == 0) {
                        System.out.printf("Partie %d/%d (C=%.1f)\n", i, nbParties, c);
                    }

                    Jeu jeu = Jeu.initialiserJeu();
                    List<Joueur> joueurs = new ArrayList<>();

                    // AlphaBeta (Joueur 1)
                    Joueur alphabeta = new Joueur(Algorithmes.ALPHA_BETA, profondeurAlphaBeta);

                    // MCTS (Joueur 2)
                    Joueur mcts = new Joueur(Algorithmes.MCTS, iterationsMCTS);
                    mcts.setConstanteCMCTS(c);
                    mcts.setNbSimulationsMCTS(simulationsMCTS);

                    joueurs.add(alphabeta);
                    joueurs.add(mcts);

                    int resultat = demarrerPartie(jeu, joueurs);

                    if (resultat == 1) victoiresAlphaBeta++;
                    else if (resultat == 2) victoiresMCTS++;
                    else matchsNuls++;
                }

                // Calcul des pourcentages
                double pourcentageAlphaBeta = (victoiresAlphaBeta * 100.0) / nbParties;
                double pourcentageMCTS = (victoiresMCTS * 100.0) / nbParties;
                double pourcentageNuls = (matchsNuls * 100.0) / nbParties;

                writer.write(String.format(
                        "Résultats: AlphaBeta %.1f%% | MCTS %.1f%% | Nuls %.1f%%\n\n",
                        pourcentageAlphaBeta, pourcentageMCTS, pourcentageNuls
                ));
            }
            System.out.println("Tests complétés. Résultats dans " + fichierResultats);
        } catch (IOException e) {
            System.err.println("Erreur fichier: " + e.getMessage());
        }
    }

    private static int demarrerPartie(Jeu jeu, List<Joueur> joueurs) {
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
        return 0;
    }
}