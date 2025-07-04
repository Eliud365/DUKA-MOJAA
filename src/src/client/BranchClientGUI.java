// File: BranchClientGUI.java
package client;

import client.model.Order;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class BranchClientGUI extends JFrame {
    private JTextField nameField, drinkField, quantityField;
    private JComboBox<String> branchBox;
    private JLabel totalLabel;
    private static final String SERVER_IP = "127.0.0.1";
    private static final int SERVER_PORT = 9090;

    private final Map<String, Double> drinkPrices = new HashMap<>() {{
        put("Coke", 50.0);
        put("Pepsi", 45.0);
        put("Fanta", 40.0);
        put("Sprite", 35.0);
    }};

    public BranchClientGUI() {
        setTitle("Branch Drink Order App");
        setSize(400, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new GridLayout(7, 2, 5, 5));

        add(new JLabel("Customer Name:"));
        nameField = new JTextField();
        add(nameField);

        add(new JLabel("Drink Name:"));
        drinkField = new JTextField();
        add(drinkField);

        add(new JLabel("Quantity:"));
        quantityField = new JTextField();
        add(quantityField);

        add(new JLabel("Branch:"));
        branchBox = new JComboBox<>(new String[] {"Nakuru", "Mombasa", "Kisumu", "Nairobi"});
        add(branchBox);

        JButton calcButton = new JButton("Calculate Total");
        calcButton.addActionListener(this::calculateTotal);
        add(calcButton);

        totalLabel = new JLabel("Total: 0.00");
        add(totalLabel);

        JButton sendButton = new JButton("Send Order");
        sendButton.addActionListener(this::sendOrder);
        add(sendButton);

        setVisible(true);
    }

    private void calculateTotal(ActionEvent e) {
        String drink = drinkField.getText();
        int quantity;

        if (!drinkPrices.containsKey(drink)) {
            showError("Invalid drink. Choose from: " + drinkPrices.keySet());
            return;
        }

        try {
            quantity = Integer.parseInt(quantityField.getText());
        } catch (NumberFormatException ex) {
            showError("Invalid quantity.");
            return;
        }

        double price = drinkPrices.get(drink);
        double total = price * quantity;
        totalLabel.setText(String.format("Total: %.2f", total));
    }

    private void sendOrder(ActionEvent e) {
        String name = nameField.getText();
        String drink = drinkField.getText();
        String branch = (String) branchBox.getSelectedItem();
        int quantity;

        if (!drinkPrices.containsKey(drink)) {
            showError("Invalid drink.");
            return;
        }

        try {
            quantity = Integer.parseInt(quantityField.getText());
        } catch (NumberFormatException ex) {
            showError("Quantity must be a number.");
            return;
        }

        try (Socket socket = new Socket(SERVER_IP, SERVER_PORT);
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream())) {

            Order order = new Order(name, drink, quantity, branch);
            out.writeObject(order);
            JOptionPane.showMessageDialog(this, "✅ Order sent successfully.");

        } catch (IOException ex) {
            showError("Connection failed: " + ex.getMessage());
        }
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(BranchClientGUI::new);
    }
}
