package Server;

import Common.Order;

import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ClientHandler extends Thread {
    private final Socket socket;
    private final InventoryManager inventory;
    private final Map<String, Double> branchSales;
    private final Set<String> customers;

    public ClientHandler(Socket socket, InventoryManager inventory, Map<String, Double> branchSales, Set<String> customers) {
        this.socket = socket;
        this.inventory = inventory;
        this.branchSales = branchSales;
        this.customers = customers;
    }

    @Override
    public void run() {
        try {
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            Order order = (Order) in.readObject();

            System.out.println(" Order from " + order.getCustomerName() + " (" + order.getBranchName() + ")");

            boolean success = inventory.processOrder(order.getDrinkName(), order.getQuantity());

            if (success) {
                double price = inventory.getPrice(order.getDrinkName());
                double total = price * order.getQuantity();

                synchronized (branchSales) {
                    branchSales.put(order.getBranchName(),
                            branchSales.getOrDefault(order.getBranchName(), 0.0) + total);
                }

                synchronized (customers) {
                    customers.add(order.getCustomerName());
                }

                DatabaseManager.saveOrder(order.getCustomerName(), order.getDrinkName(),
                        order.getQuantity(), order.getBranchName(), total);

                System.out.println("✅ Processed " + order.getQuantity() + " x " + order.getDrinkName() +
                        " for KES " + total);
            } else {
                System.out.println(" Order failed: insufficient stock or invalid drink.");
            }

            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
