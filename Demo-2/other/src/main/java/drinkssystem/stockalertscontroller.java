package drinkssystem;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.event.ActionEvent;

public class stockalertscontroller {

    @FXML
    private TableView<StockAlert> stockTable;

    @FXML
    private TableColumn<StockAlert, String> colDrink;

    @FXML
    private TableColumn<StockAlert, String> colBranch;

    @FXML
    private TableColumn<StockAlert, Integer> colQuantity;

    @FXML
    public void initialize() {
        colDrink.setCellValueFactory(data -> data.getValue().drinkProperty());
        colBranch.setCellValueFactory(data -> data.getValue().branchProperty());
        colQuantity.setCellValueFactory(data -> data.getValue().quantityProperty().asObject());

        // dummy data
        ObservableList<StockAlert> alerts = FXCollections.observableArrayList(
                new StockAlert("Coke", "Nakuru", 3),
                new StockAlert("Sprite", "Mombasa", 1)
        );

        stockTable.setItems(alerts);
    }

    @FXML
    public void handleBack(ActionEvent event) {
        // TODO: add code to switch back to admin dashboard
    }
}
