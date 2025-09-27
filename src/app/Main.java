package app;

import java.lang.reflect.Method;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== Демонстрація роботи з анотаціями ===\n");

        // Демонстрація роботи методів ArrayUtils
        demonstrateArrayUtils();

        // Отримання інформації про анотації методів
        System.out.println("\n=== Інформація про анотації методів ===\n");
        displayMethodAnnotations();
    }

    private static void demonstrateArrayUtils() {
        System.out.println("1. Генерація випадкового масиву:");
        int[] array = ArrayUtils.generateRandomArray(10, 1, 100);
        System.out.print("Випадковий масив: ");
        ArrayUtils.printArray(array);

        System.out.println("\n2. Пошук мін/макс елементів:");
        System.out.println("Мінімальний елемент: " + ArrayUtils.findMin(array));
        System.out.println("Максимальний елемент: " + ArrayUtils.findMax(array));

        System.out.println("\n3. Сортування масиву:");
        System.out.print("До сортування: ");
        ArrayUtils.printArray(array);
        ArrayUtils.mergeSort(array);
        System.out.print("Після сортування: ");
        ArrayUtils.printArray(array);

        System.out.println("\n4. Бінарний пошук:");
        int target = array[array.length / 2];
        int index = ArrayUtils.binarySearch(array, target);
        System.out.println("Пошук елемента " + target + ": знайдено на позиції " + index);
    }

    private static void displayMethodAnnotations() {
        Class<?> clazz = ArrayUtils.class;
        Method[] methods = clazz.getDeclaredMethods();

        int annotatedMethodCount = 0;

        for (Method method : methods) {
            if (method.isAnnotationPresent(MethodInfo.class) &&
                method.isAnnotationPresent(Author.class)) {

                annotatedMethodCount++;

                MethodInfo methodInfo = method.getAnnotation(MethodInfo.class);
                Author author = method.getAnnotation(Author.class);

                System.out.println("┌─────────────────────────────────────────┐");
                System.out.printf("│ Метод #%-2d                               │%n", annotatedMethodCount);
                System.out.println("├─────────────────────────────────────────┤");
                System.out.printf("│ Назва: %-32s │%n", methodInfo.name());
                System.out.printf("│ Тип повернення: %-23s │%n", methodInfo.returnType());
                System.out.printf("│ Опис: %-34s│%n", truncateDescription(methodInfo.description()));
                System.out.printf("│ Автор: %-7s %-22s   │%n", author.firstName(), author.lastName());
                System.out.printf("│ Java метод: %-27s │%n", method.getName());
                System.out.printf("│ Параметри: %-28s │%n", getParametersString(method));
                System.out.println("└─────────────────────────────────────────┘");
                System.out.println();
            }
        }

        System.out.println("Загальна кількість анотованих методів: " + annotatedMethodCount);
    }

    private static String truncateDescription(String description) {
        if (description.length() > 34) {
            return description.substring(0, 31) + "...";
        }
        return description;
    }

    private static String getParametersString(Method method) {
        Class<?>[] paramTypes = method.getParameterTypes();
        if (paramTypes.length == 0) {
            return "немає";
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < paramTypes.length; i++) {
            if (i > 0) sb.append(", ");
            sb.append(paramTypes[i].getSimpleName());
        }

        String result = sb.toString();
        if (result.length() > 28) {
            return result.substring(0, 25) + "...";
        }
        return result;
    }
}
