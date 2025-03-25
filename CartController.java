package controller; // 🔹 FIX: Ensure package is declared

import model.Cart;  // 🔹 FIX: Import the correct Cart class
import model.Inventory;
import model.Product;
import javax.swing.*;
import java.util.Map;

public class CartController {
    private Cart cart;
    private Inventory inventory;

    public CartController(Inventory inventory) {
        this.inventory = inventory;
        this.cart = new Cart(); // 🔹 FIX: Initialize the cart object
    }

    // 🔹 FIX: Ensure cart is properly accessed
    public boolean addToCart(int id, int qty) {
        Product product = inventory.getProductById(id);
        if (product != null && !product.isExpired() && product.getQuantity() >= qty) {
            cart.addToCart(product, qty);
            return true;
        }
        return false;
    }

    public boolean modifyCartItem(int id, int newQuantity) {
        for (Product product : cart.getCartItems().keySet()) {
            if (product.getId() == id) {
                int currentCartQty = cart.getCartItems().get(product);
                int difference = newQuantity - currentCartQty;
    
                if (difference > 0 && product.getQuantity() >= difference) {
                    // ✅ Reduce inventory stock if increasing cart quantity
                    product.setQuantity(product.getQuantity() - difference);
                } else if (difference < 0) {
                    // ✅ Restore stock if decreasing cart quantity
                    product.setQuantity(product.getQuantity() - difference);
                }
    
                if (newQuantity > 0) {
                    cart.getCartItems().put(product, newQuantity);
                } else {
                    cart.getCartItems().remove(product); // ✅ Remove item if quantity is 0
                }
    
                return true;
            }
        }
        return false;
    }
    
    
    
    
    public void removeCartItem(int id) {
        for (Product product : cart.getCartItems().keySet()) {
            if (product.getId() == id) {
                int removedQty = cart.getCartItems().get(product);
                product.setQuantity(product.getQuantity() + removedQty); // ✅ Restore stock
                cart.getCartItems().remove(product);
                break;
            }
        }
    }
    
    
    
    
   
        public void checkout() {
            if (cart.getCartItems().isEmpty()) {
                JOptionPane.showMessageDialog(null, "🛒 Cart is empty! Add items before checkout.", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }
        
            // ✅ Prevent checkout if any item is expired
            for (Product product : cart.getCartItems().keySet()) {
                if (product.isExpired()) {
                    JOptionPane.showMessageDialog(null, "❌ Checkout failed! Cart contains expired items.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
        
            // ✅ DO NOT reduce inventory again if it was already adjusted when modifying the cart
            cart.getCartItems().clear(); // ✅ Empty the cart after checkout
        }
        
    
    
    public String getBillSummary() {
        StringBuilder bill = new StringBuilder("🛒 Bill Summary\n\n");
        double totalAmount = 0;
    
        for (Map.Entry<Product, Integer> entry : cart.getCartItems().entrySet()) {
            Product product = entry.getKey();
            int quantity = entry.getValue();
            double cost = product.getPrice() * quantity;
            totalAmount += cost;
    
            bill.append(product.getName())
                .append(" (x").append(quantity).append(") - Rs")
                .append(cost).append("\n");
        }
    
        bill.append("\n💰 Total: Rs").append(totalAmount);
        return bill.toString();
    }
    public Map<Product, Integer> getCartItems() {
        return cart.getCartItems(); // ✅ Ensures correct data is returned
    }
    public boolean addToCartByIdOrName(String input, int qty) {
        Product product = null;
    
        try {
            int id = Integer.parseInt(input);
            product = inventory.getProductById(id);
        } catch (NumberFormatException e) {
            product = inventory.getProductByName(input);
        }
    
        if (product != null && !product.isExpired() && product.getQuantity() >= qty) {
            cart.addToCart(product, qty);
            return true;
        }
    
        return false;
    }
    
}