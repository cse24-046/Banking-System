package banking.model;

import java.util.ArrayList;
import java.util.List;

public abstract class Customer {
    protected String customerId;
    protected String firstname;
    protected String surname;
    protected Address address;
    protected List<Account> accounts;
    
    public Customer(String customerId, String firstname, String surname, Address address) {
        this.customerId = customerId;
        this.firstname = firstname;
        this.surname = surname;
        this.address = address;
        this.accounts = new ArrayList<>();
    }
    
    // Getters and setters
    public String getCustomerId() { return customerId; }
    public void setCustomerId(String customerId) { this.customerId = customerId; }
    
    public String getFirstname() { return firstname; }
    public void setFirstname(String firstname) { this.firstname = firstname; }
    
    public String getSurname() { return surname; }
    public void setSurname(String surname) { this.surname = surname; }
    
    public Address getAddress() { return address; }
    public void setAddress(Address address) { this.address = address; }
    
    public List<Account> getAccounts() { return accounts; }
    
    
    public void addAccount(Account account) {
        if (account != null && !accounts.contains(account)) {
            accounts.add(account);
        }
    }
    
    public Account getAccountById(String accountNumber) {
        for (Account account : accounts) {
            if (account.getAccountNumber().equals(accountNumber)) {
                return account;
            }
        }
        return null;
    }
    
    public double getTotalBalance() {
        double total = 0;
        for (Account account : accounts) {
            total += account.getBalance();
        }
        return total;
    }
    
    public String getCustomerInfo() {
        return String.format("ID: %s, Name: %s %s, Address: %s, Accounts: %d, Total Balance: BWP %.2f",
                           customerId, firstname, surname, address, accounts.size(), getTotalBalance());
    }
}