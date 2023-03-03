package brainball;

import toolbox.LoggingUtil;

import java.util.List;

public class Analyzer {

    private static final int BRAIN_BALL_SIZE = 13;
    BrainBall brainBall;
    LoggingUtil logger = new LoggingUtil();

    public Analyzer(BrainBall brainBall) {
        this.brainBall = brainBall;
    }

    public Analyzer() {
    }

    public void analyze(BrainBall brainBall, List<Integer> movesList) {
        System.out.println("Externe Movelist: " + movesList.size());
        int movesSize = movesList.size();
        for (int i = 0; i < movesSize; i++) {

            if (movesList.get(movesList.size() - 1) != -13) {
                brainBall.makeMove(BRAIN_BALL_SIZE - movesList.get(movesList.size() - 1));
            } else {
                brainBall.flip();
            }

            //Konfiguration
            for (BrainBall.RingElement ringElement : brainBall.getRing()) {
                if (ringElement.getColor() == BrainBall.RingElement.Color.YELLOW) {
                    logger.append("\t" + (ringElement.getNumber() + BRAIN_BALL_SIZE) + ",");
                } else {
                    logger.append("\t" + ringElement.getNumber() + ",");
                }
            }
            //Heuristic A,B,C
            logger.append("\t" + BrainBallHeuristic.getHeuristicA(brainBall) + ",");
            logger.append("\t" + BrainBallHeuristic.getHeuristicB(brainBall) + ",");
            logger.append("\t" + BrainBallHeuristic.getHeuristicC(brainBall) + ",");

            //Schritte bis zur Lösung
            logger.append("\t" + (i + 1) + ",");

            //Next move
            logger.append("\t" + movesList.get(movesList.size() - 1) + ",");
            movesList.remove(movesList.size() - 1);

            //New Game
            if (i == 0) {
                logger.appendnl("\t" + 1);
            } else {
                logger.appendnl("\t" + 0);
            }

        }
    }

}
