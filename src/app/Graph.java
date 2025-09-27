package app;

import java.util.*;

public class Graph {
    private final Map<Integer, Set<Integer>> adjacencyList;

    public Graph() {
        this.adjacencyList = new HashMap<>();
    }

    public void addVertex(int vertex) {
        adjacencyList.putIfAbsent(vertex, new HashSet<>());
    }

    public void addEdge(int source, int destination) {
        addVertex(source);
        addVertex(destination);

        adjacencyList.get(source).add(destination);
        adjacencyList.get(destination).add(source);
    }

    public void removeVertex(int vertex) {
        if (!hasVertex(vertex)) {
            return;
        }

        for (int neighbor : adjacencyList.get(vertex)) {
            adjacencyList.get(neighbor).remove(vertex);
        }

        adjacencyList.remove(vertex);
    }

    public void removeEdge(int source, int destination) {
        if (hasVertex(source) && hasVertex(destination)) {
            adjacencyList.get(source).remove(destination);
            adjacencyList.get(destination).remove(source);
        }
    }

    public boolean hasVertex(int vertex) {
        return adjacencyList.containsKey(vertex);
    }

    public boolean hasEdge(int source, int destination) {
        return hasVertex(source) && hasVertex(destination) &&
               adjacencyList.get(source).contains(destination);
    }

    public int getVertexCount() {
        return adjacencyList.size();
    }

    public int getEdgeCount() {
        int edgeCount = 0;
        for (Set<Integer> neighbors : adjacencyList.values()) {
            edgeCount += neighbors.size();
        }
        return edgeCount / 2;
    }

    public boolean isConnected() {
        if (adjacencyList.isEmpty()) {
            return true;
        }

        Set<Integer> visited = new HashSet<>();
        Queue<Integer> queue = new LinkedList<>();

        int startVertex = adjacencyList.keySet().iterator().next();
        queue.offer(startVertex);
        visited.add(startVertex);

        while (!queue.isEmpty()) {
            int current = queue.poll();

            for (int neighbor : adjacencyList.get(current)) {
                if (!visited.contains(neighbor)) {
                    visited.add(neighbor);
                    queue.offer(neighbor);
                }
            }
        }

        return visited.size() == getVertexCount();
    }

    public void printGraph() {
        System.out.println("Граф:");
        for (Map.Entry<Integer, Set<Integer>> entry : adjacencyList.entrySet()) {
            System.out.printf("Вершина %d: %s%n", entry.getKey(), entry.getValue());
        }
        System.out.printf("Кількість вершин: %d%n", getVertexCount());
        System.out.printf("Кількість ребер: %d%n", getEdgeCount());
        System.out.printf("Граф зв'язний: %s%n", isConnected() ? "Так" : "Ні");
    }
}
