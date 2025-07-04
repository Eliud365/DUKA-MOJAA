package server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DatabaseManager {
    private static final String DB_URL = "jdbc:sqlite:drinksales.db";

    public static void initializeDatabase() {
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            String createOrdersTable = "CREATE TABLE IF NOT EXISTS orders (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "customers TEXT," +
                    "drinks TEXT," +
                    "quantity INTEGER," +
                    "branches TEXT," +
                    "double REAL)";
            conn.createStatement().execute(createOrdersTable);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void saveOrder(String customers, String drinks, int orders, String branches, double stock) {
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            String insert = "INSERT INTO drinks (customers, drinks, orders, branches, stock) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(insert);
            stmt.setString(1, customers);
            stmt.setString(2, drinks);
            stmt.setInt(3, orders);
            stmt.setString(4, branches);
            stmt.setDouble(5, stock);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
