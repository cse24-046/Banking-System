package com.banking.services;

import com.banking.core.Customer;
import java.util.List;

public class AuthenticationService {
    private static final String ADMIN_USERNAME = "Admin";
    private static final String ADMIN_PASSWORD = "123456";
    
    private List<Customer> customers;

    public AuthenticationService(List<Customer> customers) {
        this.customers = customers;
    }

    public Customer authenticateCustomer(String customerId, String password) {
        if (customerId == null || password == null) {
            return null;
        }
        
        return customers.stream()
                .filter(c -> c.getCustomerId().equals(customerId) && c.getPassword().equals(password))
                .findFirst()
                .orElse(null);
    }

    public boolean authenticateBankClerk(String username, String password) {
        if (username == null || password == null) {
            return false;
        }
        return ADMIN_USERNAME.equals(username) && ADMIN_PASSWORD.equals(password);
    }

    public boolean customerExists(String customerId) {
        return customers.stream()
                .anyMatch(c -> c.getCustomerId().equals(customerId));
    }
    
    // Add this method to get customer by ID
    public Customer getCustomerById(String customerId) {
        return customers.stream()
                .filter(c -> c.getCustomerId().equals(customerId))
                .findFirst()
                .orElse(null);
    }
    
    public List<Customer> getCustomers() {
        return customers;
    }
}