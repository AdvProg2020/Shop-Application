package Server.model.log;

import Server.model.ModelBasic;
import Server.model.ModelUtilities;
import Server.model.account.Customer;
import Server.model.account.Seller;
import Server.model.sellable.SubFile;
import Server.model.sellable.SubSellable;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileLog implements ModelBasic {
    private static final Map<String, FileLog> allFileLogs = new HashMap<>();
    private static int lastNum = 1;
    private String fileLogId;
    private final String subFileId;
    private final String customerId;
    private final String sellerId;
    private final Date date;
    private final double price;
    private final double saleAmount;
    private final double discountAmount;

    public FileLog(String subFileId, String customerId, double discountAmount) {
        this.subFileId = subFileId;
        this.customerId = customerId;
        this.discountAmount = discountAmount;
        sellerId = getSubFile().getSeller().getId();
        date = new Date();
        price = getSubFile().getRawPrice();
        saleAmount = price - getSubFile().getPriceWithSale();
        initialize();
    }

    public static List<FileLog> getAllFileLogs() {
        return ModelUtilities.getAllInstances(allFileLogs.values(), false);
    }

    public static FileLog getFileLogById(String fileLogId) {
        return ModelUtilities.getInstanceById(allFileLogs, fileLogId, false);
    }

    @Override
    public void initialize() {
        if (fileLogId == null)
            fileLogId = ModelUtilities.generateNewId(getClass().getSimpleName(), lastNum);
        allFileLogs.put(fileLogId, this);
        lastNum++;

        getCustomer().addFileLog(fileLogId);
        getSeller().addFileLog(fileLogId);
        getSubFile().addCustomer(getCustomer().getId());
    }

    @Override
    public boolean isSuspended() {
        return false;
    }

    @Override
    public String getId() {
        return fileLogId;
    }

    public Customer getCustomer() {
        return Customer.getCustomerById(customerId, false);
    }

    public Seller getSeller() {
        return Seller.getSellerById(sellerId, false);
    }

    public SubSellable getSubFile() {
        return SubFile.getSubFileById(subFileId, false);
    }

    public Date getDate() {
        return date;
    }

    public double getPrice() {
        return price;
    }

    public double getSaleAmount() {
        return saleAmount;
    }

    public double getDiscountAmount() {
        return discountAmount;
    }


}
