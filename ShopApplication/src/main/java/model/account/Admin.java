package model.account;

public class Admin extends Account {
    protected static final String MANAGER_ID = ""; // TODO: set id
    protected static Admin manager = null; // head of other admins (shouldn't be suspended)

    public Admin(String username, String password, String firstName, String lastName, String email, String phone) {
        super(username, password, firstName, lastName, email, phone);
        initialize();
    }

    public static Admin getManager() {
        return manager;
    }

    public static Admin getAdminById(String accountId) {
        return (Admin) getAccountById(accountId, true);
    }

    public static Admin getAdminById(String accountId, boolean checkSuspense) {
        return (Admin) getAccountById(accountId, checkSuspense);
    }

    @Override
    public void initialize() {
        if (manager == null) {
            manager = this;
            accountId = MANAGER_ID;
        } else {
            super.initialize();
        }
    }


}
