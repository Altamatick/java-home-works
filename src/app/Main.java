package app;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Comparator;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        // Створення списку об'єктів класу Product
        List<Product> products = Arrays.asList(
                new Product("Laptop", "Electronics", 1200.0),
                new Product("Coffee Maker", "Appliances", 80.0),
                new Product("Headphones", "Electronics", 150.0),
                new Product("Blender", "Appliances", 50.0),
                new Product("Smartphone", "Electronics", 800.0),
                new Product("Microwave", "Appliances", 120.0),
                new Product("T-shirt", "Clothing", 25.0),
                new Product("Jeans", "Clothing", 60.0)
        );

        System.out.println("=== Всі продукти ===");
        products.forEach(System.out::println);

        // Групування продуктів за категоріями та обчислення середньої ціни
        Map<String, Double> categoryAverages = products.stream()
                .collect(Collectors.groupingBy(Product::getCategory,
                        Collectors.averagingDouble(Product::getPrice)));

        System.out.println("\n=== Середня ціна по категоріях ===");
        categoryAverages.forEach((category, avgPrice) ->
                System.out.printf("%s: %.2f\n", category, avgPrice));

        // Знаходження категорії з найвищою середньою ціною
        var maxEntry = categoryAverages.entrySet().stream()
                .max(Map.Entry.comparingByValue()); // один прохід O(n)

        String categoryWithHighestAvgPrice = maxEntry
                .map(Map.Entry::getKey)
                .orElse("Немає даних");

        double highestAvgPrice = maxEntry
                .map(Map.Entry::getValue)
                .orElse(0.0);

        System.out.println("\n=== Результат ===");
        System.out.printf("Категорія з найвищою середньою ціною: %s (%.2f)\n",
                categoryWithHighestAvgPrice, highestAvgPrice);

        // Додаткова інформація: групування продуктів за категоріями
        System.out.println("\n=== Продукти по категоріях ===");
        Map<String, List<Product>> productsByCategory = products.stream()
                .collect(Collectors.groupingBy(Product::getCategory));

        productsByCategory.forEach((category, productList) -> {
            System.out.println(category + ":");
            productList.forEach(product -> System.out.println("  " + product));
        });
    }
}
