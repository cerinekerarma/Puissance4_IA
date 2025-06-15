package Tests;

import Algorithmes.MCTS;
import Puissance4.Plateau;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class TestMCTS {
    public static void main(String[] args) {
        // Paramètres à tester
        int[] iterationsList = {100, 500, 1000};
        double[] constantesC = {1, 10, 20, 30};
        int[] simulationsList = {1, 5, 10};

        String fichierResultats = "resultats/analyse_arbres_mcts.txt";

        try (FileWriter writer = new FileWriter(fichierResultats)) {
            writer.write("Analyse des arbres MCTS en fonction des paramètres\n\n");
            writer.write("Format: itérations=X, C=Y.YY, simulations=Z | Profondeur | Nœuds par niveau | Branche max\n");
            writer.write("--------------------------------------------------------\n\n");

            Plateau plateauInitial = new Plateau(6,7,1,null); // Plateau vide de départ

            for (int iterations : iterationsList) {
                for (double c : constantesC) {
                    for (int simulations : simulationsList) {
                        MCTS mcts = new MCTS(1, 2); // Joueur 1 vs Joueur 2
                        mcts.definirConstanteC(c);
                        mcts.definirNbSimulations(simulations);

                        // Exécution de MCTS
                        mcts.executer(plateauInitial, iterations);

                        // Récupération des stats
                        int profondeur = mcts.getMaxProfondeurArbre();
                        List<Integer> noeudsParNiveau = mcts.getNoeudsParNiveau();
                        int brancheMax = mcts.getTaillePlusGrandeBranche();

                        // Formatage des résultats
                        String ligneResultat = String.format(
                                "itérations=%d, C=%.2f, simulations=%d | Profondeur: %d | Nœuds/niveau: %s | Branche max: %d\n",
                                iterations, c, simulations, profondeur, noeudsParNiveau, brancheMax
                        );

                        writer.write(ligneResultat);
                        System.out.print(ligneResultat);
                    }
                }
            }

            System.out.println("\nAnalyse terminée. Résultats sauvegardés dans: " + fichierResultats);
        } catch (IOException e) {
            System.err.println("Erreur d'écriture fichier: " + e.getMessage());
        }
    }
}