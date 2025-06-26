package app;

public class Main {
    private static final double CONV_K = 2.20462;

    public static void main(String[] args) {
        System.out.println("Converter App.");
        double kgs = 5;
        System.out.println("App for measures converting.");
        double pounds = convKgsToPounds(kgs);
        System.out.println("Result is" + pounds + " pounds.");
    }

    private static double convKgsToPounds(double kgs) {
        return kgs * CONV_K;
    }
}