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
        System.out.print("Enter 1 or 2: ");
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
        System.out.println("Invalid choice.");
    }
}
