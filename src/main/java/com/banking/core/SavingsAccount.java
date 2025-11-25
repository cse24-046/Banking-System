package com.banking.core;

import com.banking.interfaces.InterestBearing;

public class SavingsAccount extends Account implements InterestBearing {
    public SavingsAccount(String accountNumber, String customerId, double initialBalance) {
        super(accountNumber, customerId, initialBalance);
    }

    @Override
    public String getAccountType() {
        return "SAVINGS";
    }

    @Override
    public boolean canWithdraw() {
        return false; // Savings account does not allow withdrawals
    }

    @Override
    public void applyMonthlyInterest() {
        double interest = balance * getInterestRate();
        balance += interest;
        addTransaction("Interest Applied: +" + interest + " | Balance: " + balance);
    }

    @Override
    public double getInterestRate() {
        return 0.025; 
    }
}