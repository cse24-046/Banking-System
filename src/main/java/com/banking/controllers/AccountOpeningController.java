package com.banking.controllers;

import com.banking.core.*;
import com.banking.services.DataStorage;
import com.banking.services.AuthenticationService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import java.util.List;
import java.util.Random;

public class AccountOpeningController {
    @FXML private TextField customerIdField;
    @FXML private TextField customerNameField;
    @FXML private PasswordField passwordField;
    @FXML private ComboBox<String> customerTypeCombo;
    @FXML private ComboBox<String> accountTypeCombo;
    @FXML private TextField initialDepositField;
    @FXML private VBox companyFields;
    @FXML private VBox chequeFields;
    @FXML private TextField companyRegField;
    @FXML private TextField employerField;
    @FXML private TextField companyAddressField;
    @FXML private Label messageLabel;
    @FXML private VBox passwordFieldContainer;

    private AuthenticationService authService;
    private List<Customer> customers;
    private Customer existingCustomer = null;
    private BankClerkController bankClerkController; // Reference to parent controller

    public AccountOpeningController() {
        this.customers = DataStorage.loadCustomers();
        DataStorage.loadAccounts(this.customers);
        this.authService = new AuthenticationService(customers);
    }

    // Setting the parent controller for callbacks
    public void setBankClerkController(BankClerkController controller) {
        this.bankClerkController = controller;
    }

    @FXML
    private void initialize() {
        customerTypeCombo.getItems().addAll("Individual", "Company");
        accountTypeCombo.getItems().addAll("Savings", "Investment", "Cheque");
        
        customerTypeCombo.getSelectionModel().select(0);
        accountTypeCombo.getSelectionModel().select(0);
        
        companyFields.setVisible(false);
        chequeFields.setVisible(false);
        
        setupEventHandlers();
    }

    private void setupEventHandlers() {
        customerTypeCombo.valueProperty().addListener((obs, oldVal, newVal) -> {
            companyFields.setVisible("Company".equals(newVal));
        });

        accountTypeCombo.valueProperty().addListener((obs, oldVal, newVal) -> {
            chequeFields.setVisible("Cheque".equals(newVal));
            
            if ("Investment".equals(newVal)) {
                initialDepositField.setText("500.00");
                initialDepositField.setEditable(true);
            } else {
                initialDepositField.setEditable(true);
                initialDepositField.clear();
            }
        });

        customerIdField.textProperty().addListener((obs, oldVal, newVal) -> {
            checkExistingCustomer(newVal);
        });
    }

    private void checkExistingCustomer(String customerId) {
        if (customerId != null && !customerId.trim().isEmpty()) {
            existingCustomer = authService.getCustomerById(customerId);
            if (existingCustomer != null) {
                customerNameField.setText(existingCustomer.getName());
                customerNameField.setDisable(true);
                passwordFieldContainer.setVisible(false);
                
                if (existingCustomer instanceof IndividualCustomer) {
                    customerTypeCombo.setValue("Individual");
                } else if (existingCustomer instanceof CompanyCustomer) {
                    customerTypeCombo.setValue("Company");
                    CompanyCustomer company = (CompanyCustomer) existingCustomer;
                    companyRegField.setText(company.getCompanyRegistrationNumber());
                }
                customerTypeCombo.setDisable(true);
                
                showMessage("Existing customer found: " + existingCustomer.getName(), "info");
            } else {
                resetFormForNewCustomer();
            }
        } else {
            resetFormForNewCustomer();
        }
    }

    private void resetFormForNewCustomer() {
        existingCustomer = null;
        customerNameField.clear();
        customerNameField.setDisable(false);
        passwordFieldContainer.setVisible(true);
        customerTypeCombo.setDisable(false);
        companyRegField.clear();
        messageLabel.setText("");
    }

