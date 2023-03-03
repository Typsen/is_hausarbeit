package brainball;

import java.util.List;

public final class BrainBallHeuristic {

    private BrainBallHeuristic(){}

    /**
     * A Heuristic, based on the number of correct ordered Ring-Elements.
     * @param brainBall
     * @return A heuristic value.
     */
    public static int getHeuristicA(BrainBall brainBall) {
        List<BrainBall.RingElement> brainBallRing = brainBall.getRing();
        int heuristic = brainBallRing.size();
        int score = 1;

        for (int i = 0; i < brainBallRing.size(); i++) {
            if (BrainBall.nextFollows(brainBallRing,i)) {
                heuristic = heuristic - score;
            }
        }
        return heuristic;
    }

    /**
     * A Heuristic, based on the number of correct ordered Ring-Elements and identically colored Ring-Elements in the
     * flip-areas.
     * @param brainBall The BrainBall to check.
     * @return A heuristic value.
     */
    public static int getHeuristicB(BrainBall brainBall) {
        List<BrainBall.RingElement> brainBallRing = brainBall.getRing();
        int heuristic = brainBallRing.size()+4;
        int score = 1;

        for (int i = 0; i < brainBallRing.size(); i++) {
            if (BrainBall.nextFollows(brainBallRing,i)) {
                heuristic = heuristic - score;
            }
        }
        if (checkSegment(brainBallRing, 0,3)) {heuristic = heuristic - score;}
        if (checkSegment(brainBallRing, 3,6)) {heuristic = heuristic - score;}
        if (checkSegment(brainBallRing, 6,10)) {heuristic = heuristic - score;}
        if (checkSegment(brainBallRing, 10,13)) {heuristic = heuristic - score;}
        return heuristic;
    }

    /**
     * A Heuristic, based on the identically colored Ring-Elements in the flip-areas.
     * @param brainBall The BrainBall to check.
     * @return A heuristic value.
     */
    public static int getHeuristicC(BrainBall brainBall) {
        List<BrainBall.RingElement> brainBallRing = brainBall.getRing();
        int heuristic = 4;
        int score = 1;

        if (checkSegment(brainBallRing, 0,3)) {heuristic = heuristic - score;}
        if (checkSegment(brainBallRing, 3,6)) {heuristic = heuristic - score;}
        if (checkSegment(brainBallRing, 6,10)) {heuristic = heuristic - score;}
        if (checkSegment(brainBallRing, 10,13)) {heuristic = heuristic - score;}
        return heuristic;
    }

    /**
     * Checks weather or not all elements in the flip-areas have the same color.
     * @param ring The Ring to check
     * @param startIndex The starting index of the flip-area.
     * @param endIndex The ending index of the flip-area
     * @return True if all elements in the range have the same color.
     */
    private  static boolean checkSegment (List<BrainBall.RingElement> ring, int startIndex, int endIndex) {
        boolean sameColor = true;
        for (int i = startIndex; i < endIndex; i++) {
            if (ring.get(i).getColor() != ring.get(startIndex).getColor()) {
                sameColor = false;
            }
        }
        return sameColor;
    }
}
