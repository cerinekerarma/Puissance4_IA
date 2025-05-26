package Tests;

import Puissance4.*;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TestMinimaxVSMCTS {
    // Configuration mémoire si nécessaire : -Xmx4G
    public static void main(String[] args) {
        // Profondeurs à tester pour Minimax
        int[] profondeursMinimax = {3, 5, 7, 9, 10};
        int[] iterationsMCTS = {5, 10, 15, 20};
        StringBuilder resultats = new StringBuilder();

        // Fichier de sortie
        String fichierResultats = "resultats_tests_minimax_vs_mcts.txt";

        try (FileWriter writer = new FileWriter(fichierResultats)) {
            writer.write("Résultats des tests Minimax vs MCTS\n\n");
            writer.write("Format : Minimax(profondeur) vs MCTS(itérations)\n\n");

            int numPartie = 1;
            // Tests pour chaque combinaison
            for (int profondeurMinimax : profondeursMinimax) {
                for (int iterations : iterationsMCTS) {
                    // Jouer la partie
                    int resultat = jouerPartieMinimaxVsMCTS(profondeurMinimax, iterations);

                    // Enregistrer le résultat
                    String resultatPartie = String.format(
                            "Partie %d\nMinimax (profondeur=%d) vs MCTS (iterations=%d) : %s\n\n",
                            numPartie++,
                            profondeurMinimax,
                            iterations,
                            formaterResultat(resultat)
                    );

                    writer.write(resultatPartie);
                    System.out.print(resultatPartie); // Affiche progression
                }
            }

            System.out.println("Tests complétés. Résultats dans " + fichierResultats);
        } catch (IOException e) {
            System.err.println("Erreur fichier: " + e.getMessage());
        }
    }

    private static int jouerPartieMinimaxVsMCTS(int profondeurMinimax, int iterationsMCTS) {
        Jeu jeu = Jeu.initialiserJeu();
        List<Joueur> joueurs = new ArrayList<>();

        // Minimax (Joueur 1) vs MCTS (Joueur 2)
        joueurs.add(new Joueur(Algorithmes.MINIMAX, profondeurMinimax));
        joueurs.add(new Joueur(Algorithmes.MCTS, iterationsMCTS));

        return demarrerPartieControlee(jeu, joueurs);
    }

    private static int demarrerPartieControlee(Jeu jeu, List<Joueur> joueurs) {
        int jetonActuel = 1;
        int tours = 0;
        final int MAX_TOURS = 100; // Limite de tours

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
        switch (resultat) {
            case 1: return "Minimax vainqueur";
            case 2: return "MCTS vainqueur";
            default: return "Match nul";
        }
    }
}