package model.account;

public class Admin extends Account {
    private static boolean firstAdmin = true;

    public Admin(String username, String password, String firstName, String lastName, String email, String phone) {
        super(username, password, firstName, lastName, email, phone);
        initialize();
    }

    public static Admin getAdminById(String accountId) {
        return getAdminById(accountId, true);
    }

    public static Admin getAdminById(String accountId, boolean checkSuspense) {
        Admin admin = (Admin) allAccounts.get(accountId);
        if (checkSuspense && admin != null && admin.suspended) {
            admin = null;
        }
        return admin;
    }

    public static boolean isFirstAdmin() {
        return firstAdmin;
    }

    @Override
    public void initialize() {
        super.initialize();
        firstAdmin = false;
    }

    @Override
    public String getType() {
        return "admin";
    }

}
