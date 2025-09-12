public class InvestmentAccount extends Account implements Withdraw, ApplyMonthlyInterest {
    private static final double MONTHLY_RATE = 0.005;

    public InvestmentAccount(String accountNumber, String branch, Customer owner) {
        super(accountNumber, branch, owner);
    }

    @Override
    public void withdraw(double amount) {
        if (amount > 0 && getBalance() >= amount) {
            deposit(-amount);
        }
    }

    @Override
    public void applyMonthlyInterest() {
        deposit(getBalance() * MONTHLY_RATE);
    }
}
