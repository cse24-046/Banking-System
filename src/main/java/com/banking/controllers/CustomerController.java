package com.banking.controllers;

import com.banking.core.*;
import com.banking.services.TransactionService;
import com.banking.services.DataStorage;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.io.IOException;
import java.util.List;

public class CustomerController {
    @FXML private Label welcomeLabel;
    @FXML private ListView<String> accountsListView;
    @FXML private TextField amountField;
    @FXML private TableView<Transaction> transactionTable;
    @FXML private ComboBox<String> accountComboBox;
    @FXML private Label balanceLabel;

    private Customer customer;
    private TransactionService transactionService;
    private ObservableList<Transaction> transactionData;

    public void setCustomer(Customer customer) {
        this.customer = customer;
        this.transactionService = new TransactionService();
        this.transactionData = FXCollections.observableArrayList();
        loadCustomerData();
    }

    private void loadCustomerData() {
        welcomeLabel.setText("Welcome, " + customer.getName() + "!");
        refreshAccounts();
        transactionTable.setItems(transactionData);
    }

    private void refreshAccounts() {
        accountsListView.getItems().clear();
        accountComboBox.getItems().clear();
        
        for (Account account : customer.getAccounts()) {
            String accountInfo = String.format("%s (%s) - Balance: BWP%.2f", 
                account.getAccountNumber(), 
                account.getAccountType(), 
                account.getBalance());
            
            accountsListView.getItems().add(accountInfo);
            accountComboBox.getItems().add(account.getAccountNumber());
        }
        
        if (!customer.getAccounts().isEmpty()) {
            accountComboBox.getSelectionModel().select(0);
            updateBalanceDisplay();
        }
    }

    @FXML
    private void handleDeposit() {
        String selectedAccount = accountComboBox.getValue();
        double amount = parseAmount();
        
        if (selectedAccount == null || amount <= 0) {
            showAlert("Error", "Please select an account and enter a valid amount.");
            return;
        }

        Account account = customer.getAccount(selectedAccount);
        if (account != null) {
            transactionService.deposit(account, amount);
            refreshAccounts();
            updateBalanceDisplay();
            showAlert("Success", "Deposit of BWP" + amount + " completed successfully.");
            amountField.clear();
        }
    }

    @FXML
    private void handleWithdraw() {
        String selectedAccount = accountComboBox.getValue();
        double amount = parseAmount();
        
        if (selectedAccount == null || amount <= 0) {
            showAlert("Error", "Please select an account and enter a valid amount.");
            return;
        }

        Account account = customer.getAccount(selectedAccount);
        if (account != null) {
            if (!account.canWithdraw()) {
                showAlert("Error", "This account type does not allow withdrawals.");
                return;
            }

            if (transactionService.withdraw(account, amount)) {
                refreshAccounts();
                updateBalanceDisplay();
                showAlert("Success", "Withdrawal of BWP" + amount + " completed successfully.");
                amountField.clear();
            } else {
                showAlert("Error", "Withdrawal failed. Check your balance or withdrawal limits.");
            }
        }
    }

    @FXML
    private void handleViewTransactions() {
        String selectedAccount = accountComboBox.getValue();
        if (selectedAccount == null) {
            showAlert("Error", "Please select an account.");
            return;
        }

        List<Transaction> transactions = DataStorage.loadTransactions(selectedAccount);
        transactionData.clear();
        transactionData.addAll(transactions);
        
        if (transactions.isEmpty()) {
            showAlert("Info", "No transactions found for this account.");
        }
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
            Stage currentStage = (Stage) welcomeLabel.getScene().getWindow();
            currentStage.close();
            stage.show();
            
        } catch (IOException e) {
            showAlert("Error", "Failed to logout: " + e.getMessage());
        }
    }

    @FXML
    private void handleAccountSelection() {
        updateBalanceDisplay();
        transactionData.clear();
    }

    private void updateBalanceDisplay() {
        String selectedAccount = accountComboBox.getValue();
        if (selectedAccount != null) {
            Account account = customer.getAccount(selectedAccount);
            if (account != null) {
                balanceLabel.setText(String.format("Balance: BWP%.2f", account.getBalance()));
            }
        }
    }

    private double parseAmount() {
        try {
            return Double.parseDouble(amountField.getText());
        } catch (NumberFormatException e) {
            return 0;
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