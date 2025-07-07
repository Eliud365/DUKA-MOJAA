package Server;

import java.util.Map;
import java.util.Set;
import Common.Drink;

public class ReportGenerator {
    private final Map<String, Double> branchSales;
    private final Set<String> customers;
    private final InventoryManager inventory;

    public ReportGenerator(Map<String, Double> branchSales, Set<String> customers, InventoryManager inventory) {
        this.branchSales = branchSales;
        this.customers = customers;
        this.inventory = inventory;
    }

    public void generateReport() {
        System.out.println("\n===== SALES REPORT =====");

        System.out.println("\nCustomers who made orders:");
        for (String customer : customers) {
            System.out.println(" - " + customer);
        }

        System.out.println("\nSales per Branch:");
        double totalSales = 0;
        for (Map.Entry<String, Double> entry : branchSales.entrySet()) {
            System.out.printf(" - %s: KES %.2f%n", entry.getKey(), entry.getValue());
            totalSales += entry.getValue();
        }

        System.out.printf("\nTotal Business Sales: KES %.2f%n", totalSales);

        System.out.println("\nCurrent Inventory Levels:");
        Map<String, Drink> stock = inventory.getInventorySnapshot();
        for (Drink d : stock.values()) {
            System.out.printf(" - %s: %d units%n", d.getName(), d.getQuantity());
        }

        System.out.println("========================\n");
    }
}

