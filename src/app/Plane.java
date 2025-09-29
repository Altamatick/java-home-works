package app;

public class Plane implements Transport {
    @Override
    public void move() {
        System.out.println("Літак летить у небі");
    }
}
