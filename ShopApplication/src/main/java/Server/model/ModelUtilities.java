package Server.model;

import Server.model.account.Admin;
import Server.model.account.Customer;
import Server.model.account.Seller;
import Server.model.account.Supporter;
import Server.model.chat.AuctionChat;
import Server.model.chat.Message;
import Server.model.chat.SupportChat;
import Server.model.log.BuyLog;
import Server.model.log.FileLog;
import Server.model.log.LogItem;
import Server.model.log.SellLog;
import Server.model.request.Request;
import Server.model.sellable.Product;
import Server.model.sellable.SubFile;
import Server.model.sellable.SubProduct;

import java.io.File;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.*;

import static java.util.Map.entry;

public class ModelUtilities {
    private static Map<String, String> abbreviations = Map.ofEntries(
            entry(Customer.class.getSimpleName(), "CST"),
            entry(Seller.class.getSimpleName(), "SLR"),
            entry(Admin.class.getSimpleName(), "ADM"),
            entry(Supporter.class.getSimpleName(), "SPT"),
            entry(Cart.class.getSimpleName(), "CRT"),
            entry(Wallet.class.getSimpleName(), "WLT"),
            entry(SupportChat.class.getSimpleName(), "SCH"),
            entry(AuctionChat.class.getSimpleName(), "ACH"),
            entry(Message.class.getSimpleName(), "MSG"),
            entry(BuyLog.class.getSimpleName(), "BLG"),
            entry(SellLog.class.getSimpleName(), "SLG"),
            entry(LogItem.class.getSimpleName(), "LGI"),
            entry(FileLog.class.getSimpleName(), "FLG"),
            entry(Request.class.getSimpleName(), "REQ"),
            entry(Category.class.getSimpleName(), "CTG"),
            entry(Discount.class.getSimpleName(), "DSC"),
            entry(Sale.class.getSimpleName(), "SAL"),
            entry(Auction.class.getSimpleName(), "AUC"),
            entry(Product.class.getSimpleName(), "PRO"),
            entry(SubProduct.class.getSimpleName(), "SBP"),
            entry(File.class.getSimpleName(), "FIL"),
            entry(SubFile.class.getSimpleName(), "SFL"),
            entry(Rating.class.getSimpleName(), "RAT"),
            entry(Review.class.getSimpleName(), "REV")
    );

    private static String fixedLengthNumber(int number, int length) {
        return String.format("%0" + length + "d", number);
    }

    public static String fixedPath(String path, final String DEFAULT_PATH) {
        if (path == null || !new File(path).exists())
            path = DEFAULT_PATH;

        return path;
    }

    public static String generateNewId(String className, int lastNum) {
        StringBuilder id = new StringBuilder();
        Calendar calendar = Calendar.getInstance();

        id.append(abbreviations.get(className));
        id.append(fixedLengthNumber(calendar.get(Calendar.YEAR) % 100, 2));
        id.append(fixedLengthNumber(calendar.get(Calendar.MONTH), 2));
        id.append(fixedLengthNumber(calendar.get(Calendar.DAY_OF_MONTH), 2));
        id.append(fixedLengthNumber(lastNum, 4));

        return id.toString();
    }

    public static <T extends ModelBasic> List<T> getAllInstances(Collection<T> instances, boolean... suspense) {
        boolean checkSuspense = (suspense.length == 0) || suspense[0]; // optional (default = true)

        ArrayList<T> updatedList = new ArrayList<>(instances);
        if (checkSuspense)
            updatedList.removeIf(ModelBasic::isSuspended);
        updatedList.sort(Comparator.comparing(ModelBasic::getId));

        return updatedList;
    }

    public static <T extends ModelBasic> T getInstanceById(Map<String, T> instances, String id, boolean... suspense) {
        boolean checkSuspense = (suspense.length == 0) || suspense[0]; // optional (default = true)

        T instance = instances.get(id);
        if (checkSuspense && instance != null && instance.isSuspended())
            instance = null;

        return instance;
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    public @interface ModelOnly {
    }
}
