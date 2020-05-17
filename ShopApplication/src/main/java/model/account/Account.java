package model.account;

import model.BasicMethods;
import model.ModelBasic;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Account implements ModelBasic {
    protected static Map<String, Account> allAccounts = new HashMap<>();
    protected String accountId;
    protected boolean suspended;
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;

    protected Account(String username, String password, String firstName, String lastName, String email, String phone) {
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        suspended = false;
    }

    public static List<Account> getAllAccounts(boolean... suspense) {
        return BasicMethods.getInstances(allAccounts.values(), suspense);
    }

    public static Account getAccountById(String accountId, boolean... suspense) {
        if (accountId.equals(Admin.MANAGER_ID))
            return Admin.manager;

        return BasicMethods.getInstanceById(allAccounts, accountId, suspense);
    }

    public static Account getAccountByUsername(String username) {
        for (Account account : allAccounts.values()) {
            if (!account.suspended && account.getUsername().equals(username))
                return account;
        }

        return null;
    }

    @Override
    public abstract void initialize();

    public void suspend() {
        suspended = true;
    }

    @Override
    public boolean isSuspended() {
        return suspended;
    }

    @Override
    public String getId() {
        return accountId;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
