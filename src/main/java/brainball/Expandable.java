package brainball;

@FunctionalInterface
public interface Expandable<T> {
    Graph<T> expand(Graph<T> graph, Node<T> node);
}
