import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Puissance4.*;

public class Puissance4Vue {
    private Jeu jeu;
    private List<Joueur> joueurs;
    private JFrame frame;
    private JPanel gamePanel;
    private JButton[] columnButtons;
    private JLabel statusLabel;
    private int currentPlayer = 1;
    private boolean gameFinished = false;

    public Puissance4Vue() {
        createModeSelectionDialog();
    }

    private void setupPlayers(int mode) {
        HashMap<Integer, Algorithmes[]> algosJoueurs = new HashMap<>();
        algosJoueurs.put(0, new Algorithmes[]{null, null});
        algosJoueurs.put(1, new Algorithmes[]{null, Algorithmes.RANDOM});
        algosJoueurs.put(2, new Algorithmes[]{Algorithmes.RANDOM, Algorithmes.RANDOM});

        joueurs = new ArrayList<>();
        joueurs.add(new Joueur(algosJoueurs.get(mode)[0], null));
        joueurs.add(new Joueur(algosJoueurs.get(mode)[1], null));

        if (mode == 1) {
            if (!joueurs.get(1).estHumain()) {
                selectAlgorithmForAI(joueurs.get(1), "Choisir l'algorithme pour l'IA");
            }
        } else if (mode == 2) {
            if (!joueurs.get(0).estHumain()) {
                selectAlgorithmForAI(joueurs.get(0), "Choisir l'algorithme pour l'IA 1");
            }
            if (!joueurs.get(1).estHumain()) {
                selectAlgorithmForAI(joueurs.get(1), "Choisir l'algorithme pour l'IA 2");
            }
        }
    }


    private void selectAlgorithmForAI(Joueur joueur, String titre) {
        JDialog algoDialog = new JDialog();
        algoDialog.setTitle(titre);
        algoDialog.setLayout(new BorderLayout());
        algoDialog.setModal(true);
        algoDialog.setSize(400, 300);
        algoDialog.setLocationRelativeTo(null);

        JPanel algoPanel = new JPanel(new GridLayout(5, 1, 10, 10));
        algoPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel label = new JLabel(titre, JLabel.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 16));
        algoPanel.add(label);

        HashMap<Integer, String> nomsAlgos = new HashMap<>();
        nomsAlgos.put(0, "Aléatoire");
        nomsAlgos.put(1, "Minimax");
        nomsAlgos.put(2, "Alpha-Beta");
        nomsAlgos.put(3, "MCTS");

        for (Map.Entry<Integer, String> entry : nomsAlgos.entrySet()) {
            JButton algoButton = new JButton(entry.getValue());
            int algo = entry.getKey();
            algoButton.addActionListener(e -> {
                HashMap<Integer, Algorithmes> choixAlgos = new HashMap<>();
                choixAlgos.put(0, Algorithmes.RANDOM);
                choixAlgos.put(1, Algorithmes.MINIMAX);
                choixAlgos.put(2, Algorithmes.ALPHA_BETA);
                choixAlgos.put(3, Algorithmes.MCTS);

                if (choixAlgos.containsKey(algo)) {
                    joueur.setAlgorithme(choixAlgos.get(algo));
                    setAIParameter(joueur);
                } else {
                    JOptionPane.showMessageDialog(algoDialog,
                            "Cet algorithme n'existe pas, l'IA utilisera l'algorithme Aléatoire.");
                    joueur.setAlgorithme(Algorithmes.RANDOM);
                    joueur.setParametre(0);
                }
                algoDialog.dispose();
            });
            algoPanel.add(algoButton);
        }

