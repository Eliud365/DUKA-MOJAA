package drinkssystem;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;

public class admincontroller {

    @FXML
    private Button salesReportBtn;

    @FXML
    private Button stockAlertBtn;

    @FXML
    private void handleSalesReports(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/sales_reports.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Sales Reports");
        alert.setHeaderText(null);
        alert.setContentText("Basic Report.");
        alert.showAndWait();
    }

    @FXML
    private void handleStockAlerts(ActionEvent event) throws IOException {

        Parent root = FXMLLoader.load(getClass().getResource("/stock_alerts.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();

        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Stock Alerts");
        alert.setHeaderText(null);
        alert.setContentText("low stock alerts per branch.");
        alert.showAndWait();
    }

    @FXML
    private void handleLogout(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/login.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }
}

