package model.account;

import java.util.HashMap;

public abstract class Account {
    private static HashMap<String, Account> allAccounts = new HashMap<String, Account>();
    protected String accountId;
    protected String username;
    protected String password;
    protected String fullName;
    protected String email;
    protected String phone;
    protected boolean suspended;

    public Account(String accountId, String username, String password, String fullName, String lastName, String email, String phone) {
        this.accountId = accountId;
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;

    }

    public static Account getAccountById(String accountId) {
        return null;
    }

    public static boolean isAccountWithUsername(String username){
        return false;
    }

    public static Account getAccountByUsername(String username) {
        return null;
    }

    public abstract String getType();

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
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

    protected abstract void addAccountToDatabase();

    protected abstract void removeAccountFromDatabase();

    protected abstract void loadDatabase();

    protected abstract void updateAccountInDatabase(String username);
}
