public class Account {
    private String accountNumber;
    private double balance;
    private String branch;
    private Customer owner;

    public Account(String accountNumber, String branch, Customer owner) {
        this.accountNumber = accountNumber;
        this.branch = branch;
        this.owner = owner;
        this.balance = 0.0;
    }

    public String getAccountNumber() { return accountNumber; }
    public double getBalance() { return balance; }
    public String getBranch() { return branch; }
    public Customer getOwner() { return owner; }

    public void deposit(double amount) {
        if (amount > 0) balance += amount;
    }

    @Override
    public String toString() {
        return "Account Number: " + accountNumber + ", Balance: " + balance;
    }
}

