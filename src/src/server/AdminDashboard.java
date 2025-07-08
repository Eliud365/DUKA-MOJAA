package server;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.text.DecimalFormat;

public class AdminDashboard extends JFrame {

    private JTable ordersTable;
    private DefaultTableModel tableModel;
    private JButton refreshButton;
    private JLabel totalSalesLabel;
    private JLabel nairobiSalesLabel;
    private JLabel nakuruSalesLabel;
    private JLabel kisumuSalesLabel;
    private JLabel mombasaSalesLabel;
    private JLabel cokeStockLabel;
    private JLabel fantaStockLabel;
    private JLabel spriteStockLabel;
    private JLabel lowStockWarningLabel;

    private static final String DB_URL = "jdbc:sqlite:drinksales.db";
    private DecimalFormat currencyFormat = new DecimalFormat("KES #,##0.00");

    public AdminDashboard() {
        initializeGUI();
        loadDataFromDatabase();
    }

    private void initializeGUI() {
        setTitle("Drink Sales Admin Panel");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setSize(1200, 800);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        refreshButton = new JButton("🔄 Refresh Orders");
        refreshButton.setFont(new Font("Arial", Font.BOLD, 12));
        refreshButton.addActionListener(new RefreshActionListener());
        topPanel.add(refreshButton);

        JPanel ordersPanel = createOrdersPanel();

        JPanel bottomPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JPanel salesPanel = createSalesPanel();
        JPanel inventoryPanel = createInventoryPanel();

        bottomPanel.add(salesPanel);
        bottomPanel.add(inventoryPanel);

        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(ordersPanel, BorderLayout.CENTER);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private JPanel createOrdersPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Customer Orders"));

        String[] columns = {"Customer Name", "Drink", "Quantity", "Branch", "Total", "Order Date"};
        tableModel = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        ordersTable = new JTable(tableModel);
        ordersTable.setFont(new Font("Arial", Font.PLAIN, 11));
        ordersTable.setRowHeight(25);
        JScrollPane scrollPane = new JScrollPane(ordersTable);
        scrollPane.setPreferredSize(new Dimension(0, 300));
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createSalesPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Sales Summary"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        totalSalesLabel = new JLabel("Total Business Sales: KES 0.00");
        totalSalesLabel.setFont(new Font("Arial", Font.BOLD, 16));
        totalSalesLabel.setForeground(new Color(0, 128, 0));
        panel.add(totalSalesLabel, gbc);

        gbc.gridwidth = 1;
        gbc.gridy++;
        gbc.gridx = 0;
        panel.add(new JLabel("🏢 Nairobi:"), gbc);
        gbc.gridx = 1;
        nairobiSalesLabel = new JLabel("KES 0.00");
        panel.add(nairobiSalesLabel, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        panel.add(new JLabel("🏢 Nakuru:"), gbc);
        gbc.gridx = 1;
        nakuruSalesLabel = new JLabel("KES 0.00");
        panel.add(nakuruSalesLabel, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        panel.add(new JLabel("🏢 Kisumu:"), gbc);
        gbc.gridx = 1;
        kisumuSalesLabel = new JLabel("KES 0.00");
        panel.add(kisumuSalesLabel, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        panel.add(new JLabel("🏢 Mombasa:"), gbc);
        gbc.gridx = 1;
        mombasaSalesLabel = new JLabel("KES 0.00");
        panel.add(mombasaSalesLabel, gbc);

        return panel;
    }

    private JPanel createInventoryPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Current Inventory"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("🥤 Coke:"), gbc);
        gbc.gridx = 1;
        cokeStockLabel = new JLabel("0 units");
        panel.add(cokeStockLabel, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        panel.add(new JLabel("🍊 Fanta:"), gbc);
        gbc.gridx = 1;
        fantaStockLabel = new JLabel("0 units");
        panel.add(fantaStockLabel, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        panel.add(new JLabel("🍋 Sprite:"), gbc);
        gbc.gridx = 1;
        spriteStockLabel = new JLabel("0 units");
        panel.add(spriteStockLabel, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        lowStockWarningLabel = new JLabel("");
        lowStockWarningLabel.setForeground(Color.RED);
        lowStockWarningLabel.setFont(new Font("Arial", Font.BOLD, 12));
        panel.add(lowStockWarningLabel, gbc);

        return panel;
    }

    private void loadDataFromDatabase() {
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            tableModel.setRowCount(0);

            // 1. Load Orders
            String query = "SELECT customerName, drinkName, quantity, branchName, totalAmount, orderDate FROM orders";
            try (PreparedStatement stmt = conn.prepareStatement(query);
                 ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Object[] row = {
                            rs.getString("customerName"),
                            rs.getString("drinkName"),
                            rs.getInt("quantity"),
                            rs.getString("branchName"),
                            "KES " + rs.getDouble("totalAmount"),
                            rs.getString("orderDate")
                    };
                    tableModel.addRow(row);
                }
            }

            // 2. Sales Per Branch + Total
            double totalSales = 0;
            double nairobi = 0, nakuru = 0, kisumu = 0, mombasa = 0;

            String salesQuery = "SELECT branchName, SUM(totalAmount) AS total FROM orders GROUP BY branchName";
            try (PreparedStatement stmt = conn.prepareStatement(salesQuery);
                 ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    double branchTotal = rs.getDouble("total");
                    totalSales += branchTotal;
                    switch (rs.getString("branchName").toLowerCase()) {
                        case "nairobi" -> nairobi = branchTotal;
                        case "nakuru" -> nakuru = branchTotal;
                        case "kisumu" -> kisumu = branchTotal;
                        case "mombasa" -> mombasa = branchTotal;
                    }
                }
            }

            updateSalesLabels(totalSales, nairobi, nakuru, kisumu, mombasa);

            // 3. Inventory Levels
            String invQuery = "SELECT drinkName, stock FROM drinks";
            int coke = 0, fanta = 0, sprite = 0;
            try (PreparedStatement stmt = conn.prepareStatement(invQuery);
                 ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String drink = rs.getString("drinkName").toLowerCase();
                    int stock = rs.getInt("stock");
                    switch (drink) {
                        case "coke" -> coke = stock;
                        case "fanta" -> fanta = stock;
                        case "sprite" -> sprite = stock;
                    }
                }
            }

            updateInventoryLabels(coke, fanta, sprite);

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "❌ DB Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateSalesLabels(double total, double nairobi, double nakuru, double kisumu, double mombasa) {
        totalSalesLabel.setText("Total Business Sales: " + currencyFormat.format(total));
        nairobiSalesLabel.setText(currencyFormat.format(nairobi));
        nakuruSalesLabel.setText(currencyFormat.format(nakuru));
        kisumuSalesLabel.setText(currencyFormat.format(kisumu));
        mombasaSalesLabel.setText(currencyFormat.format(mombasa));
    }

    private void updateInventoryLabels(int coke, int fanta, int sprite) {
        cokeStockLabel.setText(coke + " units");
        fantaStockLabel.setText(fanta + " units");
        spriteStockLabel.setText(sprite + " units");

        StringBuilder warning = new StringBuilder();
        if (coke < 5) warning.append("⚠️ Low stock: Coke ");
        if (fanta < 5) warning.append("⚠️ Low stock: Fanta ");
        if (sprite < 5) warning.append("⚠️ Low stock: Sprite ");

        lowStockWarningLabel.setText(warning.toString());
    }

    private class RefreshActionListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            loadDataFromDatabase();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AdminDashboard().setVisible(true));
    }
}
