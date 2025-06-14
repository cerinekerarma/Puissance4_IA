package Tests;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.regex.*;

public class ParseResultats {
    private Map<Integer, int[]> stats;
    private static String nomFichier = "resultats/resultats_tests_alphabeta_vs_alphabeta.txt";

    public ParseResultats(String nomFichier) {
        this.nomFichier = nomFichier;
        this.stats = new HashMap<>();
    }

    public void parser() {
        Pattern pattern = Pattern.compile(
                "IA 1 \\(profondeur = (\\d+)\\) vs IA 2 \\(profondeur = (\\d+)\\) : IA (\\d) vainqueur"
        );

        try (BufferedReader br = new BufferedReader(new FileReader(nomFichier))) {
            String ligne;
            while ((ligne = br.readLine()) != null) {
                Matcher matcher = pattern.matcher(ligne);
                if (matcher.find()) {
                    int profondeurIA1 = Integer.parseInt(matcher.group(1));
                    int vainqueur = Integer.parseInt(matcher.group(3));

                    stats.putIfAbsent(profondeurIA1, new int[]{0, 0});
                    stats.get(profondeurIA1)[1]++; // Total joué
                    if (vainqueur == 1) {
                        stats.get(profondeurIA1)[0]++; // Victoire
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void afficherTauxReussite() {
        List<Integer> profondeurs = new ArrayList<>(stats.keySet());
        Collections.sort(profondeurs);

        for (int profondeur : profondeurs) {
            int[] resultats = stats.get(profondeur);
            int gagne = resultats[0];
            int joue = resultats[1];
            double taux = (joue > 0) ? (100.0 * gagne / joue) : 0.0;
            System.out.printf("Profondeur = %d : %.1f%% de réussite%n", profondeur, taux);
        }
    }

    public static void main(String[] args) {
        ParseResultats analyseur = new ParseResultats(nomFichier); // <- adapte le nom du fichier ici
        analyseur.parser();
        analyseur.afficherTauxReussite();
    }
}
