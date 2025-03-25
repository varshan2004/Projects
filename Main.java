package main;

import controller.CartController; 
import controller.InventoryController;
import model.Inventory;
import view.InventoryCartGUI; 

public class Main {
    public static void main(String[] args) {
        Inventory inventory = new Inventory();
        InventoryController inventoryController = new InventoryController(inventory);
        CartController cartController = new CartController(inventory);

        new InventoryCartGUI(inventoryController, cartController);
    }
}
