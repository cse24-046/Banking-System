package com.banking.core;

import java.util.ArrayList;
import java.util.List;

public abstract class Customer {
    protected String customerId;
    protected String name;
    protected String password;
    protected List<Account> accounts;

    public Customer(String customerId, String name, String password) {
        this.customerId = customerId;
        this.name = name;
        this.password = password;
        this.accounts = new ArrayList<>();
    }

    // Getters and setters
    public String getCustomerId() { return customerId; }
    public String getName() { return name; }
    public String getPassword() { return password; }
    public List<Account> getAccounts() { return accounts; }

    public void addAccount(Account account) {
        accounts.add(account);
    }

    public Account getAccount(String accountNumber) {
        return accounts.stream()
                .filter(acc -> acc.getAccountNumber().equals(accountNumber))
                .findFirst()
                .orElse(null);
    }

     // Checks if customer has an account of specific type
    public boolean hasAccountType(String accountType) {
        return accounts.stream()
                .anyMatch(acc -> acc.getAccountType().equals(accountType));
    }

     // Get the number of accounts customer has
    public int getAccountCount() {
        return accounts.size();
    }

     //Checks if customer can open another account (max 3 accounts)
    public boolean canOpenMoreAccounts() {
        return accounts.size() < 3;
    }

    
     //Check if customer can open specific account type (max 1 per type)
    public boolean canOpenAccountType(String accountType) {
        return !hasAccountType(accountType);
    }

    public abstract String getCustomerType();
}