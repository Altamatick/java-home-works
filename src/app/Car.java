package app;

public class Car implements Transport {
    @Override
    public void move() {
        System.out.println("Автомобіль їде по дорозі");
    }
}
