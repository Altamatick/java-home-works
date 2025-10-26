package coffee.order;

import java.util.Objects;

/**
 * Клас, що представляє замовлення у кав'ярні.
 * Містить номер замовлення та ім'я замовника.
 */
public class Order {
    private final int number;
    private final String name;

    /**
     * Конструктор для створення замовлення.
     *
     * @param number номер замовлення
     * @param name   ім'я замовника
     */
    public Order(int number, String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Ім'я замовника не може бути порожнім");
        }
        if (number <= 0) {
            throw new IllegalArgumentException("Номер замовлення має бути додатним числом");
        }
        this.number = number;
        this.name = name.trim();
    }

    /**
     * Отримати номер замовлення.
     *
     * @return номер замовлення
     */
    public int getNumber() {
        return number;
    }

    /**
     * Отримати ім'я замовника.
     *
     * @return ім'я замовника
     */
    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return number == order.number && Objects.equals(name, order.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(number, name);
    }

    @Override
    public String toString() {
        return "Order{" +
                "number=" + number +
                ", name='" + name + '\'' +
                '}';
    }
}

