package banking.model;

public abstract class Account {
    protected String accountNumber;
    protected double balance;
    protected String branch;
    protected Customer owner;
    
    public Account(String accountNumber, Customer owner, String branch) {
        this.accountNumber = accountNumber;
        this.owner = owner;
        this.branch = branch;
        this.balance = 0.0;
        
        //adding this account to customer's accounts
        if (owner != null) {
            owner.addAccount(this);
        }
    }
    
    // Getters
    public String getAccountNumber() { return accountNumber; }
    public double getBalance() { return balance; }
    public String getBranch() { return branch; }
    public Customer getOwner() { return owner; }
    
    // Business logic
    public void deposit(double amount) {
        if (amount > 0) {
            balance += amount;
        } else {
            throw new IllegalArgumentException("Deposit amount must be positive");
        }
    }
    
    public String getAccountInfo() {
        return String.format("Account: %s, Balance: BWP %.2f, Branch: %s, Type: %s", 
                           accountNumber, balance, branch, this.getClass().getSimpleName());
    }
    
    @Override
    public String toString() {
        return getAccountInfo();
    }
}