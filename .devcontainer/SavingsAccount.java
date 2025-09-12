public class SavingsAccount extends Account implements ApplyMonthlyInterest {
    private static final double MONTHLY_RATE = 0.005;

    public SavingsAccount(String accountNumber, String branch, Customer owner) {
        super(accountNumber, branch, owner);
    }

    @Override
    public void applyMonthlyInterest() {
        deposit(getBalance() * MONTHLY_RATE);
    }
}