        algoDialog.add(algoPanel, BorderLayout.CENTER);
        algoDialog.setVisible(true);
    }

    private void setAIParameter(Joueur joueur) {
        switch (joueur.getAlgorithme()) {
            case RANDOM:
                joueur.setParametre(0);
                break;
            case MINIMAX:
            case ALPHA_BETA:
                joueur.setParametre(5); // Profondeur par défaut
                break;
            case MCTS:
                joueur.setParametre(5); // Nombre d'itérations par défaut
                break;
            default:
                joueur.setAlgorithme(Algorithmes.RANDOM);
                joueur.setParametre(0);
                break;
        }
    }

    private void createModeSelectionDialog() {
        JDialog modeDialog = new JDialog();
        modeDialog.setTitle("Puissance 4 - Sélection du mode");
        modeDialog.setLayout(new BorderLayout());
        modeDialog.setModal(true);
        modeDialog.setSize(400, 300);
        modeDialog.setLocationRelativeTo(null);

        JLabel titleLabel = new JLabel("Puissance 4", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        modeDialog.add(titleLabel, BorderLayout.NORTH);

        JPanel modePanel = new JPanel(new GridLayout(3, 1, 10, 10));
        modePanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        HashMap<Integer, String> modesTexte = new HashMap<>();
        modesTexte.put(0, "Joueur VS Joueur");
        modesTexte.put(1, "Joueur VS IA");
        modesTexte.put(2, "IA VS IA");

        for (Map.Entry<Integer, String> entry : modesTexte.entrySet()) {
            JButton modeButton = new JButton(entry.getValue());
            int mode = entry.getKey();
            modeButton.addActionListener(e -> {
                modeDialog.dispose();
                setupPlayers(mode);
                initializeGame();
            });
            modePanel.add(modeButton);
        }

        modeDialog.add(modePanel, BorderLayout.CENTER);
        modeDialog.setVisible(true);
    }

    private void initializeGame() {
        jeu = Jeu.initialiserJeu();
        currentPlayer = 1;
        gameFinished = false;

        frame = new JFrame("Puissance 4");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(700, 600);
        frame.setLayout(new BorderLayout());

        statusLabel = new JLabel("Tour du joueur " + currentPlayer, JLabel.CENTER);
        statusLabel.setFont(new Font("Arial", Font.BOLD, 16));
        frame.add(statusLabel, BorderLayout.NORTH);

        gamePanel = new JPanel(new GridLayout(6, 7, 0, 0)); // Supprime l'espacement entre les cellules
        gamePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        gamePanel.setBackground(Color.BLUE); // Fond bleu pour le plateau
        frame.add(gamePanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new GridLayout(1, 7));
        columnButtons = new JButton[7];
        for (int i = 0; i < 7; i++) {
            columnButtons[i] = new JButton("" + (i + 1)); // Affiche juste le numéro de colonne
            final int column = i ;
            columnButtons[i].addActionListener(e -> {
                if (!gameFinished && joueurs.get(currentPlayer - 1).estHumain()) {
                    playMove(column);
                }
            });
            buttonPanel.add(columnButtons[i]);
        }
        frame.add(buttonPanel, BorderLayout.SOUTH);

        updateBoard();
        frame.setVisible(true);

        if (!joueurs.get(currentPlayer - 1).estHumain()) {
            playAIMove();
        }
    }

    private void playAIMove() {
        if (gameFinished){ return; }

        Joueur joueur = joueurs.get(currentPlayer - 1);
        int jetonAdversaire = 3 - currentPlayer;
        boolean partieFinie = jeu.jouerIA(joueur, currentPlayer, jetonAdversaire, false);
        updateBoard();

        if (partieFinie) {
            endGame();
            return;
        }

        currentPlayer = 3 - currentPlayer;
        statusLabel.setText("Tour du joueur " + currentPlayer);

        // Si le prochain joueur est encore une IA, continuer
        if (!joueurs.get(currentPlayer - 1).estHumain()) {
            Timer timer = new Timer(1000, e -> playAIMove());
            timer.setRepeats(false);
            timer.start();
        }
    }


    private void endGame() {
        gameFinished = true;
        int gagnant = jeu.getPlateau().getVainqueur();
        if (gagnant != 0) {
            statusLabel.setText("Le joueur " + gagnant + " a gagné !");
        } else {
            statusLabel.setText("Match nul !");
        }

        String message = "La partie est terminée !\n" + (gagnant != 0 ? "Le joueur " + gagnant + " a gagné !" : "Match nul !");

        JOptionPane.showMessageDialog(frame, message);
        frame.dispose();
        System.exit(0);

    }

    private void playMove(int column) {
        if (gameFinished || !jeu.colonneValide(column)) {
            return;
        }

        boolean partieFinie = jeu.jouerDansColonne(column, currentPlayer);
        updateBoard();

        if (partieFinie) {
            endGame();
            return;
        }

        currentPlayer = 3 - currentPlayer;
        statusLabel.setText("Tour du joueur " + currentPlayer);

        if (!joueurs.get(currentPlayer - 1).estHumain()) {
            Timer timer = new Timer(1000, e -> playAIMove());
            timer.setRepeats(false);
            timer.start();
        }
    }

    private void updateBoard() {
        gamePanel.removeAll();
        Plateau plateau = jeu.getPlateau();
        int[][] grille = plateau.getGrille();

        for (int row = 0; row <= 5; row++) {
            for (int col = 0; col < 7; col++) {
                int finalRow = row;
                int finalCol = col;
                JPanel cell = new JPanel() {
                    @Override
                    protected void paintComponent(Graphics g) {
                        super.paintComponent(g);
                        Graphics2D g2d = (Graphics2D) g;
                        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                        // Dessine un cercle pour le jeton
                        if (grille[finalRow][finalCol] == 1) {
                            g2d.setColor(Color.RED);
                            g2d.fillOval(5, 5, getWidth() - 10, getHeight() - 10);
                        } else if (grille[finalRow][finalCol] == 2) {
                            g2d.setColor(Color.YELLOW);
                            g2d.fillOval(5, 5, getWidth() - 10, getHeight() - 10);
                        } else {
                            // Case vide - dessine un cercle blanc avec bordure bleue
                            g2d.setColor(Color.WHITE);
                            g2d.fillOval(5, 5, getWidth() - 10, getHeight() - 10);
                            g2d.setColor(Color.BLUE);
                            g2d.drawOval(5, 5, getWidth() - 10, getHeight() - 10);
                        }
                    }
                };
                cell.setPreferredSize(new Dimension(60, 60));
                cell.setBackground(Color.BLUE); // Fond bleu pour les cases
                gamePanel.add(cell);
            }
        }

        gamePanel.revalidate();
        gamePanel.repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Puissance4Vue());
    }
}