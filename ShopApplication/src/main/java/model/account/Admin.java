package model.account;

import model.ModelUtilities;

public class Admin extends Account {
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
        if (accountId == null)
            accountId = ModelUtilities.generateNewId(getClass().getSimpleName(), lastNum);
        allAccounts.put(accountId, this);
        lastNum++;

        if (manager == null)
            manager = this;
    }


}
