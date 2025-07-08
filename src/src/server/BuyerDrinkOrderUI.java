package server;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

public class BuyerDrinkOrderUI extends JFrame {
    private JComboBox<String> drinkSelector;
    private JSpinner quantitySpinner;
    private JTextField nameField;
    private JComboBox<String> branchSelector;
    private JButton orderButton;
    private JLabel statusLabel;

    private HashMap<String, Integer> stock;
    private HashMap<String, Double> prices;

    public BuyerDrinkOrderUI() {
        setTitle("Duka Moja - Order Drinks");
        setSize(500, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Setup dummy stock & prices (in actual project, fetch from server)
        stock = new HashMap<>();
        stock.put("Coke", 20);
        stock.put("Fanta", 15);
        stock.put("Sprite", 10);

        prices = new HashMap<>();
        prices.put("Coke", 100.0);
        prices.put("Fanta", 90.0);
        prices.put("Sprite", 95.0);

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridLayout(6, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));

        // Name field
        formPanel.add(new JLabel("👤 Your Name:"));
        nameField = new JTextField();
        formPanel.add(nameField);

        // Drink selector
        formPanel.add(new JLabel("🥤 Select Drink:"));
        drinkSelector = new JComboBox<>(new String[]{"Coke", "Fanta", "Sprite"});
        formPanel.add(drinkSelector);

        // Quantity spinner
        formPanel.add(new JLabel("🔢 Quantity:"));
        quantitySpinner = new JSpinner(new SpinnerNumberModel(1, 1, 20, 1));
        formPanel.add(quantitySpinner);

        // Branch selector
        formPanel.add(new JLabel("📍 Branch:"));
        branchSelector = new JComboBox<>(new String[]{"Nairobi", "Nakuru", "Kisumu", "Mombasa"});
        formPanel.add(branchSelector);

        // Spacer
        formPanel.add(new JLabel(""));
        formPanel.add(new JLabel(""));

        // Order Button
        orderButton = new JButton("🚀 Place Order");
        orderButton.setBackground(new Color(0, 128, 255));
        orderButton.setForeground(Color.WHITE);
        orderButton.setFont(new Font("Arial", Font.BOLD, 14));
        orderButton.setFocusPainted(false);
        orderButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        formPanel.add(orderButton);

        // Status Label
        statusLabel = new JLabel("", JLabel.CENTER);
        statusLabel.setForeground(new Color(34, 139, 34));
        statusLabel.setFont(new Font("Arial", Font.ITALIC, 13));

        add(formPanel, BorderLayout.CENTER);
        add(statusLabel, BorderLayout.SOUTH);

        // Action Listener
        orderButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = nameField.getText();
                String drink = (String) drinkSelector.getSelectedItem();
                int quantity = (Integer) quantitySpinner.getValue();
                String branch = (String) branchSelector.getSelectedItem();

                if (name.isEmpty()) {
                    statusLabel.setText("⚠️ Please enter your name.");
                    return;
                }

                if (stock.get(drink) < quantity) {
                    statusLabel.setText("❌ Not enough stock for " + drink);
                    return;
                }

                double total = quantity * prices.get(drink);
                statusLabel.setText("✅ " + name + ", you ordered " + quantity + " x " + drink + " from " + branch + ". Total: KES " + total);

                // TODO: Send order to server
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new BuyerDrinkOrderUI().setVisible(true));
    }
}
