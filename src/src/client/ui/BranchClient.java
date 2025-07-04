package client.ui;

import client.model.Order;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class BranchClient {
    private static final String SERVER_IP = "127.0.0.1"; // Replace with actual HQ IP if on network
    private static final int SERVER_PORT = 9090;

    public static void main(String[] args) {
        Map<String, Double> drinkPrices = new HashMap<>();
        drinkPrices.put("Coke", 50.0);
        drinkPrices.put("Pepsi", 45.0);
        drinkPrices.put("Fanta", 40.0);
        drinkPrices.put("Sprite", 35.0);

        try (Socket socket = new Socket(SERVER_IP, SERVER_PORT);
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
             Scanner scanner = new Scanner(System.in)) {

            System.out.println("=== Branch Ordering System ===");
            System.out.print("Enter your name: ");
            String customerName = scanner.nextLine();

            System.out.print("Enter drink name:(Fanta,Coke,Pepsi,Sprite) ");
            String drink = scanner.nextLine();

            if (!drinkPrices.containsKey(drink)) {
                System.out.println("Invalid drink. Available options: " + drinkPrices.keySet());
                return;
            }

            System.out.print("Enter quantity: ");
            int quantity = Integer.parseInt(scanner.nextLine());

            System.out.print("Enter branch name (Nakuru, Mombasa, Kisumu, Nairobi): ");
            String branch = scanner.nextLine().trim();

            if (!branch.equalsIgnoreCase("Nakuru") &&
                    !branch.equalsIgnoreCase("Mombasa") &&
                    !branch.equalsIgnoreCase("Kisumu") &&
                    !branch.equalsIgnoreCase("Nairobi")) {
                System.out.println("Invalid branch name. Please restart and enter a valid branch.");
                return;
            }

            double price = drinkPrices.get(drink);
            double totalCost = price * quantity;
            System.out.printf("Total cost: %.2f (%.2f per unit × %d units)%n", totalCost, price, quantity);

            Order order = new Order(customerName, drink, quantity, branch);
            out.writeObject(order);
            System.out.println("✅ Order sent successfully.");

        } catch (IOException e) {
            System.err.println("Connection error: " + e.getMessage());
        }
    }
}
