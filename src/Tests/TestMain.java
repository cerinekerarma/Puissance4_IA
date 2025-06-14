package Tests;

import Algorithmes.MCTS;
import Puissance4.Plateau;

public class TestMain {
    public static void main(String[] args) {
        MCTS mcts = new MCTS(1, 2);
        Plateau p = new Plateau(6, 7, 1, null);

        // Test exploration intensive
        mcts.definirConstanteC(10);
        mcts.executer(p.clone(), 1000);
        System.out.println("C=10: " + mcts.getArbreStats());

        // Test exploitation intensive
        mcts.definirConstanteC(0.1);
        mcts.executer(p.clone(), 1000);
        System.out.println("C=0.1: " + mcts.getArbreStats());
    }
}
