package model.account;

public class Admin extends Account {

    public Admin(String accountId, String username, String password, String firstName, String lastName, String email, String phone) {
        super(accountId, username, password, firstName, lastName, email, phone);
    }

    @Override
    public String getType() {
        return "admin";
    }

    @Override
    protected void addAccountToDatabase() {

    }

    @Override
    protected void removeAccountFromDatabase() {

    }

    @Override
    protected void loadDatabase() {

    }

    @Override
    protected void updateAccountInDatabase(String username) {

    }
}
