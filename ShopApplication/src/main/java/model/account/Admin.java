package model.account;

public class Admin extends Account{

    public Admin(String accountId, String username, String password, String firstName, String lastName, String email, String phone) {
        super(accountId, username, password, firstName, lastName, email, phone);
    }

    @Override
    public String getType() {
        return "admin";
    }

    @Override
    public void addAccountToDatabase() {

    }

    @Override
    public void removeAccountFromDatabase() {

    }

    @Override
    public void loadDatabase() {

    }

    @Override
    public void updateAccountInDatabase(String username) {

    }
}
