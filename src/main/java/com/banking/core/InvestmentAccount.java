package com.banking.core;

import com.banking.interfaces.InterestBearing;
import com.banking.interfaces.Withdrawable;

public class InvestmentAccount extends Account implements InterestBearing, Withdrawable {
    private static final double MIN_OPENING_BALANCE = 500.00;

    public InvestmentAccount(String accountNumber, String customerId, double initialBalance) {
        super(accountNumber, customerId, initialBalance);
    }

    public static boolean isValidOpeningBalance(double balance) {
        return balance >= MIN_OPENING_BALANCE;
    }

    @Override
    public String getAccountType() {
        return "INVESTMENT";
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

    @Override
    public void applyMonthlyInterest() {
        double interest = balance * getInterestRate();
        balance += interest;
        addTransaction("Interest Applied: +" + interest + " | Balance: " + balance);
    }

    @Override
    public double getInterestRate() {
        return 0.05; // 5% monthly interest
    }
}