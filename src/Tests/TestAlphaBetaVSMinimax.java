package Tests;

import Puissance4.*;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TestAlphaBetaVSMinimax {
    // Si vous avez un OutOfMemoryError, augmentez la RAM :
    // Run > Edit Configurations > VM options : -Xmx4G
    public static void main(String[] args) {
        // Profondeurs à tester
        int[] profondeurs = {3, 5, 7, 9, 10};
        StringBuilder resultats = new StringBuilder();

        // Fichier de sortie
        String fichierResultats = "resultats_tests_alphabeta_vs_minimax.txt";

        try (FileWriter writer = new FileWriter(fichierResultats)) {
            // En-tête du fichier
            writer.write("Résultats des tests AlphaBeta vs Minimax\n\n");

            // Tests pour chaque combinaison de profondeurs
            for (int i = 0; i < profondeurs.length; i++) {
                for (int j = 0; j < profondeurs.length; j++) {
                    int profondeurAlphaBeta = profondeurs[i];
                    int profondeurMinimax = profondeurs[j];

                    // Jouer la partie AlphaBeta vs Minimax
                    int resultat = jouerPartieAlphaBetaVsMinimax(profondeurAlphaBeta, profondeurMinimax);

                    // Enregistrer le résultat
                    String resultatPartie = String.format(
                            "Partie %d\nAlphaBeta (profondeur=%d) vs Minimax (profondeur=%d) : %s\n\n",
                            (i * profondeurs.length) + j + 1,
                            profondeurAlphaBeta,
                            profondeurMinimax,
                            formaterResultat(resultat)
                    );

                    writer.write(resultatPartie);
                    resultats.append(resultatPartie);

                    // Afficher progression dans la console
                    System.out.println(resultatPartie);
                }
            }

            System.out.println("Tous les tests ont été complétés. Résultats enregistrés dans " + fichierResultats);
        } catch (IOException e) {
            System.err.println("Erreur lors de l'écriture du fichier : " + e.getMessage());
        }
    }

    private static int jouerPartieAlphaBetaVsMinimax(int profondeurAlphaBeta, int profondeurMinimax) {
        Jeu jeu = Jeu.initialiserJeu();
        List<Joueur> joueurs = new ArrayList<>();

        // Création des IA - AlphaBeta (Joueur 1) vs Minimax (Joueur 2)
        joueurs.add(new Joueur(Algorithmes.ALPHA_BETA, profondeurAlphaBeta));
        joueurs.add(new Joueur(Algorithmes.MINIMAX, profondeurMinimax));

        // Démarrer la partie (sans affichage)
        return demarrerPartieSansInterface(jeu, joueurs);
    }

    private static int demarrerPartieSansInterface(Jeu jeu, List<Joueur> joueurs) {
        int jetonActuel = 1;
        int jetonAdversaire;
        boolean partieFinie;
        int tours = 0;
        final int MAX_TOURS = 100; // Prévention des boucles infinies

        while (tours++ < MAX_TOURS) {
            jetonAdversaire = 3 - jetonActuel;
            Joueur joueur = joueurs.get(jetonActuel - 1);

            // L'IA joue son coup
            partieFinie = jeu.jouerIA(joueur, jetonActuel, jetonAdversaire, false);

            if (partieFinie) {
                return jeu.getPlateau().getVainqueur();
            }

            // Passer au joueur suivant
            jetonActuel = 3 - jetonActuel;
        }
        return 0; // Match nul si trop de tours
    }

    private static String formaterResultat(int resultat) {
        if (resultat == 1) {
            return "AlphaBeta vainqueur";
        } else if (resultat == 2) {
            return "Minimax vainqueur";
        } else {
            return "Match nul";
        }
    }
}