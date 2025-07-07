package drinkssystem;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            // Load the login.fxml file
            Parent root = FXMLLoader.load(getClass().getResource("/login.fxml"));

            // Set up the scene
            Scene scene = new Scene(root);

            // Configure the stage
            primaryStage.setTitle("Duka Moja Login");
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args); // Launches the JavaFX app
    }
}


