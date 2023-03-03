package toolbox;

import org.graphstream.graph.Node;

import java.util.Comparator;

public class NodeComparator implements Comparator<Node> {

    @Override
    public int compare(Node n1, Node n2) {
        return n1.getAttribute("label").toString().toLowerCase().compareTo(n2.getAttribute("label").toString().toLowerCase());
    }
}
