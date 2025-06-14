package Tests;

import Puissance4.*;
import Algorithmes.AlphaBeta;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TestAlphaBetaVSAlphaBeta {
    public static void main(String[] args) {
        int[] profondeurs = {1,2,3,4,5,6,7,8};
        StringBuilder resultats = new StringBuilder();

        String fichierResultats = "resultats/resultats_tests_alphabeta_vs_alphabeta.txt";

        try (FileWriter writer = new FileWriter(fichierResultats)) {
            writer.write("Résultats des tests AlphaBeta vs AlphaBeta\n\n");
            writer.write("Format : IA 1 (profondeur) vs IA 2 (profondeur) | Vainqueur | Nœuds IA1 | Nœuds IA2 | Temps IA1 (ms) | Temps IA2 (ms)\n\n");

            for (int i = 0; i < profondeurs.length; i++) {
                for (int j = i; j < profondeurs.length; j++) {
                    int profondeur1 = profondeurs[i];
                    int profondeur2 = profondeurs[j];

                    // Jouer la partie avec mesure des métriques
                    PartieResultat resultat = jouerPartieAlphaBeta(profondeur1, profondeur2);

                    // Enregistrer le résultat
                    String resultatPartie = String.format(
                            "Partie %d\nIA 1 (profondeur=%d) vs IA 2 (profondeur=%d) : %s\n" +
                                    "Nœuds créés - IA1: %d | IA2: %d\n" +
                                    "Temps moyen/coup - IA1: %.3f ms | IA2: %.3f ms\n\n",
                            (i * profondeurs.length) + j + 1,
                            profondeur1,
                            profondeur2,
                            formaterResultat(resultat.vainqueur),
                            resultat.noeudsIA1,
                            resultat.noeudsIA2,
                            resultat.tempsMoyenIA1,
                            resultat.tempsMoyenIA2
                    );

                    writer.write(resultatPartie);
                    System.out.print(resultatPartie);
                }
            }
            System.out.println("Tests complétés. Résultats dans " + fichierResultats);
        } catch (IOException e) {
            System.err.println("Erreur fichier: " + e.getMessage());
        }
    }

    private static class PartieResultat {
        int vainqueur;
        int noeudsIA1;
        int noeudsIA2;
        double tempsMoyenIA1;
        double tempsMoyenIA2;
    }

    private static PartieResultat jouerPartieAlphaBeta(int profondeur1, int profondeur2) {
        Jeu jeu = Jeu.initialiserJeu();
        List<Joueur> joueurs = new ArrayList<>();
        PartieResultat resultat = new PartieResultat();
        int totalCoupsIA1 = 0, totalCoupsIA2 = 0;
        long tempsTotalIA1 = 0, tempsTotalIA2 = 0;

        joueurs.add(new Joueur(Algorithmes.ALPHA_BETA, profondeur1));
        joueurs.add(new Joueur(Algorithmes.ALPHA_BETA, profondeur2));

        int jetonActuel = 1;
        int tours = 0;
        final int MAX_TOURS = 100;

        while (tours++ < MAX_TOURS) {
            int jetonAdversaire = 3 - jetonActuel;
            Joueur joueur = joueurs.get(jetonActuel - 1);

            // Mesure du temps et des nœuds
            long debut = System.nanoTime();
            boolean fin = jeu.jouerIA(joueur, jetonActuel, jetonAdversaire, false);
            long duree = System.nanoTime() - debut;

            if (jetonActuel == 1) {
                tempsTotalIA1 += duree;
                totalCoupsIA1++;
                resultat.noeudsIA1 = AlphaBeta.getNoeudsCrees();
            } else {
                tempsTotalIA2 += duree;
                totalCoupsIA2++;
                resultat.noeudsIA2 = AlphaBeta.getNoeudsCrees();
            }

            if (fin) {
                resultat.vainqueur = jeu.getPlateau().getVainqueur();
                break;
            }
            jetonActuel = 3 - jetonActuel;
        }

        // Calcul des temps moyens
        resultat.tempsMoyenIA1 = totalCoupsIA1 > 0 ? (tempsTotalIA1 / 1_000_000.0) / totalCoupsIA1 : 0;
        resultat.tempsMoyenIA2 = totalCoupsIA2 > 0 ? (tempsTotalIA2 / 1_000_000.0) / totalCoupsIA2 : 0;

        return resultat;
    }

    private static String formaterResultat(int resultat) {
        switch (resultat) {
            case 1: return "IA 1 vainqueur";
            case 2: return "IA 2 vainqueur";
            default: return "Match nul";
        }
    }
}