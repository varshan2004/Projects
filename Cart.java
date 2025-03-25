package model;
import java.util.HashMap;
import java.util.Map;

public class Cart {
    private Map<Product, Integer> cartItems = new HashMap<>();

    public void addToCart(Product product, int qty) {
        if (!product.isExpired() && product.getQuantity() >= qty) {
            cartItems.put(product, cartItems.getOrDefault(product, 0) + qty);
            product.setQuantity(product.getQuantity() - qty);
        }
    }

    public void removeFromCart(Product product) {
        cartItems.remove(product);
    }

    public Map<Product, Integer> getCartItems() { return cartItems; }
}