package drinkssystem;

import javafx.beans.property.*;

public class Sale {
    private final StringProperty name;
    private final StringProperty drink;
    private final DoubleProperty price;
    private final StringProperty branch;

    public Sale(String name, String drink, double price, String branch) {
        this.name = new SimpleStringProperty(name);
        this.drink = new SimpleStringProperty(drink);
        this.price = new SimpleDoubleProperty(price);
        this.branch = new SimpleStringProperty(branch);
    }

    public StringProperty nameProperty() { return name; }
    public StringProperty drinkProperty() { return drink; }
    public DoubleProperty priceProperty() { return price; }
    public StringProperty branchProperty() { return branch; }
}
