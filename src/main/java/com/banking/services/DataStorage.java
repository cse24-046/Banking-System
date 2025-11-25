package com.banking.services;

import com.banking.core.*;
import com.banking.utils.FileHandler;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataStorage {
    private static final String CUSTOMERS_FILE = "customers.txt";
    private static final String ACCOUNTS_FILE = "accounts.txt";
    private static final String TRANSACTIONS_FILE = "transactions.txt";

    // Customer data methods
    public static void saveCustomer(Customer customer) {
        String data = customer.getCustomerId() + "|" + 
                     customer.getName() + "|" + 
                     customer.getPassword() + "|" + 
                     customer.getCustomerType() + "|" +
                     (customer instanceof CompanyCustomer ? 
                      ((CompanyCustomer) customer).getCompanyRegistrationNumber() : "");
        FileHandler.writeToFile(CUSTOMERS_FILE, data);
    }

    public static List<Customer> loadCustomers() {
        List<Customer> customers = new ArrayList<>();
        List<String> lines = FileHandler.readFromFile(CUSTOMERS_FILE);

        for (String line : lines) {
            String[] parts = line.split("\\|");
            if (parts.length >= 4) {
                String customerId = parts[0];
                String name = parts[1];
                String password = parts[2];
                String type = parts[3];

                if ("INDIVIDUAL".equals(type)) {
                    customers.add(new IndividualCustomer(customerId, name, password));
                } else if ("COMPANY".equals(type)) {
                    String regNumber = parts.length > 4 ? parts[4] : "";
                    customers.add(new CompanyCustomer(customerId, name, password, regNumber));
                }
            }
        }
        return customers;
    }

    // Account data methods
    public static void saveAccount(Account account) {
        StringBuilder data = new StringBuilder();
        data.append(account.getAccountNumber()).append("|")
            .append(account.getCustomerId()).append("|")
            .append(account.getBalance()).append("|")
            .append(account.getAccountType()).append("|");

        if (account instanceof ChequeAccount) {
            ChequeAccount chequeAccount = (ChequeAccount) account;
            data.append(chequeAccount.getEmployerName()).append("|")
                .append(chequeAccount.getCompanyAddress());
        }

        FileHandler.writeToFile(ACCOUNTS_FILE, data.toString());
        
        // Save initial transaction
        saveTransaction(account.getCustomerId(), account.getAccountNumber(), 
                       "ACCOUNT_OPENED", account.getBalance(), account.getBalance());
    }

    public static List<Account> loadAccounts(List<Customer> customers) {
        List<Account> accounts = new ArrayList<>();
        List<String> lines = FileHandler.readFromFile(ACCOUNTS_FILE);

        for (String line : lines) {
            String[] parts = line.split("\\|");
            if (parts.length >= 4) {
                String accountNumber = parts[0];
                String customerId = parts[1];
                double balance = Double.parseDouble(parts[2]);
                String accountType = parts[3];

                Customer customer = findCustomerById(customers, customerId);
                if (customer != null) {
                    Account account = null;
                    switch (accountType) {
                        case "SAVINGS":
                            account = new SavingsAccount(accountNumber, customerId, balance);
                            break;
                        case "INVESTMENT":
                            account = new InvestmentAccount(accountNumber, customerId, balance);
                            break;
                        case "CHEQUE":
                            if (parts.length >= 6) {
                                String employer = parts[4];
                                String companyAddress = parts[5];
                                account = new ChequeAccount(accountNumber, customerId, balance, employer, companyAddress);
                            }
                            break;
                    }
                    if (account != null) {
                        accounts.add(account);
                        customer.addAccount(account);
                    }
                }
            }
        }
        return accounts;
    }

    // Transaction data methods
    public static void saveTransaction(String customerId, String accountNumber, 
                                     String transactionType, double amount, double balance) {
        String data = customerId + "|" + accountNumber + "|" + transactionType + "|" + 
                     amount + "|" + balance + "|" + System.currentTimeMillis();
        FileHandler.writeToFile(TRANSACTIONS_FILE, data);
    }

    public static List<Transaction> loadTransactions(String accountNumber) {
        List<Transaction> transactions = new ArrayList<>();
        List<String> lines = FileHandler.readFromFile(TRANSACTIONS_FILE);

        for (String line : lines) {
            String[] parts = line.split("\\|");
            if (parts.length >= 6 && parts[1].equals(accountNumber)) {
                String customerId = parts[0];
                String accNumber = parts[1];
                String type = parts[2];
                double amount = Double.parseDouble(parts[3]);
                double balance = Double.parseDouble(parts[4]);
                long timestamp = Long.parseLong(parts[5]);
                
                transactions.add(new Transaction(customerId, accNumber, type, amount, balance, timestamp));
            }
        }
        return transactions;
    }

    //Update account balances in the file
    public static void updateAccountBalances(List<Customer> customers) {
        List<String> allAccountLines = new ArrayList<>();
        
        // Read all existing accounts
        List<String> existingLines = FileHandler.readFromFile(ACCOUNTS_FILE);
        
        // Creating a map of updated accounts for quick lookup
        Map<String, Account> updatedAccounts = new HashMap<>();
        for (Customer customer : customers) {
            for (Account account : customer.getAccounts()) {
                updatedAccounts.put(account.getAccountNumber(), account);
            }
        }
        
        // Update existing lines or add new ones
        for (String line : existingLines) {
            String[] parts = line.split("\\|");
            if (parts.length >= 4) {
                String accountNumber = parts[0];
                Account updatedAccount = updatedAccounts.get(accountNumber);
                
                if (updatedAccount != null) {
                    
                    StringBuilder updatedLine = new StringBuilder();
                    updatedLine.append(updatedAccount.getAccountNumber()).append("|")
                              .append(updatedAccount.getCustomerId()).append("|")
                              .append(updatedAccount.getBalance()).append("|")
                              .append(updatedAccount.getAccountType()).append("|");
                    
                    if (updatedAccount instanceof ChequeAccount) {
                        ChequeAccount chequeAccount = (ChequeAccount) updatedAccount;
                        updatedLine.append(chequeAccount.getEmployerName()).append("|")
                                  .append(chequeAccount.getCompanyAddress());
                    }
                    
                    allAccountLines.add(updatedLine.toString());
                    updatedAccounts.remove(accountNumber);
                } else {
                    // Keeps the original line if account wasn't updated
                    allAccountLines.add(line);
                }
            }
        }
        
        // Adds any new accounts that weren't in the original file
        for (Account newAccount : updatedAccounts.values()) {
            StringBuilder newLine = new StringBuilder();
            newLine.append(newAccount.getAccountNumber()).append("|")
                   .append(newAccount.getCustomerId()).append("|")
                   .append(newAccount.getBalance()).append("|")
                   .append(newAccount.getAccountType()).append("|");
            
            if (newAccount instanceof ChequeAccount) {
                ChequeAccount chequeAccount = (ChequeAccount) newAccount;
                newLine.append(chequeAccount.getEmployerName()).append("|")
                       .append(chequeAccount.getCompanyAddress());
            }
            
            allAccountLines.add(newLine.toString());
        }
        
        // Writes all accounts back to file (overwrite)
        FileHandler.overwriteFile(ACCOUNTS_FILE, allAccountLines);
    }

    private static Customer findCustomerById(List<Customer> customers, String customerId) {
        for (Customer customer : customers) {
            if (customer.getCustomerId().equals(customerId)) {
                return customer;
            }
        }
        return null;
    }
}