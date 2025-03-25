package model;
import java.time.LocalDate;

public class Product {
    private int id;
    private String name;
    private double price;
    private int quantity;
    private LocalDate expiryDate;

    public Product(int id, String name, double price, int quantity, LocalDate expiryDate) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.expiryDate = expiryDate;
    }

    public boolean isExpired() {
        return expiryDate.isBefore(LocalDate.now());
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public double getPrice() { return price; }
    public int getQuantity() { return quantity; }
    public LocalDate getExpiryDate() { return expiryDate; }

    public void setQuantity(int quantity) { this.quantity = quantity; }
    
    // 🔹 FIX: Ensure price can be modified
    public void setPrice(double price) { this.price = price; }
}