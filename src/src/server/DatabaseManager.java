package server;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DatabaseManager {
    private static final String DB_URL = "jdbc:sqlite:drinksales.db";

    public static void initializeDatabase() {
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            if (conn != null) {
                Statement stmt = conn.createStatement();

                // Create drinks table
                stmt.executeUpdate("""
                    CREATE TABLE IF NOT EXISTS drinks (
                        name TEXT PRIMARY KEY,
                        quantity INTEGER
                    )
                """);

                // Insert initial stock
                stmt.executeUpdate("""
                    INSERT OR IGNORE INTO drinks (name, quantity) VALUES
                    ('Coke', 20),
                    ('Fanta', 15),
                    ('Sprite', 10)
                """);

                // Create orders table
                stmt.executeUpdate("""
                    CREATE TABLE IF NOT EXISTS orders (
                        id INTEGER PRIMARY KEY AUTOINCREMENT,
                        customerName TEXT,
                        drinkName TEXT,
                        quantity INTEGER,
                        branchName TEXT,
                        totalAmount REAL,
                        orderDate TEXT
                    )
                """);

                System.out.println("✅ Database initialized.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void saveOrder(String customer, String drink, int quantity, String branch, double total) {
        String orderDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            // Save order
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

            // Decrease stock
            String update = "UPDATE drinks SET quantity = quantity - ? WHERE name = ?";
            try (PreparedStatement stmt = conn.prepareStatement(update)) {
                stmt.setInt(1, quantity);
                stmt.setString(2, drink);
                stmt.executeUpdate();
            }

            // Check if stock is low
            String check = "SELECT quantity FROM drinks WHERE name = ?";
            try (PreparedStatement stmt = conn.prepareStatement(check)) {
                stmt.setString(1, drink);
                ResultSet rs = stmt.executeQuery();
                if (rs.next() && rs.getInt("quantity") < 5) {
                    System.out.println("⚠️ LOW STOCK: " + drink + " has only " + rs.getInt("quantity") + " units left.");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
