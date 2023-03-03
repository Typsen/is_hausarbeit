package brainball;

import java.util.Random;

public class MCTS<T extends Solvable & Expandable<T>> {

    public static boolean LOGGING = false;
    public static int counter;

    private Graph<T> graph;

    public MCTS(Graph graph) {
        this.graph = graph;
    }

    public Node<BrainBall> solve(Node<BrainBall> root) {
        Random rn = new Random();
        BrainBall brainBall = root.getContent();
        int move;
        while (!brainBall.isSolved()) {
            move = rn.nextInt(12) + 1;
            brainBall.makeMove(move);
            brainBall.makeMove(13);
            counter++;
        }
        System.out.println("MCTS moves done: " + counter);
        return root;
    }
}