    @FXML
    private void handleOpenAccount() {
        try {
            String customerId = customerIdField.getText().trim();
            String customerName = customerNameField.getText().trim();
            String password = passwordField.getText().trim();
            String customerType = customerTypeCombo.getValue();
            String accountType = accountTypeCombo.getValue();
            double initialDeposit = Double.parseDouble(initialDepositField.getText());

            if (customerId.isEmpty() || customerName.isEmpty()) {
                showMessage("Please fill in all required fields", "error");
                return;
            }

            if (existingCustomer == null && password.isEmpty()) {
                showMessage("Password is required for new customers", "error");
                return;
            }

            Customer customer = null;
            if (existingCustomer != null) {
                customer = existingCustomer;

                if (customer.getAccounts().size() >= 3) {
                    showMessage("Customer already has maximum of 3 accounts. Cannot open more accounts.", "error");
                    return;
                }

                if (hasAccountType(customer, accountType)) {
                    showMessage("Customer already has a " + accountType + " account. Each customer can have only one account of each type.", "error");
                    return;
                }
            } else {
                if ("Individual".equals(customerType)) {
                    customer = new IndividualCustomer(customerId, customerName, password);
                } else if ("Company".equals(customerType)) {
                    String regNumber = companyRegField.getText();
                    if (regNumber.isEmpty()) {
                        showMessage("Company registration number is required", "error");
                        return;
                    }
                    customer = new CompanyCustomer(customerId, customerName, password, regNumber);
                }
                
                if (customer != null) {
                    DataStorage.saveCustomer(customer);
                    customers.add(customer);
                }
            }

            if ("Investment".equals(accountType) && !InvestmentAccount.isValidOpeningBalance(initialDeposit)) {
                showMessage("Investment account requires minimum BWP500.00 initial deposit", "error");
                return;
            }

            if ("Cheque".equals(accountType)) {
                String employer = employerField.getText();
                String companyAddress = companyAddressField.getText();
                if (employer.isEmpty() || companyAddress.isEmpty()) {
                    showMessage("Employer information is required for cheque account", "error");
                    return;
                }
            }

            if (customer != null) {
                String accountNumber = generateAccountNumber();
                Account account = createAccount(accountNumber, customerId, accountType, initialDeposit);
                
                if (account != null) {
                    customer.addAccount(account);
                    DataStorage.saveAccount(account);
                    
                    // Shows success alert
                    showSuccessAlert(accountNumber, accountType, customer.getName(), initialDeposit);
                    
                    // Refreshes the customer list
                    if (bankClerkController != null) {
                        bankClerkController.refreshData();
                    }
                    
                    clearForm();
                }
            }

        } catch (NumberFormatException e) {
            showMessage("Please enter a valid initial deposit amount", "error");
        } catch (Exception e) {
            showMessage("Error opening account: " + e.getMessage(), "error");
        }
    }

    private void showSuccessAlert(String accountNumber, String accountType, String customerName, double initialDeposit) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Account Opened Successfully");
        alert.setHeaderText("Account Created Successfully!");
        alert.setContentText(
            "Account Details:\n\n" +
            "Account Number: " + accountNumber + "\n" +
            "Account Type: " + accountType + "\n" +
            "Customer: " + customerName + "\n" +
            "Initial Deposit: BWP" + String.format("%.2f", initialDeposit) + "\n\n" +
            "The account has been successfully opened and saved to the system."
        );
        
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.setStyle("-fx-background-color: #f0fff0; -fx-border-color: #4caf50; -fx-border-width: 2px;");
        
        alert.showAndWait();
        
        showMessage("Account " + accountNumber + " opened successfully for " + customerName, "success");
    }

    private boolean hasAccountType(Customer customer, String accountType) {
        for (Account account : customer.getAccounts()) {
            String internalAccountType = convertToInternalAccountType(accountType);
            if (account.getAccountType().equals(internalAccountType)) {
                return true;
            }
        }
        return false;
    }

    private String convertToInternalAccountType(String uiAccountType) {
        switch (uiAccountType) {
            case "Savings": return "SAVINGS";
            case "Investment": return "INVESTMENT";
            case "Cheque": return "CHEQUE";
            default: return uiAccountType.toUpperCase();
        }
    }

    private Account createAccount(String accountNumber, String customerId, String accountType, double initialDeposit) {
        switch (accountType) {
            case "Savings":
                return new SavingsAccount(accountNumber, customerId, initialDeposit);
            case "Investment":
                return new InvestmentAccount(accountNumber, customerId, initialDeposit);
            case "Cheque":
                String employer = employerField.getText();
                String companyAddress = companyAddressField.getText();
                return new ChequeAccount(accountNumber, customerId, initialDeposit, employer, companyAddress);
            default:
                return null;
        }
    }

    private String generateAccountNumber() {
        Random random = new Random();
        return "ACC" + (100000 + random.nextInt(900000));
    }

    private void clearForm() {
        customerIdField.clear();
        customerNameField.clear();
        passwordField.clear();
        initialDepositField.clear();
        companyRegField.clear();
        employerField.clear();
        companyAddressField.clear();
        customerTypeCombo.getSelectionModel().select(0);
        accountTypeCombo.getSelectionModel().select(0);
        resetFormForNewCustomer();
    }

    private void showMessage(String message, String type) {
        messageLabel.setText(message);
        switch (type) {
            case "error":
                messageLabel.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
                break;
            case "success":
                messageLabel.setStyle("-fx-text-fill: green; -fx-font-weight: bold;");
                break;
            case "info":
                messageLabel.setStyle("-fx-text-fill: blue; -fx-font-weight: bold;");
                break;
            default:
                messageLabel.setStyle("-fx-text-fill: black;");
        }
    }
}