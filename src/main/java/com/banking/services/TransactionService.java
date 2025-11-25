package com.banking.services;

import com.banking.core.*;
import com.banking.interfaces.InterestBearing;
import com.banking.interfaces.Withdrawable;

import java.util.List;

public class TransactionService {
    private DataStorage dataStorage;

    public TransactionService() {
        this.dataStorage = new DataStorage();
    }

    public boolean deposit(Account account, double amount) {
        if (amount > 0) {
            account.deposit(amount);
            dataStorage.saveTransaction(account.getCustomerId(), account.getAccountNumber(), 
                                      "DEPOSIT", amount, account.getBalance());
            return true;
        }
        return false;
    }

    public boolean withdraw(Account account, double amount) {
        if (account instanceof Withdrawable) {
            Withdrawable withdrawableAccount = (Withdrawable) account;
            boolean success = withdrawableAccount.withdraw(amount);
            if (success) {
                dataStorage.saveTransaction(account.getCustomerId(), account.getAccountNumber(), 
                                          "WITHDRAWAL", -amount, account.getBalance());
            }
            return success;
        }
        return false;
    }

    public void applyMonthlyInterest(List<Customer> customers) {
        for (Customer customer : customers) {
            for (Account account : customer.getAccounts()) {
                if (account instanceof InterestBearing) {
                    InterestBearing interestAccount = (InterestBearing) account;
                    
                    double oldBalance = account.getBalance();
                    
                    // Special handling for SavingsAccount to apply customer-specific rates
                    if (account instanceof SavingsAccount) {
                        double rate = (customer instanceof CompanyCustomer) ? 0.075 : 0.025;
                        double interest = account.getBalance() * rate;
                        account.deposit(interest); // Use deposit to update balance and record transaction
                        dataStorage.saveTransaction(account.getCustomerId(), account.getAccountNumber(), 
                                                  "INTEREST", interest, account.getBalance());
                    } else {
                        // Handle other interest-bearing accounts (InvestmentAccount) normally
                        interestAccount.applyMonthlyInterest();
                        double interestAmount = account.getBalance() - oldBalance;
                        
                        if (interestAmount > 0) {
                            dataStorage.saveTransaction(account.getCustomerId(), account.getAccountNumber(), 
                                                      "INTEREST", interestAmount, account.getBalance());
                        }
                    }
                }
            }
        }
        
        // CRITICAL: Save the updated balances back to file
        DataStorage.updateAccountBalances(customers);
    }
}