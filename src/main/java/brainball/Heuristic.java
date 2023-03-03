package brainball;

@FunctionalInterface
public interface Heuristic<T> {
    int getHeuristic(T o);
}
