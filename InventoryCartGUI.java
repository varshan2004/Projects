package view; // ✅ Fix: Correct package

import controller.CartController;
import controller.InventoryController;
import model.Product;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public class InventoryCartGUI extends JFrame {
    private InventoryController inventoryController;
    private CartController cartController;
    private JTable inventoryTable, cartTable;
    private DefaultTableModel inventoryTableModel, cartTableModel;

    public InventoryCartGUI(InventoryController inventoryController, CartController cartController) {
        this.inventoryController = inventoryController;
        this.cartController = cartController;

        setTitle("🛍️ Inventory & Cart System");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JTabbedPane tabbedPane = new JTabbedPane();

        // 🔹 INVENTORY TAB
        JPanel inventoryPanel = new JPanel(new BorderLayout());
        inventoryTableModel = new DefaultTableModel(new Object[]{"ID", "Name", "Price", "Qty", "Expiry"}, 0);
        inventoryTable = new JTable(inventoryTableModel);

        JButton addProductButton = new JButton("➕ Add Product");
        JButton modifyProductButton = new JButton("✏️ Modify Product");
        JButton deleteProductButton = new JButton("🗑 Delete Product");
        JButton moveToCartButton = new JButton("🛒 Move to Cart");

        addProductButton.addActionListener(e -> addProduct());
        modifyProductButton.addActionListener(e -> modifyProduct());
        deleteProductButton.addActionListener(e -> deleteProduct());
        moveToCartButton.addActionListener(e -> moveToCart());

        JPanel inventoryButtons = new JPanel();
        inventoryButtons.add(addProductButton);
        inventoryButtons.add(modifyProductButton);
        inventoryButtons.add(deleteProductButton);
        inventoryButtons.add(moveToCartButton);

        inventoryPanel.add(new JScrollPane(inventoryTable), BorderLayout.CENTER);
        inventoryPanel.add(inventoryButtons, BorderLayout.SOUTH);
        tabbedPane.addTab("📦 Inventory", inventoryPanel);

        // 🔹 CART TAB
        JPanel cartPanel = new JPanel(new BorderLayout());
        cartTableModel = new DefaultTableModel(new Object[]{"ID", "Name", "Qty", "Price"}, 0);
        cartTable = new JTable(cartTableModel);

        JButton modifyCartButton = new JButton("✏️ Modify Quantity");
        JButton removeCartItemButton = new JButton("🗑 Remove Item");
        JButton checkoutButton = new JButton("💳 Checkout");

        modifyCartButton.addActionListener(e -> modifyCartItem());
        removeCartItemButton.addActionListener(e -> removeCartItem());
        checkoutButton.addActionListener(e -> checkout());

        JPanel cartButtons = new JPanel();
        cartButtons.add(modifyCartButton);
        cartButtons.add(removeCartItemButton);
        cartButtons.add(checkoutButton);

        cartPanel.add(new JScrollPane(cartTable), BorderLayout.CENTER);
        cartPanel.add(cartButtons, BorderLayout.SOUTH);
        tabbedPane.addTab("🛒 Shopping Cart", cartPanel);

        add(tabbedPane, BorderLayout.CENTER);
        refreshInventoryTable();
        refreshCartTable();

        setVisible(true);
    }

    private void addProduct() {
        JTextField idField = new JTextField();
        JTextField nameField = new JTextField();
        JTextField priceField = new JTextField();
        JTextField qtyField = new JTextField();
        JTextField expiryField = new JTextField();
    
        JPanel panel = new JPanel(new GridLayout(5, 2));
        panel.add(new JLabel("ID:"));
        panel.add(idField);
        panel.add(new JLabel("Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Price:"));
        panel.add(priceField);
        panel.add(new JLabel("Qty:"));
        panel.add(qtyField);
        panel.add(new JLabel("Expiry (yyyy-mm-dd):"));
        panel.add(expiryField);
    
        int result = JOptionPane.showConfirmDialog(this, panel, "Enter Product Details", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                int id = Integer.parseInt(idField.getText());
                String name = nameField.getText();
                double price = Double.parseDouble(priceField.getText());
                int qty = Integer.parseInt(qtyField.getText());
                LocalDate expiry = LocalDate.parse(expiryField.getText());
    
                boolean added = inventoryController.addProduct(id, name, price, qty, expiry); // ✅ Ensure method call is correct
                if (added) {
                    refreshInventoryTable();
                    JOptionPane.showMessageDialog(this, "✅ Product Added Successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "❌ Invalid Input! Please check values.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    

    // 🔹 MODIFY PRODUCT FUNCTION
    private void modifyProduct() {
        int selectedRow = inventoryTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Select a product to modify!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int id = (int) inventoryTableModel.getValueAt(selectedRow, 0);
        JTextField priceField = new JTextField();
        JTextField qtyField = new JTextField();

        JPanel panel = new JPanel(new GridLayout(2, 2));
        panel.add(new JLabel("New Price:"));
        panel.add(priceField);
        panel.add(new JLabel("New Qty:"));
        panel.add(qtyField);

        int result = JOptionPane.showConfirmDialog(this, panel, "Modify Product", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                double newPrice = Double.parseDouble(priceField.getText());
                int newQty = Integer.parseInt(qtyField.getText());
                inventoryController.modifyProduct(id, newPrice, newQty);
                refreshInventoryTable();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Invalid Input!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // 🔹 DELETE PRODUCT FUNCTION
    private void deleteProduct() {
        int selectedRow = inventoryTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Select a product to delete!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int id = (int) inventoryTableModel.getValueAt(selectedRow, 0);
        inventoryController.deleteProduct(id);
        refreshInventoryTable();
    }

    // 🔹 MOVE PRODUCT TO CART FUNCTION
    private void moveToCart() {
        int selectedRow = inventoryTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Select a product to move to cart!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int id = (int) inventoryTableModel.getValueAt(selectedRow, 0);
        JTextField qtyField = new JTextField();

        JPanel panel = new JPanel(new GridLayout(1, 2));
        panel.add(new JLabel("Enter Quantity:"));
        panel.add(qtyField);

        int result = JOptionPane.showConfirmDialog(this, panel, "Move to Cart", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            int qty = Integer.parseInt(qtyField.getText());
            cartController.addToCart(id, qty);
            refreshCartTable();
            refreshInventoryTable();
        }
    }

  

   public void refreshCartTable() {
    cartTableModel.setRowCount(0); // ✅ Clear table before updating

    for (Map.Entry<Product, Integer> entry : cartController.getCartItems().entrySet()) {
        Product product = entry.getKey();
        int qty = entry.getValue();
        cartTableModel.addRow(new Object[]{
            product.getId(), product.getName(), qty, product.getPrice() * qty
        });
    }
}

private void modifyCartItem() {
    int selectedRow = cartTable.getSelectedRow();
    if (selectedRow == -1) {
        JOptionPane.showMessageDialog(this, "❌ Select an item to modify!", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    int id = (int) cartTableModel.getValueAt(selectedRow, 0);
    JTextField qtyField = new JTextField();

    JPanel panel = new JPanel(new GridLayout(1, 2));
    panel.add(new JLabel("New Quantity:"));
    panel.add(qtyField);

    int result = JOptionPane.showConfirmDialog(this, panel, "Modify Cart Item", JOptionPane.OK_CANCEL_OPTION);
    if (result == JOptionPane.OK_OPTION) {
        try {
            int newQty = Integer.parseInt(qtyField.getText());
            boolean updated = cartController.modifyCartItem(id, newQty);

            if (updated) {
                refreshCartTable();
                refreshInventoryTable(); // ✅ Ensure inventory updates as well
                JOptionPane.showMessageDialog(this, "✅ Cart item updated!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "❌ Cannot modify item!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Invalid Input!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}


// 🔹 REMOVE CART ITEM FUNCTION
private void removeCartItem() {
    int selectedRow = cartTable.getSelectedRow();
    if (selectedRow == -1) {
        JOptionPane.showMessageDialog(this, "❌ Select an item to remove!", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    int id = (int) cartTableModel.getValueAt(selectedRow, 0);
    cartController.removeCartItem(id);
    refreshCartTable();
    refreshInventoryTable(); // ✅ Ensure inventory updates after removal
    JOptionPane.showMessageDialog(this, "🗑️ Item removed from cart!", "Success", JOptionPane.INFORMATION_MESSAGE);
}


// 🔹 CHECKOUT FUNCTION
private void checkout() {
    String billContent = cartController.getBillSummary(); // ✅ Ensure this method is called
    generateBill(billContent); // ✅ Save the bill as a text file

    cartController.checkout();
    refreshCartTable();
    refreshInventoryTable();
    JOptionPane.showMessageDialog(this, "✅ Checkout Complete! Bill Saved.", "Success", JOptionPane.INFORMATION_MESSAGE);
}

// 🔹 Save the Bill as a Text File
private void generateBill(String billContent) {
    String fileName = "Bill_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".txt";

    try (FileWriter writer = new FileWriter(fileName)) {
        writer.write(billContent);
        JOptionPane.showMessageDialog(this, "📄 Bill saved as: " + fileName, "Bill Generated", JOptionPane.INFORMATION_MESSAGE);
    } catch (IOException e) {
        JOptionPane.showMessageDialog(this, "❌ Error saving bill!", "Error", JOptionPane.ERROR_MESSAGE);
    }
}
public void refreshInventoryTable() {
    inventoryTableModel.setRowCount(0);

    for (Product p : inventoryController.getAllProducts()) {
        inventoryTableModel.addRow(new Object[]{
            p.getId(), p.getName(), p.getPrice(), p.getQuantity(), p.getExpiryDate()
        });
    }
}


private void addItemToCart() {
    JTextField idOrNameField = new JTextField();
    JTextField qtyField = new JTextField();

    JPanel panel = new JPanel(new GridLayout(2, 2));
    panel.add(new JLabel("Enter ID or Name:"));
    panel.add(idOrNameField);
    panel.add(new JLabel("Quantity:"));
    panel.add(qtyField);

    int result = JOptionPane.showConfirmDialog(this, panel, "Add Item to Cart", JOptionPane.OK_CANCEL_OPTION);
    if (result == JOptionPane.OK_OPTION) {
        try {
            String input = idOrNameField.getText().trim();
            int quantity = Integer.parseInt(qtyField.getText().trim());

            boolean added = cartController.addToCartByIdOrName(input, quantity); // ✅ Correct method call

            if (added) {
                refreshCartTable();
                JOptionPane.showMessageDialog(this, "✅ Item added to cart!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "❌ Item not found or unavailable!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Invalid Input!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}



}