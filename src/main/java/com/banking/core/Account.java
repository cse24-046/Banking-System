package com.banking.core;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public abstract class Account {
    protected String accountNumber;
    protected String customerId;
    protected double balance;
    protected LocalDateTime openingDate;
    protected List<String> transactionHistory;

    public Account(String accountNumber, String customerId, double initialBalance) {
        this.accountNumber = accountNumber;
        this.customerId = customerId;
        this.balance = initialBalance;
        this.openingDate = LocalDateTime.now();
        this.transactionHistory = new ArrayList<>();
        addTransaction("Account opened with balance: " + initialBalance);
    }

    // Getters
    public String getAccountNumber() { return accountNumber; }
    public String getCustomerId() { return customerId; }
    public double getBalance() { return balance; }
    public LocalDateTime getOpeningDate() { return openingDate; }
    public List<String> getTransactionHistory() { return transactionHistory; }

    public void deposit(double amount) {
        if (amount > 0) {
            balance += amount;
            addTransaction("Deposit: +" + amount + " | Balance: " + balance);
        }
    }

    protected void addTransaction(String transaction) {
        String timestamp = LocalDateTime.now().toString();
        transactionHistory.add(timestamp + " - " + transaction);
    }

    public abstract String getAccountType();
    public abstract boolean canWithdraw();
}