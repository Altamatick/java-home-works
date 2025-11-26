package app;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Order {
    private Long id;
    private LocalDateTime creationDate;
    private Double totalCost;
    private List<Product> products;

    public Order() {
        this.products = new ArrayList<>();
    }

    public Order(Long id, LocalDateTime creationDate, Double totalCost, List<Product> products) {
        this.id = id;
        this.creationDate = creationDate;
        this.totalCost = totalCost;
        this.products = products != null ? products : new ArrayList<>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public Double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(Double totalCost) {
        this.totalCost = totalCost;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return Objects.equals(id, order.id) &&
               Objects.equals(creationDate, order.creationDate) &&
               Objects.equals(totalCost, order.totalCost) &&
               Objects.equals(products, order.products);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, creationDate, totalCost, products);
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", creationDate=" + creationDate +
                ", totalCost=" + totalCost +
                ", products=" + products +
                '}';
    }
}
