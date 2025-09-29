package app;

public class Client {
    public void run() {
        System.out.println("=== Демонстрація паттерну Фабричний метод ===");

        // Створюємо фабрики
        TransportFactory carFactory = new CarFactory();
        TransportFactory planeFactory = new PlaneFactory();

        // Створюємо транспортні засоби через фабрики
        Transport car = carFactory.createTransport();
        Transport plane = planeFactory.createTransport();

        // Перевіряємо типи створених об'єктів
        System.out.println("Створено об'єкт типу: " + car.getClass().getSimpleName());
        System.out.println("Створено об'єкт типу: " + plane.getClass().getSimpleName());

        // Викликаємо методи руху
        System.out.println("\nВиконання методів руху:");
        car.move();
        plane.move();

        // Додаткова перевірка правильності роботи
        System.out.println("\n=== Перевірка правильності роботи ===");
        System.out.println("CarFactory створює Car: " + (car instanceof Car));
        System.out.println("PlaneFactory створює Plane: " + (plane instanceof Plane));
    }
}
