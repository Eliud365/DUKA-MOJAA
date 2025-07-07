package Common;

import java.io.Serializable;

    public class Drink implements Serializable {
        private String name;
        private double price;
        private int quantity;

        public Drink(String name, double price, int quantity) {
            this.name = name;
            this.price = price;
            this.quantity = quantity;
        }

        public String getName() { return name; }
        public double getPrice() { return price; }
        public int getQuantity() { return quantity; }
        public void reduceQuantity(int amount) { this.quantity -= amount; }
    }



