package model.account;

import model.ModelUtilities;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Supporter extends Account { //TODO: add chat
    protected static Map<String, Supporter> allSupporters = new HashMap<>();
    private static int lastNum = 1;

    public Supporter(String username, String password, String firstName, String lastName, String email, String phone, String image) {
        super(username, password, firstName, lastName, email, phone, image);
        initialize();
    }


    public static List<Supporter> getAllSupporters(boolean... suspense) {
        return ModelUtilities.getAllInstances(allSupporters.values(), suspense);
    }

    public static Supporter getSupporterById(String accountId, boolean... suspense) {
        return ModelUtilities.getInstanceById(allSupporters, accountId, suspense);
    }

    @Override
    public void initialize() {
        if (accountId == null)
            accountId = ModelUtilities.generateNewId(getClass().getSimpleName(), lastNum);
        allSupporters.put(accountId, this);
        lastNum++;
        super.initialize();
    }
}
