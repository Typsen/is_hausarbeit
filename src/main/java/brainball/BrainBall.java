package brainball;

import java.util.*;
import java.util.stream.Collectors;

public class BrainBall implements Solvable, Expandable<BrainBall> {

    public boolean LOGGING = false;

    private static final int STANDARD_SIZE = 13;
    private static final int STANDARD_SHUFFLE_MOVES = 10;
    private List<RingElement> ring = new LinkedList<>();
    private Stack<Integer> moves = new Stack<>();
    private Stack<Integer> undoneMoves = new Stack<>();

    public BrainBall() {
        for (int i = 0; i < STANDARD_SIZE; i++) {
            ring.add(new RingElement(i));
        }
    }

    public BrainBall(boolean reverse) {
        for (int i = STANDARD_SIZE; i > 0; i--) {
            ring.add(new RingElement(i));
        }
    }

    public BrainBall(BrainBall original) {
        original.ring.forEach(ringElement -> this.ring.add(new RingElement(ringElement)));
        this.moves = (Stack<Integer>) original.moves.clone();
        this.undoneMoves = (Stack<Integer>) original.undoneMoves.clone();
        this.LOGGING = original.LOGGING;
    }

    /**
     * Rotates the Ring one step clockwise.
     */
    public void rotate() {
        Collections.rotate(ring, 1);
    }

    /**
     * Rotates the Ring multiple steps. If steps is positive the rotation is clockwise if negative anticlockwise.
     * @param steps Amount of steps.
     */
    public void rotate(int steps) {
        Collections.rotate(ring, steps);
        updateHistory(steps);
    }

    /**
     * This method flips elements 0 - 2 and 6-9 of the ring "around", therefore reversing the order and switching their
     * color from white to yellow and vice versa. STANDARD_SIZE and -STANDARD_SIZE are considered flip-commands.
     */
    public void flip() { flip(STANDARD_SIZE);}

    public void flip(int move) {
        Collections.reverse(ring.subList(0, 3));
        ring.subList(0, 3).forEach(RingElement::changeColor);
        Collections.reverse(ring.subList(6, 10));
        ring.subList(6, 10).forEach(RingElement::changeColor);
        updateHistory(move);
    }

    /**
     * Shuffles the BrainBall STANDARD_SHUFFLE_MOVES and returns a list of all performed moves.
     * @return A list of all performed moves.
     */
    public List<Integer> shuffle() {
        return shuffle(STANDARD_SHUFFLE_MOVES);
    }

    /**
     * Shuffles the Brain ball numberOfMoves times and returns a list of all performed moves.
     * @param numberOfMoves The number of random moves to perform.
     * @return A list of all performed moves.
     */
    public List<Integer> shuffle(int numberOfMoves) {
        List<Integer> moves = new ArrayList<>();
        for (int i = 0; i < numberOfMoves; i++) {
            moves.add(randomMove());
        }
        return moves;
    }

    /**
     * Performs a move as follows:
     * -STANDARD_SIZE or STANDARD_SIZE: Flip
     * -STANDARD_SIZE+1 to -1: Counter-clockwise rotation 1 to STANDARD_SIZE-1 steps
     * 1 - STANDARD_SIZE-1: Clockwise rotation 1 to 12 steps
     * Any other number: No move
     *
     */
    public void makeMove(int move) {
        if (move == 0) { return;}

        if (-STANDARD_SIZE < move && move < STANDARD_SIZE) {
            if (LOGGING) { System.out.println("Move: " + move + ". Rotate.");}
            rotate(move);
            if (LOGGING) { System.out.println(this);}
        } else if (move == -STANDARD_SIZE || move == STANDARD_SIZE) {
            if (LOGGING) { System.out.println("Move: " + move + ". Flip.");}
            flip(move);
            if (LOGGING) { System.out.println(this);}
        }
    }

    /**
     * Performs multiple moves in sequence.
     * @param moves The list of moves to perform.
     */
    public void makeMoves(List<Integer> moves) {
        moves.forEach(this::makeMove);
    }

