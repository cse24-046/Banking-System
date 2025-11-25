package com.banking.core;

public class IndividualCustomer extends Customer {
    public IndividualCustomer(String customerId, String name, String password) {
        super(customerId, name, password);
    }

    @Override
    public String getCustomerType() {
        return "INDIVIDUAL";
    }
}