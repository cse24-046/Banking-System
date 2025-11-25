package com.banking.core;

public class CompanyCustomer extends Customer {
    private String companyRegistrationNumber;

    public CompanyCustomer(String customerId, String name, String password, String companyRegistrationNumber) {
        super(customerId, name, password);
        this.companyRegistrationNumber = companyRegistrationNumber;
    }

    public String getCompanyRegistrationNumber() {
        return companyRegistrationNumber;
    }

    @Override
    public String getCustomerType() {
        return "COMPANY";
    }
}