package drinkssystem;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.stage.Stage;

import java.io.IOException;

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
    public void handleBack(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/admin.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }
}
