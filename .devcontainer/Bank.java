import java.util.ArrayList;
import java.util.List;

public class Bank {
    private String name;
    private String branchCode;
    private String customerId;
    private String accountNo;
    private List<Customer> customers;

    public Bank(String name, String branchCode) {
        this.name = name;
        this.branchCode = branchCode;
        this.customers = new ArrayList<>();
    }

    public Customer registerCustomer(String customerId, String firstname, String surname, Address address) {
        Customer customer = new Customer(customerId, firstname, surname, address);
        customers.add(customer);
        return customer;
    }

    public SavingsAccount openSavings(Customer customer, String accNo) {
        SavingsAccount sa = new SavingsAccount(accNo, branchCode, customer);
        customer.addAccount(sa);
        return sa;
    }

    public InvestmentAccount openInvestment(Customer customer, String accNo) {
        InvestmentAccount ia = new InvestmentAccount(accNo, branchCode, customer);
        customer.addAccount(ia);
        return ia;
    }

    public ChequeAccount openCheque(Customer customer, String accNo, EmploymentInfo empInfo) {
        ChequeAccount ca = new ChequeAccount(accNo, branchCode, customer, empInfo);
        customer.addAccount(ca);
        return ca;
    }
}

