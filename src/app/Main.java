package app;

import com.example.PasswordGenerator;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("=== Додаток для генерації паролів ===");
        System.out.println("Використовує бібліотеку Password Generator v1.0");
        System.out.println();

        while (true) {
            System.out.println("Меню:");
            System.out.println("1. Згенерувати пароль");
            System.out.println("2. Згенерувати кілька паролів");
            System.out.println("3. Вийти");
            System.out.print("Виберіть опцію (1-3): ");

            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    generateSinglePassword(scanner);
                    break;
                case 2:
                    generateMultiplePasswords(scanner);
                    break;
                case 3:
                    System.out.println("До побачення!");
                    scanner.close();
                    return;
                default:
                    System.out.println("Невірний вибір. Спробуйте ще раз.");
            }
            System.out.println();
        }
    }

    private static void generateSinglePassword(Scanner scanner) {
        System.out.print("Введіть довжину пароля (мінімум 1): ");
        int length = scanner.nextInt();

        if (length < 1) {
            System.out.println("Помилка: довжина пароля має бути більше 0");
            return;
        }

        try {
            String password = PasswordGenerator.generatePassword(length);
            System.out.println("Згенерований пароль: " + password);
            System.out.println("Довжина: " + password.length() + " символів");

            System.out.println("Перевірка випадковості - ще один пароль тієї ж довжини:");
            String password2 = PasswordGenerator.generatePassword(length);
            System.out.println("Другий пароль: " + password2);
            System.out.println("Паролі однакові: " + password.equals(password2));
        } catch (IllegalArgumentException e) {
            System.out.println("Помилка: " + e.getMessage());
        }
    }

    private static void generateMultiplePasswords(Scanner scanner) {
        System.out.print("Введіть довжину пароля: ");
        int length = scanner.nextInt();
        System.out.print("Введіть кількість паролів: ");
        int count = scanner.nextInt();

        if (length < 1) {
            System.out.println("Помилка: довжина пароля має бути більше 0");
            return;
        }

        if (count < 1 || count > 20) {
            System.out.println("Помилка: кількість паролів має бути від 1 до 20");
            return;
        }

        try {
            System.out.println("Згенеровані паролі:");
            for (int i = 1; i <= count; i++) {
                String password = PasswordGenerator.generatePassword(length);
                System.out.printf("%2d. %s%n", i, password);
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Помилка: " + e.getMessage());
        }
    }
}