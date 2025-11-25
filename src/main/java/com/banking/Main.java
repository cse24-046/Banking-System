package com.banking;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.net.URL;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        try {
            URL fxmlUrl = getClass().getResource("/com/banking/views/login.fxml");
            if (fxmlUrl == null) {
                throw new RuntimeException("Cannot find FXML file: /com/banking/views/login.fxml");
            }
            
            FXMLLoader loader = new FXMLLoader(fxmlUrl);
            Parent root = loader.load();
            
            primaryStage.setTitle("Banking System - Login");
            Scene scene = new Scene(root, 800, 600);
            primaryStage.setScene(scene);
            primaryStage.setResizable(false);
            
            // Center the stage on screen
            centerStage(primaryStage);
            
            primaryStage.show();
            
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error loading FXML: " + e.getMessage());
        }
    }

     //Center a stage on the primary screen
    public static void centerStage(Stage stage) {
        stage.centerOnScreen();
    }

    public static void main(String[] args) {
        launch(args);
    }
}