package model.account;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class Account {
    private static HashMap<String, Account> allAccounts = new HashMap<>();
    protected String accountId;
    protected String username;
    protected String password;
    protected String firstName;
    protected String lastName;
    protected String email;
    protected String phone;
    protected boolean suspended;

    public Account(String username, String password, String firstName, String lastName, String email, String phone) {
        accountId = getNewId();
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        suspended = false;
        allAccounts.put(accountId, this);
    }

    private static String getNewId() {
        return null;
        //TODO: implement
    }

    public static ArrayList<Account> getAllAccounts() {
        return (ArrayList<Account>) allAccounts.values();
    }

    public static Account getAccountById(String accountId) {
        return allAccounts.get(accountId);
    }

    public static Account getAccountByUsername(String username) {
        for (Account account : allAccounts.values()) {
            if (account.getUsername().equals(username)) {
                return account;//TODO: if not suspended
            }
        }
        return null;
    }

    public abstract String getType();

    public String getAccountId() {
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

    public boolean isSuspended() {
        return suspended;
    }

    public void suspend() {
        suspended = true;
    }
}
