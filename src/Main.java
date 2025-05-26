import Algorithmes.*;
import Puissance4.*;

import java.util.*;

public class Main {
    public static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        /** Pour lancer le jeu Puissance4 */
        interfacePuissance4();

        /** Décommentez ces fonctions pour la partie analyse */
        //analyser_Minimax_AlphaBeta();
        //analyser_Profondeur_AlphaBeta();
        //analyser_AlphaBeta_Minimax_vs_Aleatoire();
        //analyser_MCTS();
        //analyserProfondeurArbreAvecC();
    }

    // Interface utilisateur pour le jeu Puissance4
    public static void interfacePuissance4()
    {
        System.out.println("## Puissance 4 ##");

        Jeu jeu = Jeu.initialiserJeu();
        HashMap<Integer, String> modesTexte = new HashMap<>();
        modesTexte.put(0, "Joueur VS Joueur");
        modesTexte.put(1, "Joueur VS IA");
        modesTexte.put(2, "IA VS IA");
        HashMap<Integer, Algorithmes[]> algosJoueurs = new HashMap<>();

        Algorithmes[] algos0 = {null, null};
        algosJoueurs.put(0, algos0);
        Algorithmes[] algos1 = {null, Algorithmes.RANDOM};
        algosJoueurs.put(1, algos1);
        Algorithmes[] algos2 = {Algorithmes.RANDOM, Algorithmes.RANDOM};
        algosJoueurs.put(2, algos2);

        System.out.println("Choisissez un mode de jeu : ");
        for (Map.Entry entree : modesTexte.entrySet()) {
            System.out.println(entree.getKey() + " -> " + entree.getValue());
        }

        System.out.print("Saisissez votre choix : ");
        List<Joueur> joueurs;
        while(true)
        {
            int choixMode = scanner.nextInt();
            if(!algosJoueurs.containsKey(choixMode))
            {
                System.out.println("Choix impossible !");
            }
            else
            {
                joueurs = new ArrayList<>();
                joueurs.add(new Joueur(algosJoueurs.get(choixMode)[0], null));
                joueurs.add(new Joueur(algosJoueurs.get(choixMode)[1], null));
                break;
            }
        }

        boolean presenceIA = false;
        for(Joueur joueur : joueurs)
        {
            if(!joueur.estHumain())
            {
                presenceIA = true;
                break;
            }
        }

        if(presenceIA)
        {
            HashMap<Integer, String> nomsAlgos = new HashMap<>();
            HashMap<Integer, Algorithmes> choixAlgos = new HashMap<>();
            nomsAlgos.put(0, "Aleatoire");
            nomsAlgos.put(1, "Minimax");
            nomsAlgos.put(2, "Alpha-Beta");
            nomsAlgos.put(3, "MCTS");
            choixAlgos.put(0, Algorithmes.RANDOM);
            choixAlgos.put(1, Algorithmes.MINIMAX);
            choixAlgos.put(2, Algorithmes.ALPHA_BETA);
            choixAlgos.put(3, Algorithmes.MCTS);

            System.out.println("\nSélectionnez l'algorithme que l'IA utilisera:");
            for(Map.Entry nom : nomsAlgos.entrySet())
            {
                System.out.println(nom.getKey() + " => " + nom.getValue());
            }

            int compteurIA = 0;
            int choixAlgo;
            for(Joueur joueur : joueurs)
            {
                if(!joueur.estHumain())
                {
                    compteurIA++;
                    System.out.print("Algo de l'IA " + compteurIA + " : ");
                    choixAlgo = scanner.nextInt();
                    if(choixAlgos.containsKey(choixAlgo)) {
                        joueur.setAlgorithme(choixAlgos.get(choixAlgo));
                    }
                    else {
                        System.out.println("Cet algorithme n'existe pas, l'IA utilisera l'algorithme Aleatoire.");
                        joueur.setAlgorithme(Algorithmes.RANDOM);
                    }

                    switch (joueur.getAlgorithme())
                    {
                        case RANDOM:
                            joueur.setParametre(0);
                            break;
                        case MINIMAX:
                        case ALPHA_BETA:
                            System.out.print("Choisissez la profondeur p : ");
                            joueur.setParametre(scanner.nextInt());
                            break;
                        case MCTS:
                            System.out.print("Choisissez le nombre d'itérations : ");
                            joueur.setParametre(scanner.nextInt());
                            break;
                        default:
                            System.out.println("Erreur, l'algorithme n'existe pas ! L'IA utilisera Aleatoire.");
                            joueur.setAlgorithme(Algorithmes.RANDOM);
                            joueur.setParametre(0);
                            break;
                    }
                }
            }
        }

        System.out.println("\n# La partie commence ! #\n");
        demarrerPartie(jeu, joueurs, true);
    }

    // Démarre une partie de Puissance4
    public static int demarrerPartie(Jeu jeu, List<Joueur> joueurs, boolean afficher)
    {
        Integer jetonAdversaire;
        Integer jetonActuel;
        int coup;
        Joueur joueur;
        boolean partieFinie = false;

        while(true)
        {
            for(int i = 1 ; i <= joueurs.size(); i++)
            {
                jetonActuel = i;
                jetonAdversaire = 1 + i%2;

                if(joueurs.get(i - 1).estHumain())
                {
                    while(true)
                    {
                        Puissance4_algos.afficher(afficher, "Saisissez le coup du joueur " + jetonActuel);
                        coup = scanner.nextInt();
                        if(jeu.colonneValide(coup))
                        {
                            break;
                        }
                        System.out.println("Coup invalide !");
                    }

                    partieFinie = jeu.jouerDansColonne(coup, jetonActuel);
                    Puissance4_algos.afficher(afficher, "Le joueur " + jetonActuel + " joue en " + coup);
                    Puissance4_algos.afficher(afficher, "Score = " + jeu.getPlateau().evaluation(jetonActuel, jetonAdversaire));
                }
                else
                {
                    joueur = joueurs.get(jetonActuel - 1);
                    partieFinie = jeu.jouerIA(joueur, jetonActuel, jetonAdversaire, afficher);
                    Puissance4_algos.afficher(afficher, "Score = " + jeu.getPlateau().evaluation(jetonActuel, jetonAdversaire));
                }

                if(afficher)
                {
                    jeu.afficherEtatJeu();
                }

                if(partieFinie)
                {
                    break;
                }
            }

            Puissance4_algos.afficher(afficher, "");
            if(partieFinie)
            {
                Puissance4_algos.afficher(afficher, "La partie est terminée !");
                int gagnant = jeu.getPlateau().getVainqueur();
                if(gagnant != 0)
                {
                    Puissance4_algos.afficher(afficher, "Le joueur " + gagnant + " a gagné !");
                }
                return gagnant;
            }
        }
    }
}