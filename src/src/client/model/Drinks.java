package client.model;



import java.io.Serializable;

public class Drinks implements Serializable {
    private String name;
    private double price;
    private int quantity;

    public Drinks(String name, double price, int quantity) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    public String getName() { return name; }
    public double getPrice() { return price; }
    public int getQuantity() { return quantity; }
    public void reduceQuantity(int amount) { this.quantity -= amount; }

    @Override
    public String toString() {
        return name + " - $" + price + " (Qty: " + quantity + ")";
    }
}
