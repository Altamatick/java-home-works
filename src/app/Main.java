package app;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== Демонстрація роботи з графом ===\n");

        Graph graph = new Graph();

        System.out.println("1. Створення графу та додавання вершин:");
        graph.addVertex(1);
        graph.addVertex(2);
        graph.addVertex(3);
        graph.addVertex(4);
        System.out.println("Додано вершини: 1, 2, 3, 4");

        System.out.println("\n2. Перевірка існування вершин:");
        System.out.println("Вершина 1 існує: " + graph.hasVertex(1));
        System.out.println("Вершина 2 існує: " + graph.hasVertex(2));
        System.out.println("Вершина 5 існує: " + graph.hasVertex(5));

        System.out.println("\n3. Додавання ребер:");
        graph.addEdge(1, 2);
        graph.addEdge(2, 3);
        graph.addEdge(3, 4);
        graph.addEdge(1, 4);
        System.out.println("Додано ребра: (1-2), (2-3), (3-4), (1-4)");

        System.out.println("\n4. Перевірка існування ребер:");
        System.out.println("Ребро (1-2) існує: " + graph.hasEdge(1, 2));
        System.out.println("Ребро (1-3) існує: " + graph.hasEdge(1, 3));
        System.out.println("Ребро (2-4) існує: " + graph.hasEdge(2, 4));

        System.out.println("\n5. Стан графу:");
        graph.printGraph();

        System.out.println("\n6. Видалення ребра (1-4):");
        graph.removeEdge(1, 4);
        System.out.println("Ребро (1-4) після видалення існує: " + graph.hasEdge(1, 4));

        System.out.println("\n7. Стан графу після видалення ребра:");
        graph.printGraph();

        System.out.println("\n8. Видалення вершини 3:");
        graph.removeVertex(3);
        System.out.println("Вершина 3 після видалення існує: " + graph.hasVertex(3));

        System.out.println("\n9. Фінальний стан графу:");
        graph.printGraph();

        System.out.println("\n=== Завдання виконано успішно! ===");
    }
}
