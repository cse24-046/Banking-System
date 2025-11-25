package com.banking.controllers;

import com.banking.services.DataStorage;
import com.banking.services.TransactionService;
import com.banking.core.Customer;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class BankClerkController {
    @FXML private TabPane mainTabPane;
    @FXML private ListView<String> customersListView;
    @FXML private Label statsLabel;
    @FXML private ComboBox<String> customerFilterCombo;
    @FXML private TextArea customerDetailsArea;

    private List<Customer> customers;
    private List<Customer> filteredCustomers;
    private TransactionService transactionService;

    @FXML
    private void initialize() {
        // Initializing filter options
        customerFilterCombo.getItems().addAll("All Customers", "Individual Only", "Company Only");
        customerFilterCombo.getSelectionModel().select(0);
        
        
        customerFilterCombo.valueProperty().addListener((obs, oldVal, newVal) -> {
            filterCustomers();
        });
        
        customersListView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            showCustomerDetails(newVal);
        });
        
        loadData();
        refreshCustomersList();
        updateStatistics();
    }

    private void loadData() {
        this.customers = DataStorage.loadCustomers();
        DataStorage.loadAccounts(this.customers);
        this.transactionService = new TransactionService();
        this.filteredCustomers = customers;
    }

    private void filterCustomers() {
        String filter = customerFilterCombo.getValue();
        if (filter == null) return;
        
        switch (filter) {
            case "Individual Only":
                filteredCustomers = customers.stream()
                        .filter(c -> c.getCustomerType().equals("INDIVIDUAL"))
                        .collect(Collectors.toList());
                break;
            case "Company Only":
                filteredCustomers = customers.stream()
                        .filter(c -> c.getCustomerType().equals("COMPANY"))
                        .collect(Collectors.toList());
                break;
            case "All Customers":
            default:
                filteredCustomers = customers;
                break;
        }
        refreshCustomersList();
    }

    private void refreshCustomersList() {
        customersListView.getItems().clear();
        for (Customer customer : filteredCustomers) {
            String customerInfo = String.format("%s - %s (%s) - %d account(s)", 
                customer.getCustomerId(),
                customer.getName(),
                customer.getCustomerType(),
                customer.getAccounts().size());
            customersListView.getItems().add(customerInfo);
        }
        
        // Clears details when list refreshes
        customerDetailsArea.clear();
    }
    

    private void showCustomerDetails(String selectedCustomer) {
        if (selectedCustomer == null) {
            customerDetailsArea.clear();
            return;
        }
        
        // Extracts customer ID from the list
        String customerId = selectedCustomer.split(" - ")[0];
        Customer customer = findCustomerById(customerId);
        
        if (customer != null) {
            StringBuilder details = new StringBuilder();
            details.append("CUSTOMER DETAILS\n");
            details.append("================\n\n");
            details.append("Customer ID: ").append(customer.getCustomerId()).append("\n");
            details.append("Name: ").append(customer.getName()).append("\n");
            details.append("Type: ").append(customer.getCustomerType()).append("\n");
            
            if (customer.getAccounts().isEmpty()) {
                details.append("\nNo accounts found.\n");
            } else {
                details.append("\nACCOUNTS:\n");
                details.append("---------\n");
                
                for (int i = 0; i < customer.getAccounts().size(); i++) {
                    var account = customer.getAccounts().get(i);
                    details.append("\n").append(i + 1).append(". ").append(account.getAccountType()).append(" Account\n");
                    details.append("   Account Number: ").append(account.getAccountNumber()).append("\n");
                    details.append("   Balance: BWP").append(String.format("%.2f", account.getBalance())).append("\n");
                    
                    // Shows account-specific details
                    if (account.getAccountType().equals("CHEQUE")) {
                        var chequeAccount = (com.banking.core.ChequeAccount) account;
                        details.append("   Employer: ").append(chequeAccount.getEmployerName()).append("\n");
                        details.append("   Company Address: ").append(chequeAccount.getCompanyAddress()).append("\n");
                    }
                }
            }
            
            customerDetailsArea.setText(details.toString());
        }
    }

    private Customer findCustomerById(String customerId) {
        return customers.stream()
                .filter(c -> c.getCustomerId().equals(customerId))
                .findFirst()
                .orElse(null);
    }

    private void updateStatistics() {
        int totalCustomers = customers.size();
        int totalAccounts = customers.stream()
                .mapToInt(c -> c.getAccounts().size())
                .sum();
        
        double totalBalance = customers.stream()
                .flatMap(c -> c.getAccounts().stream())
                .mapToDouble(acc -> acc.getBalance())
                .sum();

        statsLabel.setText(String.format(
            "Total Customers: %d | Total Accounts: %d | Total Bank Balance: BWP%.2f",
            totalCustomers, totalAccounts, totalBalance
        ));
    }

    @FXML
    private void handleApplyInterest() {
        try {
            transactionService.applyMonthlyInterest(customers);
            showAlert("Success", "Monthly interest applied to all applicable accounts.");
            refreshData(); // This will reload the data and show updated balances
        } catch (Exception e) {
            showAlert("Error", "Failed to apply interest: " + e.getMessage());
        }
    }

    @FXML
    private void handleOpenAccountTab() {
        try {
            mainTabPane.getSelectionModel().select(1);
        } catch (Exception e) {
            showAlert("Error", "Failed to open account tab: " + e.getMessage());
        }
    }
  
    public void setAccountOpeningController(AccountOpeningController controller) {
        controller.setBankClerkController(this);
    }
    @FXML
    private void handleRefresh() {
        refreshData();
        showAlert("Refreshed", "Customer data has been refreshed.");
    }

    
     // Refresh all data
    public void refreshData() {
        loadData();
        filterCustomers();
        updateStatistics();
    }

    @FXML
    private void handleLogout() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/banking/views/login.fxml"));
            Parent root = loader.load();
            
            Stage stage = new Stage();
            stage.setTitle("Banking System - Login");
            stage.setScene(new Scene(root, 800, 600));
            stage.setResizable(false);
            
            // Center the login stage
            stage.centerOnScreen();
            
            // Close current window and show login
            Stage currentStage = (Stage) mainTabPane.getScene().getWindow();
            currentStage.close();
            stage.show();
            
        } catch (IOException e) {
            showAlert("Error", "Failed to logout: " + e.getMessage());
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}