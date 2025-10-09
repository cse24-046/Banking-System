package banking.model;

public class SavingsAccount extends Account implements ApplyMonthlyInterest {
    public static final double INDIVIDUAL_RATE = 0.025;    // 2.5% for individuals
    public static final double COMPANY_RATE = 0.075;       // 7.5% for companies
    
    public SavingsAccount(String accountNumber, Customer owner, String branch) {
        super(accountNumber, owner, branch);
    }
    
    @Override
    public void applyMonthlyInterest() {
        double rate = getApplicableInterestRate();
        double interest = balance * rate;
        balance += interest;
    }
    
    public double calculateProjectedInterest() {
        double rate = getApplicableInterestRate();
        return balance * rate;
    }
    
    private double getApplicableInterestRate() {
        if (owner instanceof CompanyCustomer) {
            return COMPANY_RATE;
        } else {
            return INDIVIDUAL_RATE;
        }
    }
    
    public double getCurrentInterestRate() {
        return getApplicableInterestRate();
    }
    
    @Override
    public String getAccountInfo() {
        double currentRate = getCurrentInterestRate() * 100; // Convert to percentage
        return super.getAccountInfo() + String.format(", Interest Rate: %.1f%%", currentRate);
    }
}