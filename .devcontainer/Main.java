public class Main {
    public static void main(String[] args) {
        // Create a Bank (Controller)
        Bank bank = new Bank("First National Bank", "FNB001");

        // Create Address
        Address address = new Address("123 Main St", "Gaborone", "Botswana");

        // Register Customer
        Customer customer = bank.registerCustomer("CUST001", "Bianca", "Smith", address);

        // Open Accounts
        SavingsAccount savings = bank.openSavings(customer, "SA001");
        InvestmentAccount investment = bank.openInvestment(customer, "IA001");
        EmploymentInfo empInfo = new EmploymentInfo("TechCorp", "Industrial Road, Gaborone");
        ChequeAccount cheque = bank.openCheque(customer, "CA001", empInfo);

        // Transactions
        bank.deposit(savings, 1000);
        bank.deposit(investment, 2000);
        bank.deposit(cheque, 1500);

        // Withdraw
        bank.withdraw(cheque, 500);
        bank.withdraw(investment, 800);

        // Apply interest
        bank.applyInterest(savings);
        bank.applyInterest(investment);

        // Print balances
        System.out.println("=== Account Balances ===");
        for (Account acc : customer.getAccounts()) {
            System.out.println(acc);
        }
    }
}

