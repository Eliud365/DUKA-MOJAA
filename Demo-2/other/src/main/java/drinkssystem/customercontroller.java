package drinkssystem;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.fxml.Initializable;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.HashMap;
import java.util.Map;

public class customercontroller implements Initializable {

    @FXML
    private TextField nameField;

    @FXML
    private ComboBox<String> branchComboBox;

    @FXML
    private ComboBox<String> drinkComboBox;

    @FXML
    private TextField quantityField;

    @FXML
    private TextField totalPriceField;

    // Map to store drink names and their prices
    private final Map<String, Integer> drinkPrices = new HashMap<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Populate branch combo box
        branchComboBox.getItems().addAll("Nakuru", "Mombasa", "Kisumu");

        // Define drinks and their prices
        drinkPrices.put("CocaCola", 30);
        drinkPrices.put("Fanta", 45);
        drinkPrices.put("Sprite", 48);
        drinkPrices.put("Pepsi", 55);
        drinkPrices.put("Krest", 60);

        //populate drinks in ComboBox
        drinkComboBox.getItems().addAll(drinkPrices.keySet());
    }

    @FXML
    private void handlesubmit() {
        String name = nameField.getText();
        String branch = branchComboBox.getValue();
        String drink = drinkComboBox.getValue();
        String quantityText = quantityField.getText();

        if (name.isEmpty() || branch == null || drink == null || quantityText.isEmpty()) {
            showAlert("Please fill in all fields.");
            return;
        }

        try {
            int quantity = Integer.parseInt(quantityText);
            // Get the price from the map
            Integer pricePerDrink = drinkPrices.get(drink);

            if (pricePerDrink == null) {
                showAlert("Selected drink not found in the price list.");
                return;
            }
            int totalPrice = pricePerDrink * quantity;

            totalPriceField.setText(String.valueOf(totalPrice));
            showAlert("Order submitted! Total: Ksh " + totalPrice);

        } catch (NumberFormatException e) {
            showAlert("Please enter a valid number for quantity.");
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Order Info");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void handleback(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/login.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }
}


