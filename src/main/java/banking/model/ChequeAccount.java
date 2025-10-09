package banking.model;

public class ChequeAccount extends Account implements Withdrawable {
    private EmploymentInfo employmentInfo;
    
    public ChequeAccount(String accountNumber, Customer owner, String branch, EmploymentInfo employmentInfo) {
        super(accountNumber, owner, branch);
        
        if (employmentInfo == null) {
            throw new IllegalArgumentException("Employment information is required for Cheque accounts");
        }
        this.employmentInfo = employmentInfo;
    }
    
    public EmploymentInfo getEmploymentInfo() {
        return employmentInfo;
    }
    
    public void setEmploymentInfo(EmploymentInfo employmentInfo) {
        this.employmentInfo = employmentInfo;
    }
    
    @Override
    public void withdraw(double amount) {
        if (amount > 0) {
            // Cheque accounts can go into overdraft
            balance -= amount;
        } else {
            throw new IllegalArgumentException("Invalid withdrawal amount");
        }
    }
    
    @Override
    public String getAccountInfo() {
        return super.getAccountInfo() + ", Company: " + employmentInfo.getCompanyName();
    }
}