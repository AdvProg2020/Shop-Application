package model.account;

public class Admin extends Account {
    private static boolean firstAdmin = true;

    public Admin(String username, String password, String firstName, String lastName, String email, String phone) {
        super(username, password, firstName, lastName, email, phone);
        firstAdmin = false;
    }

    public static Admin getAdminById(String accountId) {
        return (Admin) Account.getAccountById(accountId);
    }

    public static boolean isFirstAdmin() {
        return firstAdmin;
    }

    @Override
    public String getType() {
        return "admin";
    }
}
