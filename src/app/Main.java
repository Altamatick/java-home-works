package app;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== Демонстрація принципу Expert ===");

        User user = new User("Іван Петренко", "ivan.petrenko@email.com");
        System.out.println("Створено користувача: " + user);

        Address address = new Address("вул. Хрещатик, 1", "Київ", "Україна", "01001");
        System.out.println("Створено адресу: " + address);

        user.setAddress(address);
        System.out.println("\nАдресу встановлено для користувача.");

        System.out.println("\n=== Інформація про користувача ===");
        System.out.println(user.getFullInfo());

        System.out.println("\n=== Отримання адреси через User ===");
        Address userAddress = user.getAddress();
        if (userAddress != null) {
            System.out.println("Адреса користувача: " + userAddress);
        } else {
            System.out.println("Адреса не встановлена");
        }

        System.out.println("\n=== Зміна адреси ===");
        Address newAddress = new Address("вул. Володимирська, 10", "Київ", "Україна", "01030");
        user.setAddress(newAddress);
        System.out.println("Нова адреса: " + user.getAddress());

        System.out.println("\n=== Принцип Expert виконано ===");
        System.out.println("Клас User відповідає за управління своєю адресою");
        System.out.println("Адреса належить користувачу і доступна через його методи");
    }
}