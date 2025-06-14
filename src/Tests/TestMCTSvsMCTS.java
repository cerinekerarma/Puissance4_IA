package Tests;

import Puissance4.*;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TestMCTSvsMCTS {
    public static void main(String[] args) {
        int[] iterationsList = {100, 500, 1000};  // Nombre d'itérations
        double[] constantesC = {0.5, 1.0, Math.sqrt(2), 2.0};  // Constantes d'exploration
        int[] simulationsList = {1, 5, 10};  // Nombre de simulations par rollout

        StringBuilder resultats = new StringBuilder();
        String fichierResultats = "resultats/resultats_tests_mcts_vs_mcts.txt";

        try (FileWriter writer = new FileWriter(fichierResultats)) {
            writer.write("Résultats des tests MCTS vs MCTS\n\n");
            writer.write("Format : IA 1 (iter=x, C=x.xx, sim=x) vs IA 2 (iter=x, C=x.xx, sim=x)\n\n");

            int numPartie = 1;

            for (int iter1 : iterationsList) {
                for (double c1 : constantesC) {
                    for (int sim1 : simulationsList) {
                        for (int iter2 : iterationsList) {
                            for (double c2 : constantesC) {
                                for (int sim2 : simulationsList) {

                                    // Jouer la partie
                                    int resultat = jouerPartieMCTSvsMCTS(iter1, c1, sim1, iter2, c2, sim2);

                                    String resultatPartie = String.format(
                                            "Partie %d\nIA 1 (iter=%d, C=%.2f, sim=%d) vs IA 2 (iter=%d, C=%.2f, sim=%d) : %s\n\n",
                                            numPartie++, iter1, c1, sim1, iter2, c2, sim2, formaterResultat(resultat)
                                    );

                                    writer.write(resultatPartie);
                                    System.out.print(resultatPartie); // Affiche la progression
                                }
                            }
                        }
                    }
                }
            }

            System.out.println("Tests complétés. Résultats dans " + fichierResultats);
        } catch (IOException e) {
            System.err.println("Erreur fichier: " + e.getMessage());
        }
    }

    private static int jouerPartieMCTSvsMCTS(int iter1, double c1, int sim1, int iter2, double c2, int sim2) {
        Jeu jeu = Jeu.initialiserJeu();
        List<Joueur> joueurs = new ArrayList<>();

        Joueur ia1 = new Joueur(Algorithmes.MCTS, iter1);
        ia1.setConstanteCMCTS(c1);
        ia1.setNbSimulationsMCTS(sim1);

        Joueur ia2 = new Joueur(Algorithmes.MCTS, iter2);
        ia2.setConstanteCMCTS(c2);
        ia2.setNbSimulationsMCTS(sim2);

        joueurs.add(ia1);
        joueurs.add(ia2);

        return demarrerPartieControlee(jeu, joueurs);
    }

    private static int demarrerPartieControlee(Jeu jeu, List<Joueur> joueurs) {
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

    private static String formaterResultat(int resultat) {
        return switch (resultat) {
            case 1 -> "IA 1 vainqueur";
            case 2 -> "IA 2 vainqueur";
            default -> "Match nul";
        };
    }
}
