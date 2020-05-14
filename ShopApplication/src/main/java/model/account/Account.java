package model.account;

import model.Initializable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Account implements Initializable {
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

    protected static String generateNewId() {
        //TODO: implement
        return null;
    }

    public static List<Account> getAllAccounts(boolean... suspense) {
        boolean checkSuspense = (suspense.length == 0) || suspense[0]; // optional (default = true)

        List<Account> accounts = new ArrayList<>(allAccounts.values());
        if (checkSuspense)
            accounts.removeIf(account -> account.suspended);

        return accounts;
    }

    public static Account getAccountById(String accountId, boolean... suspense) {
        boolean checkSuspense = (suspense.length == 0) || suspense[0]; // optional (default = true)

        if (accountId.equals(Admin.MANAGER_ID))
            return Admin.manager;

        Account account = allAccounts.get(accountId);
        if (checkSuspense && account != null && account.suspended)
            account = null;

        return account;
    }

    public static Account getAccountByUsername(String username) {
        for (Account account : allAccounts.values()) {
            if (!account.suspended && account.getUsername().equals(username))
                return account;
        }

        return null;
    }

    @Override
    public void initialize() {

    }

    public void suspend() {
        suspended = true;
    }

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
