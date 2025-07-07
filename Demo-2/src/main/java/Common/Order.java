package Common;

import java.io.Serializable;

public class Order implements Serializable {
    private String customerName;
    private String drinkName;
    private int quantity;
    private String branchName;

    public Order(String customerName, String drinkName, int quantity, String branchName) {
        this.customerName = customerName;
        this.drinkName = drinkName;
        this.quantity = quantity;
        this.branchName = branchName;
    }

    public String getCustomerName() { return customerName; }
    public String getDrinkName() { return drinkName; }
    public int getQuantity() { return quantity; }
    public String getBranchName() { return branchName; }
}


