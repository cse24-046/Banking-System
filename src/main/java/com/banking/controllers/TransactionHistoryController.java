package com.banking.controllers;

import java.util.List;

import com.banking.core.Transaction;
import com.banking.services.DataStorage;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;

public class TransactionHistoryController {
    @FXML private ComboBox<String> accountComboBox;
    @FXML private TextArea transactionArea;
    @FXML private Label accountInfoLabel;

    @FXML
    private void initialize() {
        setupEventHandlers();
    }

    private void setupEventHandlers() {
        accountComboBox.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                loadTransactionHistory(newVal);
            }
        });
    }

    public void setAccountNumbers(java.util.List<String> accountNumbers) {
        accountComboBox.getItems().clear();
        accountComboBox.getItems().addAll(accountNumbers);
        if (!accountNumbers.isEmpty()) {
            accountComboBox.getSelectionModel().select(0);
        }
    }

    private void loadTransactionHistory(String accountNumber) {
        List<Transaction> transactions = DataStorage.loadTransactions(accountNumber);
        
        StringBuilder history = new StringBuilder();
        history.append("Transaction History for Account: ").append(accountNumber).append("\n\n");
        
        if (transactions.isEmpty()) {
            history.append("No transactions found.");
        } else {
            for (Transaction transaction : transactions) {
                history.append(transaction).append("\n");
            }
        }
        
        transactionArea.setText(history.toString());
        accountInfoLabel.setText("Showing transactions for: " + accountNumber);
    }

    @FXML
    private void handleRefresh() {
        String selectedAccount = accountComboBox.getValue();
        if (selectedAccount != null) {
            loadTransactionHistory(selectedAccount);
        }
    }
}