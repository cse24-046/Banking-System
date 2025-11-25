package com.banking.interfaces;

public interface Withdrawable {
    boolean withdraw(double amount);
    double getBalance();
}