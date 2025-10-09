package banking.model;

public class IndividualCustomer extends Customer {
    
    public IndividualCustomer(String customerId, String firstname, String surname, Address address) {
        super(customerId, firstname, surname, address);
    }
    
    @Override
    public String getCustomerInfo() {
        return "Individual Customer - " + super.getCustomerInfo();
    }
}