    /**
     * Performs multiple moves in sequence.
     * @param moves The list of moves to perform.
     * @param reverse If true, the method will perform the reverse of the moves in the list.
     */
    public void makeMoves(List<Integer> moves, boolean reverse) {
        if (reverse) {
            Collections.reverse(moves);
            moves = moves.stream().map(m -> -m).toList();
        }
        makeMoves(moves);
    }

    /**
     * Performs a random move with equal distribution between a rotation and a flip. No two flips can be performed after
     * another because it would be pointless.
     */
    public int randomMove() {
        if (Math.random() < 0.5 && (moves.size() == 0 || moves.get(moves.size() - 1) != STANDARD_SIZE)) {
            makeMove(STANDARD_SIZE);
            return STANDARD_SIZE;
        } else {
            int move = new Random().nextInt(STANDARD_SIZE - 1) + 1;
            makeMove(move);
            return move;
        }
    }

    /**
     * Performs multiple random moves.
     * @param count Number of moves to perform.
     * @return A list of all random moves.
     */
    public List<Integer> randomMoves(int count) {
        List<Integer> moves = new LinkedList<>();
        for (int i = 0; i < count; i++) {
            moves.add(randomMove());
        }
        return moves;
    }

    /**
     * Reverses the move done.
     * @return The original move that has been undone.
     * Note: The move to undo any given move is the inverse of the move, however the original move is being returned here.
     */
    public int undo() {
        if (moves.empty()) { return 0;}

        int lastMove = moves.peek();
        makeMove(-lastMove);
        return lastMove;
    }

    /**
     * Redoes moves undone with the undo-method.
     * @return The move that has been undone.
     */
    public int redo() {
        if (undoneMoves.empty()) { return 0;}

        int undoneMove = undoneMoves.peek();
        makeMove(undoneMove);
        return undoneMove;
    }

    /**
     * Updates the move and undoneMoves Stacks when a move is performed.
     * @param move the performed move.
     */
    private void updateHistory(int move){
        if(!moves.empty() && -move == moves.peek()){
            undoneMoves.add(moves.pop());
        } else if(!undoneMoves.empty() && move == undoneMoves.peek()){
            moves.add(undoneMoves.pop());
        } else {
            moves.add(move);
            undoneMoves.clear();
        }
    }

    /**
     * Checks if the BrainBall is solved. A BrainBall is considered solved, if all numbers have the same color and are in
     * ascending or descending order. The ring position/rotation doesn't matter.
     * @return true if the BrainBall is solved. False otherwise.
     */
    @Override
    public boolean isSolved(){
        return isSolved(true,true);
    }

    public boolean isSolved(boolean colors, boolean order) {
        for (int i = 0; i < ring.size(); i++) {
            if(!nextFollows(ring,i)){ return false;}
        }
        return true;
    }

    /**
     * Checks if the next element in the Ring is the same color and ascending or descending in order.
     * @param brainBallRing The BrainBall ring to check.
     * @param index The index of the element to check if the next element follows it.
     * @return true if the next element after index follows the element at index. This means that both have the same color
     * and that the next element either ascends or descends in order.
     */
    public static boolean nextFollows(List<BrainBall.RingElement> brainBallRing, int index) {
        return nextFollows(brainBallRing,index,true,true);
    }

    public static boolean nextFollows(List<BrainBall.RingElement> brainBallRing, int index, boolean colors, boolean order) {
        int nextIndex = (index+1) % brainBallRing.size();

        int currentNumber = brainBallRing.get(index).getNumber();
        BrainBall.RingElement.Color currentColor = brainBallRing.get(index).getColor();

        int ascendingNumber = currentNumber == brainBallRing.size()-1? 0 : currentNumber+1;
        int descendingNumber = currentNumber == 0? brainBallRing.size()-1 : currentNumber-1;

        boolean ascending = ascendingNumber == brainBallRing.get(nextIndex).getNumber();
        boolean descending = descendingNumber == brainBallRing.get(nextIndex).getNumber();

        BrainBall.RingElement.Color nextColor = brainBallRing.get(nextIndex).getColor();

        if(colors && order){
            return currentColor == nextColor && (ascending || descending);
        } else if (order) {
            return ascending || descending;
        } else {
            return !(currentColor == nextColor && (ascending || descending));
        }
    }

