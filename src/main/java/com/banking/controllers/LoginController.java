package com.banking.controllers;

import com.banking.Main;
import com.banking.services.AuthenticationService;
import com.banking.services.DataStorage;
import com.banking.core.Customer;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.List;

public class LoginController {
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private RadioButton customerRadio;
    @FXML private RadioButton clerkRadio;
    @FXML private Label messageLabel;

    private AuthenticationService authService;

    @FXML
    private void initialize() {
        ToggleGroup group = new ToggleGroup();
        customerRadio.setToggleGroup(group);
        clerkRadio.setToggleGroup(group);
        customerRadio.setSelected(true);
        
        // Initialize auth service
        List<Customer> customers = DataStorage.loadCustomers();
        DataStorage.loadAccounts(customers);
        this.authService = new AuthenticationService(customers);
    }

    @FXML
    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();

        if (username.isEmpty() || password.isEmpty()) {
            showMessage("Please enter both username and password", "error");
            return;
        }

        try {
            if (customerRadio.isSelected()) {
                // Customer login
                Customer customer = authService.authenticateCustomer(username, password);
                if (customer != null) {
                    openCustomerDashboard(customer);
                } else {
                    showMessage("Invalid customer ID or password", "error");
                }
            } else {
                // Bank clerk login
                if (authService.authenticateBankClerk(username, password)) {
                    openBankClerkDashboard();
                } else {
                    showMessage("Invalid admin credentials", "error");
                }
            }
        } catch (Exception e) {
            showMessage("Login failed: " + e.getMessage(), "error");
        }
    }

    private void openCustomerDashboard(Customer customer) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/banking/views/CustomerDashboard.fxml"));
        Parent root = loader.load();
        
        CustomerController controller = loader.getController();
        controller.setCustomer(customer);
        
        Stage stage = new Stage();
        stage.setTitle("Customer Dashboard - " + customer.getName());
        stage.setScene(new Scene(root, 1000, 700));
        stage.setResizable(false);
        
        // Center the new stage
        centerStage(stage);
        
        // Close login window and show customer dashboard
        Stage loginStage = (Stage) usernameField.getScene().getWindow();
        loginStage.close();
        stage.show();
    }

    private void openBankClerkDashboard() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/banking/views/bankClerkDashboard.fxml"));
        Parent root = loader.load();
        
        Stage stage = new Stage();
        stage.setTitle("Bank Clerk Dashboard");
        stage.setScene(new Scene(root, 1000, 700));
        stage.setResizable(false);
        
        // Center the new stage
        centerStage(stage);
        
        // Close login window and show bank clerk dashboard
        Stage loginStage = (Stage) usernameField.getScene().getWindow();
        loginStage.close();
        stage.show();
    }

     //Center a stage on the screen
    private void centerStage(Stage stage) {
        stage.centerOnScreen();
    }

    private void showMessage(String message, String type) {
        messageLabel.setText(message);
        if ("error".equals(type)) {
            messageLabel.setStyle("-fx-text-fill: red;");
        } else {
            messageLabel.setStyle("-fx-text-fill: green;");
        }
    }
}