package com.drinkssystem;

import javafx.beans.property.*;
import javafx.beans.value.ObservableValue;

public class Sale {
    private final StringProperty name;
    private final StringProperty drink;
    private final DoubleProperty price;
    private final StringProperty branch;
    private final DoubleProperty quantity;

    public Sale(String name, String drink, double price, String branch,double quantity) {
        this.name = new SimpleStringProperty(name);
        this.drink = new SimpleStringProperty(drink);
        this.price = new SimpleDoubleProperty(price);
        this.branch = new SimpleStringProperty(branch);
        this.quantity = new SimpleDoubleProperty(quantity);
    }

    public StringProperty nameProperty() { return name; }
    public StringProperty drinkProperty() { return drink; }
    public DoubleProperty priceProperty() { return price; }
    public StringProperty branchProperty() { return branch; }
    public DoubleProperty quantityProperty(){return quantity;}
}
