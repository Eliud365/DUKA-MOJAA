// File: HQServer.java
package client.server;

import client.model.Order;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class HQServer {
    private static final int PORT = 9090;
    private static final List<Order> orders = new ArrayList<>();
    private static final Map<String, Integer> branchSales = new HashMap<>();
    private static final Map<String, Integer> stock = new HashMap<>();
    private static final Map<String, Double> drinkPrices = new HashMap<>();
    private static final Map<String, Integer> drinkSales = new HashMap<>();

    static {
        // Initialize stock for available drinks
        stock.put("Coke", 100);
        stock.put("Pepsi", 100);
        stock.put("Fanta", 100);
        stock.put("Sprite", 100);

        // Initialize prices for each drink
        drinkPrices.put("Coke", 50.0);
        drinkPrices.put("Pepsi", 45.0);
        drinkPrices.put("Fanta", 40.0);
        drinkPrices.put("Sprite", 35.0);
    }

    public static void main(String[] args) {
        System.out.println("HQ Server started on port " + PORT);

        // Start admin console for live reporting
        new Thread(() -> handleAdminInput()).start();

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                new Thread(() -> handleClient(clientSocket)).start();
            }
        } catch (IOException e) {
            System.err.println("Server error: " + e.getMessage());
        }
    }

    private static void handleClient(Socket clientSocket) {
        try (ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream())) {
            Order order = (Order) in.readObject();

            synchronized (orders) {
                orders.add(order);
            }

            synchronized (branchSales) {
                branchSales.put(order.getBranchName(),
                        branchSales.getOrDefault(order.getBranchName(), 0) + order.getQuantity());
            }

            synchronized (stock) {
                String drink = order.getDrinkName();
                int remaining = stock.getOrDefault(drink, 0) - order.getQuantity();
                stock.put(drink, remaining);

                if (remaining < 10) {
                    System.out.println("⚠️ LOW STOCK ALERT: " + drink + " has " + remaining + " units remaining.");
                }
            }

            synchronized (drinkSales) {
                String drink = order.getDrinkName();
                drinkSales.put(drink, drinkSales.getOrDefault(drink, 0) + order.getQuantity());
            }

            saveOrderToFile(order);

            System.out.println("✅ Received order from " + order.getCustomerName() + " at " + order.getBranchName());

        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Client handling error: " + e.getMessage());
        }
    }

    private static void saveOrderToFile(Order order) {
        try (FileWriter fw = new FileWriter("orders.csv", true);
             BufferedWriter bw = new BufferedWriter(fw)) {

            String line = String.format("%s,%s,%.2f,%s\n",
                    order.getCustomerName(),
                    order.getDrinkName(),
                    drinkPrices.getOrDefault(order.getDrinkName(), 0.0),
                    order.getBranchName());

            bw.write(line);

        } catch (IOException e) {
            System.err.println("Failed to write order to file: " + e.getMessage());
        }
    }

    private static void handleAdminInput() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("\n[Admin] Type 'report' to view sales report: ");
            String cmd = scanner.nextLine();
            if (cmd.equalsIgnoreCase("report")) {
                printReport();
            }
        }
    }

    private static void printReport() {
        System.out.println("\n===== SALES REPORT =====");
        int totalUnits = 0;
        double totalRevenue = 0.0;

        for (String drink : drinkSales.keySet()) {
            int qty = drinkSales.get(drink);
            double price = drinkPrices.getOrDefault(drink, 0.0);
            double subtotal = qty * price;
            totalUnits += qty;
            totalRevenue += subtotal;

            System.out.printf("%s: %d units × %.2f = %.2f\n", drink, qty, price, subtotal);
        }

        System.out.println("TOTAL UNITS SOLD: " + totalUnits);
        System.out.printf("TOTAL REVENUE: %.2f\n", totalRevenue);

        System.out.println("\n===== BRANCH SALES SUMMARY =====");
        for (String branch : branchSales.keySet()) {
            System.out.println(branch + ": " + branchSales.get(branch) + " drinks sold");
        }

        System.out.println("\n===== STOCK STATUS =====");
        for (String drink : stock.keySet()) {
            System.out.println(drink + ": " + stock.get(drink) + " remaining");
        }
        System.out.println();
    }
}
