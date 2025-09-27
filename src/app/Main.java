package app;

import java.util.function.Function;
import java.util.function.Supplier;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== Functional Programming Demo ===\n");

        // 1. Анонімний клас для MathOperation (додавання)
        MathOperation addition = new MathOperation() {
            @Override
            public int operate(int a, int b) {
                return a + b;
            }
        };

        int result1 = addition.operate(15, 25);
        System.out.println("1. Anonymous class (addition): 15 + 25 = " + result1);

        // 2. Лямбда-вираз для StringManipulator (перетворення у верхній регістр)
        StringManipulator toUpperCase = input -> input.toUpperCase();

        String testString = "Hello World!";
        String result2 = toUpperCase.manipulate(testString);
        System.out.println("2. Lambda expression (toUpperCase): '" + testString + "' -> '" + result2 + "'");

        // 3. Посилання на метод для підрахунку великих літер
        Function<String, Integer> uppercaseCounter = StringListProcessor::countUppercase;

        String testString2 = "Hello JAVA Programming!";
        Integer result3 = uppercaseCounter.apply(testString2);
        System.out.println("3. Method reference (countUppercase): '" + testString2 + "' has " + result3 + " uppercase letters");

        // 4. Supplier для генерації випадкових чисел від 1 до 100
        Supplier<Integer> randomSupplier = () -> RandomNumberGenerator.generateRandomNumber(1, 100);

        System.out.println("4. Supplier (random numbers 1-100):");
        for (int i = 0; i < 5; i++) {
            Integer randomNumber = randomSupplier.get();
            System.out.println("   Random number " + (i + 1) + ": " + randomNumber);
        }

        // 5. Додаткові демонстрації
        System.out.println("\n=== Additional Demonstrations ===");

        // Інші математичні операції через лямбда-вирази
        MathOperation multiplication = (a, b) -> a * b;
        MathOperation subtraction = (a, b) -> a - b;

        System.out.println("Multiplication: 8 * 7 = " + multiplication.operate(8, 7));
        System.out.println("Subtraction: 20 - 12 = " + subtraction.operate(20, 12));

        // Інші строкові маніпуляції
        StringManipulator toLowerCase = String::toLowerCase;
        StringManipulator reverse = input -> new StringBuilder(input).reverse().toString();

        String sample = "Programming";
        System.out.println("Original: " + sample);
        System.out.println("To lower: " + toLowerCase.manipulate(sample));
        System.out.println("Reversed: " + reverse.manipulate(sample));

        // Тестування RandomNumberGenerator з різними діапазонами
        System.out.println("\nRandom numbers in different ranges:");
        System.out.println("Range 10-20: " + RandomNumberGenerator.generateRandomNumber(10, 20));
        System.out.println("Range 50-60: " + RandomNumberGenerator.generateRandomNumber(50, 60));
    }
}
