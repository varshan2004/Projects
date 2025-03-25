package main; // 🔹 FIX: Declare correct package

import controller.CartController; // 🔹 FIX: Import the controllers
import controller.InventoryController;
import model.Inventory;
import view.InventoryCartGUI; // 🔹 FIX: Ensure GUI is properly imported

public class Main {
    public static void main(String[] args) {
        Inventory inventory = new Inventory();
        InventoryController inventoryController = new InventoryController(inventory);
        CartController cartController = new CartController(inventory);

        new InventoryCartGUI(inventoryController, cartController); // 🔹 FIX: Ensure correct object creation
    }
}