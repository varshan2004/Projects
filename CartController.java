package controller; 

import model.Cart; 
import model.Inventory;
import model.Product;
import javax.swing.*;
import java.util.Map;

public class CartController {
    private Cart cart;
    private Inventory inventory;

    public CartController(Inventory inventory) {
        this.inventory = inventory;
        this.cart = new Cart(); 
    }

    
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
                    
                    product.setQuantity(product.getQuantity() - difference);
                } else if (difference < 0) {
                    
                    product.setQuantity(product.getQuantity() - difference);
                }
    
                if (newQuantity > 0) {
                    cart.getCartItems().put(product, newQuantity);
                } else {
                    cart.getCartItems().remove(product); 
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
                product.setQuantity(product.getQuantity() + removedQty); 
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
        
            
            for (Product product : cart.getCartItems().keySet()) {
                if (product.isExpired()) {
                    JOptionPane.showMessageDialog(null, "❌ Checkout failed! Cart contains expired items.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
        
          
            cart.getCartItems().clear(); 
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
        return cart.getCartItems(); 
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
