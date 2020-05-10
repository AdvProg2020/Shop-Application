package model.account;

public class Admin extends Account {
    private static Admin manager = null;

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

    public static Admin getManager() {
        return manager;
    }

    @Override
    public void initialize() {
        super.initialize();
        if (manager == null) {
            manager = this;
        }
    }

    @Override
    public String getType() {
        return "Admin";
    }

}
