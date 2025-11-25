package com.banking.core;

import com.banking.interfaces.Withdrawable;

public class ChequeAccount extends Account implements Withdrawable {
    private String employerName;
    private String companyAddress;

    public ChequeAccount(String accountNumber, String customerId, double initialBalance, 
                        String employerName, String companyAddress) {
        super(accountNumber, customerId, initialBalance);
        this.employerName = employerName;
        this.companyAddress = companyAddress;
    }

    public String getEmployerName() { return employerName; }
    public String getCompanyAddress() { return companyAddress; }

    @Override
    public String getAccountType() {
        return "CHEQUE";
    }

    @Override
    public boolean canWithdraw() {
        return true;
    }

    @Override
    public boolean withdraw(double amount) {
        if (amount > 0 && amount <= balance) {
            balance -= amount;
            addTransaction("Withdrawal: -" + amount + " | Balance: " + balance);
            return true;
        }
        return false;
    }
}