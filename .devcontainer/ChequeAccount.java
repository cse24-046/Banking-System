public class ChequeAccount extends Account implements Withdraw {
    private EmploymentInfo employmentInfo;

    public ChequeAccount(String accountNumber, String branch, Customer owner, EmploymentInfo employmentInfo) {
        super(accountNumber, branch, owner);
        this.employmentInfo = employmentInfo;
    }

    public EmploymentInfo getEmploymentInfo() {
        return employmentInfo;
    }

    @Override
    public void withdraw(double amount) {
        if (amount > 0 && getBalance() >= amount) {
            deposit(-amount);
        }
    }
}

