package brainball;

public class Node<T>{
    private final String id;
    private T content;

    public Node(String id, T content) {
        this.id = id;
        this.content = content;
    }

    public String getId() {
        return id;
    }

    public T getContent() {
        return content;
    }

    public void setContent(T content){
        this.content = content;
    }
}
