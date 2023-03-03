package brainball;

import java.util.List;

public class BrainBallSolver {

    private static final int SHUFFLE_MOVES = 10;

    public static void main(String[] args) {
            BrainBall brainBall = new BrainBall();
            Graph<BrainBall> graph = new Graph<>();
            Node<BrainBall> root = graph.addNode("root", brainBall);
            IDAStar<BrainBall> idaStar = new IDAStar<>(graph, BrainBallHeuristic::getHeuristicB);
            IDAStar.EXPAND_GRAPH = true;
            IDAStar.LOGGING = false;

            MCTS<BrainBall> mcts = new MCTS<>(graph);

            brainBall.shuffle(SHUFFLE_MOVES);
            Node<BrainBall> solve;
            solve = idaStar.solve(root);
//        solve = mcts.solve(root);

            System.out.println("BrainBall: " + (solve == null ? "null" : solve.getContent().toString()) + "\nAll moves: " + (solve == null ? "null" : solve.getContent().getMoves() + "\nNumber of solving moves: " + (solve == null ? "null" : solve.getContent().getMoves().size() - SHUFFLE_MOVES + "\nSolved: " + (solve == null ? "null" : solve.getContent().isSolved()))));

            brainBall = solve.getContent();
            List<Integer> moves = brainBall.getMoves().subList(SHUFFLE_MOVES, brainBall.getMoves().size());

            Analyzer analyzer = new Analyzer();
            analyzer.analyze(brainBall, moves);

    }
}
