package client;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class BuyerOrderScreen extends JFrame {

    private JComboBox<String> drinkCombo;
    private JTextField quantityField;
    private JComboBox<String> branchCombo;
    private JTextField customerNameField;
    private JLabel totalLabel;
    private JButton placeOrderButton;

    public BuyerOrderScreen() {
        setTitle("🧃 Place a Drink Order");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 400);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JLabel header = new JLabel("🧾 Duka Moja - Order Your Drink", SwingConstants.CENTER);
        header.setFont(new Font("Arial", Font.BOLD, 20));
        header.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(header, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Customer Name
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("👤 Customer Name:"), gbc);
        gbc.gridx = 1;
        customerNameField = new JTextField(15);
        formPanel.add(customerNameField, gbc);

        // Drink Combo
        gbc.gridx = 0;
        gbc.gridy++;
        formPanel.add(new JLabel("🥤 Select Drink:"), gbc);
        gbc.gridx = 1;
        drinkCombo = new JComboBox<>(new String[]{"Coke", "Fanta", "Sprite"});
        formPanel.add(drinkCombo, gbc);

        // Quantity
        gbc.gridx = 0;
        gbc.gridy++;
        formPanel.add(new JLabel("🔢 Quantity:"), gbc);
        gbc.gridx = 1;
        quantityField = new JTextField();
        formPanel.add(quantityField, gbc);

        // Branch
        gbc.gridx = 0;
        gbc.gridy++;
        formPanel.add(new JLabel("📍 Branch:"), gbc);
        gbc.gridx = 1;
        branchCombo = new JComboBox<>(new String[]{"Nairobi", "Nakuru", "Kisumu", "Mombasa"});
        formPanel.add(branchCombo, gbc);

        // Total
        gbc.gridx = 0;
        gbc.gridy++;
        formPanel.add(new JLabel("💰 Total Amount:"), gbc);
        gbc.gridx = 1;
        totalLabel = new JLabel("KES 0.00");
        totalLabel.setFont(new Font("Arial", Font.BOLD, 14));
        totalLabel.setForeground(Color.BLUE);
        formPanel.add(totalLabel, gbc);

        // Button
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        placeOrderButton = new JButton("🛒 Place Order");
        placeOrderButton.addActionListener(this::placeOrder);
        formPanel.add(placeOrderButton, gbc);

        add(formPanel, BorderLayout.CENTER);

        // Live total update
        quantityField.getDocument().addDocumentListener(new SimpleDocumentListener() {
            public void update() {
                updateTotal();
            }
        });
    }

    private void updateTotal() {
        String selectedDrink = (String) drinkCombo.getSelectedItem();
        int quantity;
        try {
            quantity = Integer.parseInt(quantityField.getText());
        } catch (NumberFormatException e) {
            totalLabel.setText("KES 0.00");
            return;
        }

        double price = switch (selectedDrink) {
            case "Coke" -> 100;
            case "Fanta" -> 90;
            case "Sprite" -> 95;
            default -> 0;
        };

        double total = price * quantity;
        totalLabel.setText("KES " + total);
    }

    private void placeOrder(ActionEvent e) {
        String customer = customerNameField.getText().trim();
        String drink = (String) drinkCombo.getSelectedItem();
        String branch = (String) branchCombo.getSelectedItem();
        int quantity;

        if (customer.isEmpty() || quantityField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            quantity = Integer.parseInt(quantityField.getText());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid quantity!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        double price = switch (drink) {
            case "Coke" -> 100;
            case "Fanta" -> 90;
            case "Sprite" -> 95;
            default -> 0;
        };

        double total = price * quantity;
        String orderDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:drinksales.db")) {
            String insert = "INSERT INTO orders (customerName, drinkName, quantity, branchName, totalAmount, orderDate) VALUES (?, ?, ?, ?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(insert)) {
                stmt.setString(1, customer);
                stmt.setString(2, drink);
                stmt.setInt(3, quantity);
                stmt.setString(4, branch);
                stmt.setDouble(5, total);
                stmt.setString(6, orderDate);
                stmt.executeUpdate();
            }

            String update = "UPDATE drinks SET quantity = quantity - ? WHERE name = ?";
            try (PreparedStatement stmt = conn.prepareStatement(update)) {
                stmt.setInt(1, quantity);
                stmt.setString(2, drink);
                stmt.executeUpdate();
            }

            JOptionPane.showMessageDialog(this, "✅ Order placed successfully!");

            // Clear form
            quantityField.setText("");
            customerNameField.setText("");
            totalLabel.setText("KES 0.00");

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "❌ Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Inner class for live updates
    private abstract class SimpleDocumentListener implements DocumentListener {
        public abstract void update();
        @Override public void insertUpdate(DocumentEvent e) { update(); }
        @Override public void removeUpdate(DocumentEvent e) { update(); }
        @Override public void changedUpdate(DocumentEvent e) { update(); }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new BuyerOrderScreen().setVisible(true));
    }
}
