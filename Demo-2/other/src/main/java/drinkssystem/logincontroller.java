package drinkssystem;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;

import java.io.IOException;

public class logincontroller {

        @FXML
        private Button customerButton;

        @FXML
        private Button adminButton;

        @FXML
        private void handleCustomerLogin(ActionEvent event) {
            try {
                Parent customerRoot = FXMLLoader.load(getClass().getResource("/customer.fxml"));
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(new Scene(customerRoot));
                stage.setTitle("Customer Portal");
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @FXML
        private void handleAdminLogin(ActionEvent event) {
            try {
                Parent adminRoot = FXMLLoader.load(getClass().getResource("/admin.fxml"));
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(new Scene(adminRoot));
                stage.setTitle("Admin Portal");
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
        }
        }
    }


