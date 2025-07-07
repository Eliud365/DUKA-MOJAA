package drinkssystem;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.event.ActionEvent;

public class salesreportcontroller {

    @FXML
    private TableView<Sale> salesTable;

    @FXML
    private TableColumn<Sale, String> colName;

    @FXML
    private TableColumn<Sale, String> colDrink;

    @FXML
    private TableColumn<Sale, Double> colPrice;

    @FXML
    private TableColumn<Sale, String> colBranch;

    @FXML
    public void initialize() {
        colName.setCellValueFactory(data -> data.getValue().nameProperty());
        colDrink.setCellValueFactory(data -> data.getValue().drinkProperty());
        colPrice.setCellValueFactory(data -> data.getValue().priceProperty().asObject());
        colBranch.setCellValueFactory(data -> data.getValue().branchProperty());

        // dummy data
        ObservableList<Sale> data = FXCollections.observableArrayList(
                new Sale("Alma", "Coke", 80.0, "Nakuru"),
                new Sale("Brian", "Fanta", 70.0, "Kisumu")
        );

        salesTable.setItems(data);
    }

    @FXML
    public void handleBack(ActionEvent event) {
        // TODO: add code to switch back to admin dashboard
    }
}

