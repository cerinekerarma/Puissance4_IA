package Tests;

import Puissance4.*;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import Puissance4.*;

public class TestAlphaBetaVSAlphaBeta {
// si apres avoir lancé, vous avez un outOfMemoryError, changer la capacite de RAM a 4Go: Run > Edit Configurations > VM options : -Xmx4G
    public static void main(String[] args) {
        // Profondeurs à tester
        int[] profondeurs = {3, 5, 7, 9, 10};
        StringBuilder resultats = new StringBuilder();

        // Fichier de sortie
        String fichierResultats = "resultats_tests_alphabeta_vs_alphabeta.txt";

        try (FileWriter writer = new FileWriter(fichierResultats)) {
            // En-tête du fichier
            writer.write("Résultats des tests AlphaBeta vs AlphaBeta\n\n");

            // Tests pour chaque combinaison de profondeurs
            for (int i = 0; i < profondeurs.length; i++) {
                for (int j = i; j < profondeurs.length; j++) {
                    int profondeur1 = profondeurs[i];
                    int profondeur2 = profondeurs[j];

                    // Jouer la partie
                    int resultat = jouerPartieAlphaBeta(profondeur1, profondeur2);

                    // Enregistrer le résultat
                    String resultatPartie = String.format(
                            "Partie %d\nIA 1 (profondeur = %d) vs IA 2 (profondeur = %d) : %s\n\n",
                            (i * profondeurs.length) + j + 1,
                            profondeur1,
                            profondeur2,
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

    private static int jouerPartieAlphaBeta(int profondeur1, int profondeur2) {
        Jeu jeu = Jeu.initialiserJeu();
        List<Joueur> joueurs = new ArrayList<>();

        // Création des deux IA AlphaBeta avec les profondeurs spécifiées
        joueurs.add(new Joueur(Algorithmes.ALPHA_BETA, profondeur1));
        joueurs.add(new Joueur(Algorithmes.ALPHA_BETA, profondeur2));

        // Démarrer la partie (sans affichage)
        return demarrerPartieSansInterface(jeu, joueurs);
    }

    private static int demarrerPartieSansInterface(Jeu jeu, List<Joueur> joueurs) {
        int jetonActuel = 1;
        int jetonAdversaire;
        boolean partieFinie;

        while (true) {
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
    }

    private static String formaterResultat(int resultat) {
        if (resultat == 1) {
            return "IA 1 vainqueur";
        } else if (resultat == 2) {
            return "IA 2 vainqueur";
        } else {
            return "Match nul";
        }
    }
}
