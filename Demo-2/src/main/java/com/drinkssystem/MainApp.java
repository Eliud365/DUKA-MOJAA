package com.drinkssystem;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {            // Load the login.fxml file
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("login.fxml"));

            // Set up the scene
            Scene scene = new Scene(fxmlLoader.load());

            // Configure the stage
            primaryStage.setTitle("Duka Moja Login");
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static void main(String[] args) {
        launch(args); // Launches the JavaFX app
    }
}