    /**
     * Solves a BrainBall by reversing all moves.
     */
    public void solve() {
        List<Integer> reverseMoves = moves
                .stream()
                .map(m -> -m)
                .collect(Collectors.collectingAndThen(Collectors.toList(),list -> {
                    Collections.reverse(list);
                    return list;
                }));
        makeMoves(reverseMoves);
    }

    /**
     * Expands a Node in a Graph with all possible next steps that can be taken. It performs a rotation and a flip
     * since 2 steps in a row could have been combined in a single step.
     * @param graph The graph with the node to expand.
     * @param node The node to expand.
     * @return The modified graph.
     */
    @Override
    public Graph<BrainBall> expand(Graph<BrainBall> graph, Node<BrainBall> node) {
        for (int i = 0; i <= node.getContent().getRing().size(); i++) {
            BrainBall tmpBrainBall = new BrainBall(node.getContent());
            tmpBrainBall.makeMove(i);
            tmpBrainBall.makeMove(-node.getContent().getRing().size());
            Node<BrainBall> newNode = graph.addNode(node.getId() + "_" + i, tmpBrainBall);
            graph.addUniNeighbor(node, newNode,2);
        }
        return graph;
    }

    public boolean getLOGGING() {
        return LOGGING;
    }

    public void setLOGGING(boolean LOGGING) {
        this.LOGGING = LOGGING;
    }

    public List<RingElement> getRing() {
        return Collections.unmodifiableList(ring);
    }

    public void setRing(List<RingElement> ring) {
        this.ring = ring;
    }

    public Stack<Integer> getMoves() {
        return (Stack<Integer>) moves.clone();
    }

//    @Override
//    public boolean equals(Object o) {
//        if (this == o){ return true;}
//        if(o == null || getClass() != o.getClass()) { return false;}
//
//        List<RingElement> oRing = ((BrainBall) o).ring;
//        RingElement oStartElement = oRing.stream()
//                .filter(ringElement -> ringElement.number == ring.get(0).number && ringElement.color == ring.get(0).color)
//                .findAny()
//                .orElse(null);
//        if(oStartElement == null){ return false;}
//
//        int oStartIndex = oRing.indexOf(oStartElement);
//
//        for (int i = 0; i < ring.size(); i++) {
//            if(ring.get(i).number != oRing.get(oStartIndex).number || ring.get(i).color != oRing.get(oStartIndex).color){
//                return false;
//            }
//            oStartIndex = (oStartIndex+1)%oRing.size();
//        }
//
//        return true;
//    }
//
//    @Override
//    public int hashCode() {
//        return super.hashCode();
//    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        ring.forEach(ringElement -> stringBuilder.append(ringElement.toString()));
        return stringBuilder.toString();
    }

    public static class RingElement {
        public enum Color {WHITE, YELLOW}

        private final int number;
        private Color color;

        RingElement(int number) {
            this.number = number;
            color = Color.YELLOW;
        }

        RingElement(int number, RingElement.Color color) {
            this.number = number;
            this.color = color;
        }

        RingElement(RingElement original) {
            this.number = original.number;
            this.color = original.color;
        }

        public void changeColor() {
            color = color == Color.WHITE ? Color.YELLOW : Color.WHITE;
        }

        public int getNumber() {
            return number;
        }

        public Color getColor() {
            return color;
        }

        @Override
        public String toString() {
            char c = color == Color.WHITE ? 'W' : 'Y';
            return "[" + number + "|" + c + "]";
        }
    }
}
