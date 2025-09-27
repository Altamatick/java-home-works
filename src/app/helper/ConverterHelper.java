package app.helper;

public class ConverterHelper {
    private static final double CONV_K = 2.20462;
    private static final double MILES_TO_KM = 1.60934;
    private static final double KM_TO_MILES = 1 / MILES_TO_KM;

    public static double milesToKm(double miles) {
        return miles * MILES_TO_KM;
    }

    public static double kmToMiles(double km) {
        return km * KM_TO_MILES;
    }

    private static double convKgsToPounds(double kgs) {
        return kgs * CONV_K;
    }

    private static double convPoundsToKgs(double pnds) {
        return pnds / CONV_K;
    }
}

