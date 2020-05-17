package model;

import model.account.Admin;
import model.account.Customer;
import model.account.Seller;
import model.log.BuyLog;
import model.log.LogItem;
import model.log.SellLog;
import model.request.Request;

import java.util.*;

import static java.util.Map.entry;

public class BasicMethods {
    private static Map<String, String> abbreviations = Map.ofEntries(
            entry(Customer.class.getSimpleName(), "CST"),
            entry(Seller.class.getSimpleName(), "SLR"),
            entry(Admin.class.getSimpleName(), "ADM"),
            entry(BuyLog.class.getSimpleName(), "BLG"),
            entry(SellLog.class.getSimpleName(), "SLG"),
            entry(LogItem.class.getSimpleName(), "LGI"),
            entry(Request.class.getSimpleName(), "REQ"),
            entry(Cart.class.getSimpleName(), "CRT"),
            entry(Category.class.getSimpleName(), "CTG"),
            entry(Discount.class.getSimpleName(), "DSC"),
            entry(Product.class.getSimpleName(), "PRO"),
            entry(Rating.class.getSimpleName(), "RAT"),
            entry(Review.class.getSimpleName(), "REV"),
            entry(Sale.class.getSimpleName(), "SAL"),
            entry(SubProduct.class.getSimpleName(), "SBP")
    );


    public static String generateNewId(String className, int lastNum) {
        abbreviations.get(className);
        Date date = new Date();
        Calendar.getInstance().get(Calendar.DAY_OF_YEAR);
        return null;
    }

    public static <T extends ModelBasic> List<T> getInstances(Collection<T> instances, boolean... suspense) {
        boolean checkSuspense = (suspense.length == 0) || suspense[0]; // optional (default = true)

        if (checkSuspense)
            instances.removeIf(ModelBasic::isSuspended);

        ArrayList<T> retValue = new ArrayList<>(instances);
        retValue.sort(new InstanceComparator());
        return retValue;
    }

    public static <T extends ModelBasic> T getInstanceById(Map<String, T> instances, String instanceId, boolean... suspense) {
        boolean checkSuspense = (suspense.length == 0) || suspense[0]; // optional (default = true)

        T instance = instances.get(instanceId);
        if (checkSuspense && instance != null && instance.isSuspended())
            instance = null;

        return instance;
    }


    private static class InstanceComparator implements Comparator<ModelBasic> {

        @Override
        public int compare(ModelBasic o1, ModelBasic o2) {
            return o1.getId().compareTo(o2.getId());
        }
    }
}
