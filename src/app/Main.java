package app;

import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== Демонстрація алгоритмів сортування та пошуку ===\n");

        // 1. Створення та заповнення масиву випадковими числами
        int arraySize = 15;
        int minValue = 1;
        int maxValue = 100;

        int[] originalArray = ArrayUtils.generateRandomArray(arraySize, minValue, maxValue);
        ArrayUtils.printArray(originalArray, "Оригінальний масив");

        // 2. Створення копії для сортування (щоб зберегти оригінальний)
        int[] arrayToSort = Arrays.copyOf(originalArray, originalArray.length);

        // 3. Застосування сортування злиттям
        System.out.println("\n=== Сортування злиттям ===");
        long startTime = System.nanoTime();
        ArrayUtils.mergeSort(arrayToSort);
        long endTime = System.nanoTime();

        ArrayUtils.printArray(arrayToSort, "Відсортований масив");
        System.out.printf("Час сортування: %.3f мс\n", (endTime - startTime) / 1_000_000.0);

        // 4. Демонстрація бінарного пошуку
        System.out.println("\n=== Бінарний пошук ===");

        // Пошук існуючих елементів
        int[] searchTargets = {arrayToSort[0], arrayToSort[arrayToSort.length / 2], arrayToSort[arrayToSort.length - 1]};
        binarySearch(searchTargets, arrayToSort);

        // Пошук неіснуючих елементів
        int[] nonExistentTargets = {0, 150, -5};
        System.out.println("\nПошук неіснуючих елементів:");
        binarySearch(nonExistentTargets, arrayToSort);

        // 5. Порівняння з вбудованими методами Java
        System.out.println("\n=== Порівняння з вбудованими методами ===");

        int[] javaArray = Arrays.copyOf(originalArray, originalArray.length);
        startTime = System.nanoTime();
        Arrays.sort(javaArray);
        endTime = System.nanoTime();

        System.out.printf("Час вбудованого сортування Java: %.3f мс\n", (endTime - startTime) / 1_000_000.0);

        // Перевірка, чи результати однакові
        boolean arraysEqual = Arrays.equals(arrayToSort, javaArray);
        System.out.printf("Результати сортування однакові: %s\n", arraysEqual);

        // Порівняння бінарного пошуку
        int testTarget = arrayToSort[5];
        int ourResult = ArrayUtils.binarySearch(arrayToSort, testTarget);
        int javaResult = Arrays.binarySearch(javaArray, testTarget);

        System.out.printf("Наш бінарний пошук знайшов %d на позиції: %d\n", testTarget, ourResult);
        System.out.printf("Java бінарний пошук знайшов %d на позиції: %d\n", testTarget, javaResult);

        // 6. Додаткова демонстрація з різними розмірами масивів
        System.out.println("\n=== Тестування з різними розмірами масивів ===");

        int[] sizes = {10, 100, 1000};
        for (int size : sizes) {
            int[] testArray = ArrayUtils.generateRandomArray(size, 1, 1000);

            startTime = System.nanoTime();
            ArrayUtils.mergeSort(testArray);
            endTime = System.nanoTime();

            double sortTime = (endTime - startTime) / 1_000_000.0;
            System.out.printf("Масив розміром %d елементів відсортовано за %.3f мс\n", size, sortTime);

            // Перевірка правильності сортування
            boolean isSorted = isSorted(testArray);
            System.out.printf("Масив правильно відсортований: %s\n", isSorted);
        }

        System.out.println("\n=== Демонстрація завершена ===");
    }

    private static void binarySearch(int[] targets, int[] arr) {
        for (int target : targets) {
            int index = ArrayUtils.binarySearch(arr, target);
            if (index != -1) {
                System.out.printf("Знайдено %d на позиції %d\n", target, index);
            } else {
                System.out.printf("Елемент %d не знайдено (як і очікувалось)\n", target);
            }
        }
    }

    private static boolean isSorted(int[] array) {
        for (int i = 1; i < array.length; i++) {
            if (array[i] < array[i - 1]) {
                return false;
            }
        }
        return true;
    }
}
