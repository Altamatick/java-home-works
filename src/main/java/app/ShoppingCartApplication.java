package app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.util.List;
import java.util.Scanner;

@SpringBootApplication
public class ShoppingCartApplication implements CommandLineRunner {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ApplicationContext applicationContext;

    private Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        SpringApplication.run(ShoppingCartApplication.class, args);
    }

    @Override
    public void run(String... args) {
        System.out.println("=== Онлайн-магазин: Керування кошиком ===\n");
        
        Cart cart = applicationContext.getBean(Cart.class);
        
        while (true) {
            printMenu();
            String choice = scanner.nextLine().trim();
            
            // Пропускаємо порожній ввід
            if (choice.isEmpty()) {
                continue;
            }
            
            switch (choice) {
                case "1":
                    showAllProducts();
                    break;
                case "2":
                    addProductToCart(cart);
                    break;
                case "3":
                    removeProductFromCart(cart);
                    break;
                case "4":
                    showCart(cart);
                    break;
                case "5":
                    createNewCart();
                    cart = applicationContext.getBean(Cart.class);
                    System.out.println("Створено новий кошик\n");
                    break;
                case "0":
                    System.out.println("До побачення!");
                    return;
                default:
                    System.out.println("Невірний вибір '" + choice + "'. Спробуйте ще раз.\n");
            }
        }
    }

    private void printMenu() {
        System.out.println("Оберіть дію:");
        System.out.println("1 - Показати всі товари");
        System.out.println("2 - Додати товар до кошика");
        System.out.println("3 - Видалити товар з кошика");
        System.out.println("4 - Показати кошик");
        System.out.println("5 - Створити новий кошик");
        System.out.println("0 - Вийти");
        System.out.print("Ваш вибір: ");
        System.out.flush();
    }

    private void showAllProducts() {
        System.out.println("\n=== Всі товари ===");
        List<Product> products = productRepository.findAll();
        if (products.isEmpty()) {
            System.out.println("Товарів немає");
        } else {
            for (Product product : products) {
                System.out.println(product);
            }
        }
        System.out.println();
    }

    private void addProductToCart(Cart cart) {
        System.out.print("\nВведіть ID товару для додавання: ");
        System.out.flush();
        try {
            String input = scanner.nextLine().trim();
            if (input.isEmpty()) {
                System.out.println("ID не може бути порожнім.");
                System.out.println();
                return;
            }
            Long id = Long.parseLong(input);
            cart.addProductById(id);
        } catch (NumberFormatException e) {
            System.out.println("Невірний формат ID. Введіть число.");
        }
        System.out.println();
    }

    private void removeProductFromCart(Cart cart) {
        if (cart.isEmpty()) {
            System.out.println("\nКошик порожній. Немає що видаляти.\n");
            return;
        }
        
        System.out.print("\nВведіть ID товару для видалення: ");
        System.out.flush();
        try {
            String input = scanner.nextLine().trim();
            if (input.isEmpty()) {
                System.out.println("ID не може бути порожнім.");
                System.out.println();
                return;
            }
            Long id = Long.parseLong(input);
            cart.removeProductById(id);
        } catch (NumberFormatException e) {
            System.out.println("Невірний формат ID. Введіть число.");
        }
        System.out.println();
    }

    private void showCart(Cart cart) {
        System.out.println("\n" + cart + "\n");
    }

    private void createNewCart() {
        System.out.println("\nСтворюється новий кошик...");
    }
}

