import java.util.ArrayList;
import java.util.List;

public class Customer {
    private String customerId;
    private String firstname;
    private String surname;
    private Address address;
    private List<Account> accounts;

    public Customer(String customerId, String firstname, String surname, Address address) {
        this.customerId = customerId;
        this.firstname = firstname;
        this.surname = surname;
        this.address = address;
        this.accounts = new ArrayList<>();
    }

    public String getCustomerId() { return customerId; }

    public void addAccount(Account account) {
        accounts.add(account);
    }

    public List<Account> getAccounts() {
        return accounts;
    }
}
