package banking.model;

public class InvestmentAccount extends Account implements Withdrawable, ApplyMonthlyInterest {
    public static final double MONTHLY_RATE = 0.05; // 5%
    private static final double MIN_OPENING_BALANCE = 500.0;
    
    public InvestmentAccount(String accountNumber, Customer owner, String branch, double initialDeposit) {
        super(accountNumber, owner, branch);
        
        if (initialDeposit >= MIN_OPENING_BALANCE) {
            this.balance = initialDeposit;
        } else {
            throw new IllegalArgumentException(
                "Investment account requires minimum opening balance of BWP " + MIN_OPENING_BALANCE);
        }
    }
    
    @Override
    public void applyMonthlyInterest() {
        double interest = balance * MONTHLY_RATE;
        balance += interest;
    }
    
    @Override
    public void withdraw(double amount) {
        if (amount > 0 && amount <= balance) {
            balance -= amount;
        } else {
            throw new IllegalArgumentException("Invalid withdrawal amount or insufficient funds");
        }
    }
    
    public double calculateProjectedInterest() {
        return balance * MONTHLY_RATE;
    }
}