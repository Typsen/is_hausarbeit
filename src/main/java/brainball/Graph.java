package brainball;

import com.google.common.base.Preconditions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class Graph<T> {
    /**
     * A map of all nodes present in this graph.
     */
    private final Map<String, Node<T>> nodes = new HashMap<>();

    /**
     * A map of all Nodes and their adjacent nodes of this graph.
     * The first Key represents a node of the graph and it's value-map the neighbors.
     * The value-map's key represents a neighbor of the first key's node. The value-maps value represents all weights
     * for the given connection to the neighbor.
     */
    private final Map<Node<T>, Map<Node<T>,Float>> adjacencyList = new HashMap<>();

    /**
     * Adds a node to this graph and updates the adjacency list. If the node is already present in the graph nothing is done.
     * @param node The node to be added.
     */
    public void addNode(Node<T> node){
        Preconditions.checkNotNull(node);
        if(nodes.containsValue(node)) { return;}
        nodes.put(node.getId(),node);
        adjacencyList.put(node, new HashMap<>());
    }

    /**
     * Generates a node and adds a node to this graph and updates the adjacency list.
     * @param id The id of the node to be generated and added.
     * @return The generated node.
     */
    public Node<T> addNode(String id){
        Preconditions.checkNotNull(id);
        return addNode(id,null);
    }

    /**
     * Generates a node and adds a node to this graph and updates the adjacency list.
     * @param id The id of the node to be generated and added.
     * @param content The content of the node.
     * @return The generated node.
     */
    public Node<T> addNode(String id, T content){
        Preconditions.checkNotNull(id);

        if(nodes.containsKey(id)){ return nodes.get(id);}
        Node<T> node = new Node<>(id,content);
        nodes.put(id,node);
        adjacencyList.put(node,new HashMap<>());
        return node;
    }

    /**
     * Allows to add multiple nodes at once.
     * @param nodes List of nodes to be added
     */
    public void addNodes(List<Node<T>> nodes){
        Preconditions.checkNotNull(nodes);
        nodes.forEach(this::addNode);
    }

    /**
     * Allows to add multiple nodes at once.
     * @param nodes A map of Strings and T. The Strings will become the ids of the seperate nodes and the T's their content.
     */
    public void addNodes (Map<String,T> nodes){
        Preconditions.checkNotNull(nodes);
        nodes.forEach(this::addNode);
    }

    /**
     * Returns a node with the specified id, if present in the graph.
     * @param id The id of the node.
     * @return The node with the id, if present in the graph. Null otherwise.
     */
    public Node<T> getNode(String id){
        Preconditions.checkNotNull(id);
        return nodes.get(id);
    }

    /**
     * Returns the first node with the given content.
     * @param content The content to search within the nodes.
     * @return The first node with the given content or null if none is found.
     */
    public Node<T> getNodeByContent(T content){
        return nodeStream().filter(nodes -> nodes.getContent().equals(content)).findFirst().get();
    }

    /**
     * Adds a unidirectional neighbor to node.
     * @param nodeId The id of the node to add a neighbor to.
     * @param neighborId The id of the node to be added as neighbor.
     */
    public void addUniNeighbor(String nodeId, String neighborId){
        addUniNeighbor(nodeId,neighborId,0);
    }

    /**
     * Adds a unidirectional neighbor to node.
     * @param nodeId The id of the node to add a neighbor to.
     * @param neighborId The id of the node to be added as neighbor.
     * @param weight The weight of the connection between node and neighbor.
     */
    public void addUniNeighbor(String nodeId, String neighborId, float weight){
        Preconditions.checkNotNull(nodeId);
        Preconditions.checkNotNull(neighborId);

        Node<T> node = nodes.get(nodeId);
        Node<T> neighbor = nodes.get(neighborId);

        if(node == null) { node = addNode(nodeId);}
        if(neighbor == null) { neighbor = addNode(neighborId);}

        addUniNeighbor(node,neighbor,weight);
    }

    /**
     * Adds a unidirectional neighbor to node.
     * @param node The node to add a neighbor to.
     * @param neighbor The node to be added as neighbor.
     */
    public void addUniNeighbor(Node<T> node, Node<T> neighbor){
        addUniNeighbor(node,neighbor,0);
    }

    /**
     * Adds a unidirectional neighbor to node.
     * @param node The node to add a neighbor to.
     * @param neighbor The node to be added as neighbor.
     * @param weight The weight of the connection between node and neighbor.
     */
    public void addUniNeighbor(Node<T> node, Node<T> neighbor, float weight){
        Preconditions.checkNotNull(node);
        Preconditions.checkNotNull(neighbor);

        if(!nodes.containsValue(node)) { addNode(node);}
        if(!nodes.containsValue(neighbor)) { addNode(neighbor);}

        Map<Node<T>,Float> neighbors = adjacencyList.get(node);

        if(!neighbors.containsKey(neighbor)) { neighbors.put(neighbor,0F);}

        neighbors.put(neighbor,weight);
    }

    /**
     * Adds a bidirectional neighbor to node.
     * @param nodeId The id of the node to add a neighbor to.
     * @param neighborId The id of the node to be added as neighbor.
     */
    public void addBidiNeighbor(String nodeId, String neighborId) {
        addBidiNeighbor(nodeId,neighborId,0);
    }

    /**
     * Adds a bidirectional neighbor to node.
     * @param nodeId The id of the node to add a neighbor to.
     * @param neighborId The id of the node to be added as neighbor.
     * @param weight The weight of the connection between node and neighbor.
     */
    public void addBidiNeighbor(String nodeId, String neighborId, float weight) {
        Preconditions.checkNotNull(nodeId);
        Preconditions.checkNotNull(neighborId);

        Node<T> node = nodes.get(nodeId);
        Node<T> neighbor = nodes.get(neighborId);

        if(node == null) { node = addNode(nodeId);}
        if(neighbor == null) { neighbor = addNode(neighborId);}

        addBidiNeighbor(node,neighbor,weight);
    }

    /**
     * Adds a bidirectional neighbor to node.
     * @param node The node to add a neighbor to.
     * @param neighbor The node to be added as neighbor.
     */
    public void addBidiNeighbor(Node<T> node, Node<T> neighbor){
        addBidiNeighbor(node,neighbor,0);
    }

    /**
     * Adds a bidirectional neighbor to node.
     * @param node The node to add a neighbor to.
     * @param neighbor The node to be added as neighbor.
     * @param weight The weight of the connection between node and neighbor.
     */
    public void addBidiNeighbor(Node<T> node, Node<T> neighbor, float weight){
        Preconditions.checkNotNull(node);
        Preconditions.checkNotNull(neighbor);

        if(!nodes.containsValue(node)) { addNode(node);}
        if(!nodes.containsValue(neighbor)) { addNode(neighbor);}

        Map<Node<T>,Float> neighbors = adjacencyList.get(node);
        if(!neighbors.containsKey(neighbor)) { neighbors.put(neighbor,0F);}
        neighbors.put(neighbor,weight);

        addUniNeighbor(neighbor,node,weight);
    }

    /**
     * Checks if this node has a specific neighbor.
     * @param nodeId The id of the node to be checked.
     * @return True if the neighbor is present. False otherwise or if the node with nodeId is not in the graph.
     */
    public boolean hasNeighbor(String nodeId, String neighborId){
        Preconditions.checkNotNull(nodeId);
        Preconditions.checkNotNull(neighborId);

        Node<T> node = nodes.get(nodeId);
        Node<T> neighbor = nodes.get(neighborId);
        if(node == null) { return false;}
        if(neighbor == null) { return false;}

        return hasNeighbor(node,neighbor);
    }

    /**
     * Checks if this node has a specific neighbor.
     * @param node The node to be checked.
     * @param neighbor The neighbor to be checked.
     * @return True if the neighbor is present. False otherwise or if the node or neighbor are not in the graph.
     */
    public boolean hasNeighbor(Node<T> node, Node<T> neighbor){
        return adjacencyList.get(node).containsKey(neighbor);
    }

    /**
     * Removes a neighbor of node.
     * @param nodeId The id of the node whose neighbor is to be removed.
     * @param neighborId The id of the neighbor to be removed.
     * @return The removed neighbor.
     */
    public Node<T> removeNeighbor(String nodeId, String neighborId){
        Preconditions.checkNotNull(nodeId);
        Preconditions.checkNotNull(neighborId);

        Node<T> node = nodes.get(nodeId);
        Node<T> neighbor = nodes.get(neighborId);

        if(node == null) { return null;}
        if(neighbor == null) { return null;}

        return removeNeighbor(node,neighbor);
    }

    /**
     * Removes a neighbor of node
     * @param node The node whose neighbor is to be removed.
     * @param neighbor The neighbor to be removed
     * @return The removed neighbor.
     */
    public Node<T> removeNeighbor(Node<T> node, Node<T> neighbor){
        Preconditions.checkNotNull(node);
        Preconditions.checkNotNull(neighbor);

        if(!nodes.containsValue(node)){ return null;}
        if(!nodes.containsValue(neighbor)){ return null;}

        Map<Node<T>,Float> neighbors = adjacencyList.get(node);
        neighbors.remove(neighbor);

        return neighbor;
    }

    /**
     * Changes the weight to a neighbor of node.
     * @param nodeId The id of the node whose weight to it's neighbor is to be changed.
     * @param neighborId The id of the neighbor of node
     * @param weight The new weight of the connection
     */
    public void changeNeighborWeight(String nodeId, String neighborId, float weight){
        Preconditions.checkNotNull(nodeId);
        Preconditions.checkNotNull(neighborId);

        Node<T> node = nodes.get(nodeId);
        Node<T> neighbor = nodes.get(neighborId);

        if(node == null){ return;}
        if(neighbor == null){ return;}

        changeNeighborWeight(node,neighbor,weight);
    }

    /**
     * Changes the weight to a neighbor of node.
     * @param node The node whose weight to it's neighbor is to be changed.
     * @param neighbor The neighbor of node
     * @param weight The new weight of the connection
     */
    public void changeNeighborWeight(Node<T> node, Node<T> neighbor, float weight){
        Preconditions.checkNotNull(node);
        Preconditions.checkNotNull(neighbor);

        if(!nodes.containsValue(node)) { return;}
        if(!nodes.containsValue(neighbor)) { return;}

        Map<Node<T>,Float> neighbors = adjacencyList.get(node);
        if(!neighbors.containsKey(neighbor)) { return;}
        neighbors.put(neighbor,weight);
    }

    /**
     * Returns a stream of all nodes in the graph.
     * @return A stream of all nodes in the graph.
     */
    public Stream<Node<T>> nodeStream(){
        return nodes.values().stream();
    }

    /**
     * Returns a stream of all neighbors of a node.
     * @param nodeId The id of the node whose neighbors are to be returned.
     * @return A stream of the neighbors of node.
     */
    public Stream<Node<T>> neighborStream(String nodeId){
        Preconditions.checkNotNull(nodeId);

        Node<T> node = nodes.get(nodeId);
        if(node == null) { return Stream.empty();}
        return neighborStream(node);
    }

    /**
     * Returns a stream of all neighbors of a node.
     * @param node The node whose neighbors are to be returned.
     * @return A stream of the neighbors of node.
     */
    public Stream<Node<T>> neighborStream(Node<T> node){
        Preconditions.checkNotNull(node);

        if(!nodes.containsValue(node)) { return Stream.empty();}

        return adjacencyList.get(node).keySet().stream();
    }

    /**
     * Returns a List of the nodes in the graph.
     * @return A List of the nodes in the graph.
     */
    public List<Node<T>> getNodes(){
        return nodes.values().stream().toList();
    }

    /**
     * Returns a copy of all neighbors of a node to make it impossible to change the adjacency list from outside the class
     * @param nodeId The id of the node whose neighbors are to be returned.
     * @return A set of neighbors of the node, if present. Null otherwise.
     */
    public Map<Node<T>,Float> getNeighbors(String nodeId){
        Preconditions.checkNotNull(nodeId);

        Node<T> node = nodes.get(nodeId);
        if(node == null){ return null;}

        return getNeighbors(node);
    }

    /**
     * Returns a set of all neighbors of a node.
     * @param node The node whose neighbors are to be returned.
     * @return A set of neighbors of the node, if present. Null otherwise.
     */
    public Map<Node<T>,Float> getNeighbors(Node<T> node){
        Preconditions.checkNotNull(node);
        return adjacencyList.get(node);
    }

    /**
     * Returns the distance form node to neighbor.
     * @param nodeId The id of the starting node.
     * @param neighborId The id of the neighbor node.
     * @return The distance from node to neighbor, if present. Null otherwise.
     */
    public Float getWeightToNeighbor(String nodeId, String neighborId){
        Preconditions.checkNotNull(nodeId);
        Preconditions.checkNotNull(neighborId);

        Node<T> node = nodes.get(nodeId);
        Node<T> neighbor = nodes.get(neighborId);

        if(node == null) { return null;}
        if(neighbor == null) { return null;}

        return getWeightToNeighbor(node,neighbor);
    }

    /**
     * Returns the distance from node to neighbor.
     * @param node The starting node.
     * @param neighbor The neighbor node.
     * @return The distance from node to neighbor, if present. Null otherwise.
     */
    public Float getWeightToNeighbor(Node<T> node, Node<T> neighbor) {
        Preconditions.checkNotNull(node);
        Preconditions.checkNotNull(neighbor);

        return adjacencyList.get(node).get(neighbor);
    }

    /**
     * Returns a copy of the adjacency list to make it impossible to change the adjacency list from outside the class.
     *
     * @return A copy of the adjacency list.
     */
    public Map<Node<T>, Map<Node<T>,Float>> getAdjacencyList() {
        return Map.copyOf(adjacencyList);
    }
}
