package banking.model;

public class CompanyCustomer extends Customer {
    private String companyRegistrationNumber;
    
    public CompanyCustomer(String customerId, String companyName, Address address, String registrationNumber) {
        super(customerId, companyName, "Ltd", address); // Using company name as firstname
        this.companyRegistrationNumber = registrationNumber;
    }
    
    public String getCompanyRegistrationNumber() {
        return companyRegistrationNumber;
    }
    
    public void setCompanyRegistrationNumber(String registrationNumber) {
        this.companyRegistrationNumber = registrationNumber;
    }
    
    public String getCompanyName() {
        return getFirstname(); // Company name is stored in firstname field
    }
    
    @Override
    public String getCustomerInfo() {
        return String.format("Company Customer - Name: %s, Reg: %s, Address: %s, Accounts: %d", 
                           getCompanyName(), companyRegistrationNumber, getAddress(), getAccounts().size());
    }
}