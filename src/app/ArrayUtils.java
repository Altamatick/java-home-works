package app;

import java.util.Arrays;
import java.util.Random;

public class ArrayUtils {

    @MethodInfo(name = "mergeSort", returnType = "void", description = "Сортує масив методом злиття")
    @Author(firstName = "Денис", lastName = "Гаркуша")
    public static void mergeSort(int[] array) {
        if (array.length <= 1) {
            return;
        }
        mergeSortHelper(array, 0, array.length - 1);
    }

    private static void mergeSortHelper(int[] array, int left, int right) {
        if (left < right) {
            int mid = left + (right - left) / 2;
            mergeSortHelper(array, left, mid);
            mergeSortHelper(array, mid + 1, right);
            merge(array, left, mid, right);
        }
    }

    private static void merge(int[] array, int left, int mid, int right) {
        int[] temp = new int[right - left + 1];
        int i = left, j = mid + 1, k = 0;

        while (i <= mid && j <= right) {
            if (array[i] <= array[j]) {
                temp[k++] = array[i++];
            } else {
                temp[k++] = array[j++];
            }
        }

        while (i <= mid) {
            temp[k++] = array[i++];
        }

        while (j <= right) {
            temp[k++] = array[j++];
        }

        System.arraycopy(temp, 0, array, left, temp.length);
    }

    @MethodInfo(name = "binarySearch", returnType = "int", description = "Виконує бінарний пошук елемента в відсортованому масиві")
    @Author(firstName = "Денис", lastName = "Гаркуша")
    public static int binarySearch(int[] array, int target) {
        int left = 0;
        int right = array.length - 1;

        while (left <= right) {
            int mid = left + (right - left) / 2;

            if (array[mid] == target) {
                return mid;
            }

            if (array[mid] < target) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }

        return -1;
    }

    @MethodInfo(name = "generateRandomArray", returnType = "int[]", description = "Генерує масив випадкових чисел заданого розміру")
    @Author(firstName = "Денис", lastName = "Гаркуша")
    public static int[] generateRandomArray(int size, int min, int max) {
        Random random = new Random();
        int[] array = new int[size];

        for (int i = 0; i < size; i++) {
            array[i] = random.nextInt(max - min + 1) + min;
        }

        return array;
    }

    @MethodInfo(name = "printArray", returnType = "void", description = "Виводить елементи масиву в консоль")
    @Author(firstName = "Денис", lastName = "Гаркуша")
    public static void printArray(int[] array) {
        System.out.println(Arrays.toString(array));
    }

    @MethodInfo(name = "findMax", returnType = "int", description = "Знаходить максимальний елемент в масиві")
    @Author(firstName = "Денис", lastName = "Гаркуша")
    public static int findMax(int[] array) {
        if (array.length == 0) {
            throw new IllegalArgumentException("Масив не може бути порожнім");
        }

        int max = array[0];
        for (int i = 1; i < array.length; i++) {
            if (array[i] > max) {
                max = array[i];
            }
        }

        return max;
    }

    @MethodInfo(name = "findMin", returnType = "int", description = "Знаходить мінімальний елемент в масиві")
    @Author(firstName = "Денис", lastName = "Гаркуша")
    public static int findMin(int[] array) {
        if (array.length == 0) {
            throw new IllegalArgumentException("Масив не може бути порожнім");
        }

        int min = array[0];
        for (int i = 1; i < array.length; i++) {
            if (array[i] < min) {
                min = array[i];
            }
        }

        return min;
    }
}
