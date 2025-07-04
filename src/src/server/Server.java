package server;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Server {
    private static final int PORT = 5000;
    private InventoryManager inventory;
    private Map<String, Double> branchSales;
    private Set<String> customers;

    public Server() {
        inventory = new InventoryManager();
        branchSales = new HashMap<>();
        customers = new HashSet<>();
        DatabaseManager.initializeDatabase();
    }

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println(" Server started on port " + PORT);


            Thread reportThread = new Thread(() -> {
                try {
                    ReportGenerator reportGenerator = new ReportGenerator(branchSales, customers, inventory);
                    while (true) {
                        Thread.sleep(60000);
                        reportGenerator.generateReport();
                    }
                } catch (InterruptedException e) {
                    System.out.println("Report thread interrupted.");
                }
            });
            reportThread.setDaemon(true);
            reportThread.start();

            while (true) {
                Socket clientSocket = serverSocket.accept();
                ClientHandler handler = new ClientHandler(clientSocket, inventory, branchSales, customers);
                handler.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new Server().start();
    }
}

