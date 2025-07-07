package Server;

import Common.Drink;

import java.util.HashMap;
import java.util.Map;

public class InventoryManager {
    private final Map<String, Drink> inventory = new HashMap<>();
    private final int LOW_STOCK_THRESHOLD = 5;

    public InventoryManager() {

        inventory.put("Coke", new Drink("Coke", 100, 20));
        inventory.put("Fanta", new Drink("Fanta", 90, 15));
        inventory.put("Sprite", new Drink("Sprite", 95, 10));
    }

    public synchronized boolean processOrder(String drinkName, int quantity) {
        if (!inventory.containsKey(drinkName)) return false;
        Drink drink = inventory.get(drinkName);
        if (drink.getQuantity() < quantity) return false;
        drink.reduceQuantity(quantity);

        if (drink.getQuantity() < LOW_STOCK_THRESHOLD) {
            System.out.println("⚠ WARNING: " + drinkName + " stock is low (" + drink.getQuantity() + ")");
        }
        return true;
    }

    public synchronized double getPrice(String drinkName) {
        return inventory.containsKey(drinkName) ? inventory.get(drinkName).getPrice() : 0;
    }

    public synchronized Map<String, Drink> getInventorySnapshot() {

        return new HashMap<>(inventory);
    }
}
