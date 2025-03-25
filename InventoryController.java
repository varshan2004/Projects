package controller;

import model.Inventory;
import model.Product;
import java.time.LocalDate;
import java.util.List;

import javax.swing.JOptionPane;

public class InventoryController {
    private Inventory inventory;

    public InventoryController(Inventory inventory) {
        this.inventory = inventory;
    }

   public boolean addProduct(int id, String name, double price, int qty, LocalDate expiry) {
    if (expiry.isBefore(LocalDate.now())) { 
        JOptionPane.showMessageDialog(null, "❌ Cannot add expired product!", "Error", JOptionPane.ERROR_MESSAGE);
        return false;
    }
    
    Product newProduct = new Product(id, name, price, qty, expiry);
    inventory.addProduct(newProduct);
    return true;
}

    

 
    public void modifyProduct(int id, double newPrice, int newQuantity) {
        Product product = inventory.getProductById(id);
        if (product != null) {
            product.setPrice(newPrice);
            product.setQuantity(newQuantity);
        }
    }

 
    public void deleteProduct(int id) {
        inventory.removeProduct(id);
    }

   
    public List<Product> getAllProducts() {
        return inventory.getAllProducts();
    }

    public Inventory getInventory() {
        return inventory;
    }
   

    
}
