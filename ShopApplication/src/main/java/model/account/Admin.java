package model.account;

import model.BasicMethods;

public class Admin extends Account {
    protected static final String MANAGER_ID = ""; // TODO: set id
    protected static Admin manager = null; // head of other admins (shouldn't be suspended)
    private static int lastNum = 1;

    public Admin(String username, String password, String firstName, String lastName, String email, String phone) {
        super(username, password, firstName, lastName, email, phone);
        initialize();
    }

    public static Admin getManager() {
        return manager;
    }

    public static Admin getAdminById(String accountId, boolean... suspense) {
        return (Admin) getAccountById(accountId, suspense);
    }

    @Override
    public void initialize() {
        if (manager == null) {
            manager = this;
            accountId = MANAGER_ID;
        } else {
            if (accountId == null)
                accountId = BasicMethods.generateNewId(getClass().getSimpleName(), lastNum);
            allAccounts.put(accountId, this);
            lastNum++;
        }

    }


}
