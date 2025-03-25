package model;
import java.util.ArrayList;
import java.util.List;

public class Inventory {
    private List<Product> products = new ArrayList<>();

    public void addProduct(Product p) {
        products.add(p);
    }

    public void removeProduct(int id) {
        products.removeIf(p -> p.getId() == id);
    }

    public void updateQuantity(int id, int newQuantity) {
        for (Product p : products) {
            if (p.getId() == id) {
                p.setQuantity(newQuantity);
                break;
            }
        }
    }

    public Product getProductById(int id) {
        return products.stream().filter(p -> p.getId() == id).findFirst().orElse(null);
    }

    public List<Product> getAllProducts() { return products; }
    public Product getProductByName(String name) {
        return products.stream().filter(p -> p.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }
}