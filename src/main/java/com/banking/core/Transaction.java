package com.banking.core;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class Transaction {
    private String customerId;
    private String accountNumber;
    private String type;
    private double amount;
    private double balance;
    private long timestamp;
    
    public Transaction(String customerId, String accountNumber, String type, 
                      double amount, double balance, long timestamp) {
        this.customerId = customerId;
        this.accountNumber = accountNumber;
        this.type = type;
        this.amount = amount;
        this.balance = balance;
        this.timestamp = timestamp;
    }
    
    // Getters
    public String getCustomerId() { return customerId; }
    public String getAccountNumber() { return accountNumber; }
    public String getType() { return type; }
    public double getAmount() { return amount; }
    public double getBalance() { return balance; }
    public long getTimestamp() { return timestamp; }
    
    // Formatted getters for table display
    public String getFormattedDate() {
        LocalDateTime dateTime = LocalDateTime.ofInstant(
            Instant.ofEpochMilli(timestamp), ZoneId.systemDefault());
        return dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }
    
    public String getFormattedTime() {
        LocalDateTime dateTime = LocalDateTime.ofInstant(
            Instant.ofEpochMilli(timestamp), ZoneId.systemDefault());
        return dateTime.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
    }
    
    public String getFormattedDateTime() {
        LocalDateTime dateTime = LocalDateTime.ofInstant(
            Instant.ofEpochMilli(timestamp), ZoneId.systemDefault());
        return dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
    
    public String getFormattedAmount() {
        return String.format("%sBWP%.2f", amount >= 0 ? "+" : "", amount);
    }
    
    public String getFormattedBalance() {
        return String.format("BWP%.2f", balance);
    }
}