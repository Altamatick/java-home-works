package app;

import java.util.Scanner;
import app.helper.ConverterHelper;
import app.helper.ProjectInfoHelper;

public class Main {
    public static void main(String[] args) {
        String version = "1.1";
        String date = "September 27, 2025";
        System.out.println(ProjectInfoHelper.getProjectInfo(version, date));

        Scanner scanner = new Scanner(System.in);
        System.out.println("Choose conversion:");
        System.out.println("1 - Miles to Kilometers");
        System.out.println("2 - Kilometers to Miles");
        System.out.println("3 - Fahrenheit to Celsius");
        System.out.println("4 - Celsius to Fahrenheit");
        System.out.print("Enter 1, 2, 3 or 4: ");
        int choice = scanner.nextInt();

        if (choice == 1) {
            System.out.print("Enter miles: ");
            double miles = scanner.nextDouble();
            double kilometers = ConverterHelper.milesToKm(miles);
            System.out.println(miles + " miles = " + kilometers + " km");
            return;
        }
        if (choice == 2) {
            System.out.print("Enter kilometers: ");
            double kms = scanner.nextDouble();
            double milesResult = ConverterHelper.kmToMiles(kms);
            System.out.println(kms + " km = " + milesResult + " miles");
            return;
        }
        if (choice == 3) {
            System.out.print("Enter Fahrenheit: ");
            double fahrenheit = scanner.nextDouble();
            double celsius = ConverterHelper.fahrenheitToCelsius(fahrenheit);
            System.out.println(fahrenheit + " F = " + celsius + " C");
            return;
        }
        if (choice == 4) {
            System.out.print("Enter Celsius: ");
            double celsius = scanner.nextDouble();
            double fahrenheit = ConverterHelper.celsiusToFahrenheit(celsius);
            System.out.println(celsius + " C = " + fahrenheit + " F");
            return;
        }
        System.out.println("Invalid choice.");
    }
}
