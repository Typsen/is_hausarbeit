package brainball;

import javafx.util.Pair;

import java.util.Map;

public class IDAStar<T extends Solvable & Expandable<T>> {

    public static boolean EXPAND_GRAPH = false;
    public static boolean LOGGING = false;
    private Graph<T> graph;
    private Heuristic<T> heuristic;


    public IDAStar(Graph<T> graph, Heuristic<T> heuristic){
        this.graph = graph;
        this.heuristic = heuristic;
    }

    public Node<T> solve(Node<T> root){
        Node<T> solutionNode = null;
        int bound = heuristic.getHeuristic(root.getContent());
        // Faktor 10 ist willkürlich gewählt
        int maxBound = bound * 10;

        while(solutionNode == null){
            if(LOGGING) {
                System.err.println("Search| Node: "+root.getId()+" g: 0"+" Bound: "+bound);
            }
            Pair<Node<T>,Integer> result = search(root,0,bound);

            if(result.getKey()!=null){
                solutionNode = result.getKey();
                continue;
            }

            if(result.getValue() >= maxBound){
                return null;
            }

            bound = result.getValue();

            if(EXPAND_GRAPH) {
                graph = new Graph<>();
                graph.addNode(root);
            }
        }
        return solutionNode;
    }

    private Pair<Node<T>,Integer> search(Node<T> node, int g, int bound){
        int f = g + heuristic.getHeuristic(node.getContent());
        System.out.println("Configuration: " + node.getContent().toString() + " Heuristic: " + heuristic.getHeuristic(node.getContent()));

        if(f > bound) {
            return new Pair<>(null,f);
        }

        if(node.getContent().isSolved()){
            return new Pair<>(node,Integer.MAX_VALUE);
        }

        if(EXPAND_GRAPH) { node.getContent().expand(graph,node);}

        int min = Integer.MAX_VALUE;
        Map<Node<T>,Float> children = graph.getNeighbors(node);

        for (Node<T> child: children.keySet()) {
            int gToChild = g + Math.round(graph.getWeightToNeighbor(node,child));
            if(LOGGING) {
                System.out.println("Search| Node: "+child.getId()+" g: "+gToChild+" Bound: "+bound);
            }
            Pair<Node<T>,Integer> result = search(child,gToChild,bound);
            if (result.getKey() != null){
                return result;
            }
            if(result.getValue() < min){
                min = result.getValue();
            }
        }

        return new Pair<>(null, min);
    }
}
