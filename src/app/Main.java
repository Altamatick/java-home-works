package app;

public class Main {

    public static void main(String[] args) {

        DataProvider provider = new DataProvider();
        DataHandler<String> stringHandler = new DataHandler<>();
        DataHandler<Double> doubleHandler = new DataHandler<>();

        String namesOutput = stringHandler.handleData(provider.getProductNames());
        getOutput("Products: " + namesOutput);

        String salesOutput = doubleHandler.handleData(provider.getSalesAmounts());
        getOutput("Sales, EUR: " + salesOutput);
    }

    private static void getOutput(String output) {
        System.out.println(output);
    }
}
