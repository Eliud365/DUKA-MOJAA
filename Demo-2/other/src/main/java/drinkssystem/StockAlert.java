package drinkssystem;

import javafx.beans.property.*;

public class StockAlert {
    private final StringProperty drink;
    private final StringProperty branch;
    private final IntegerProperty quantity;

    public StockAlert(String drink, String branch, int quantity) {
        this.drink = new SimpleStringProperty(drink);
        this.branch = new SimpleStringProperty(branch);
        this.quantity = new SimpleIntegerProperty(quantity);
    }

    public StringProperty drinkProperty() { return drink; }
    public StringProperty branchProperty() { return branch; }
    public IntegerProperty quantityProperty() { return quantity; }
}
