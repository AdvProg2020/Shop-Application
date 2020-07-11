package model.log;

import model.ModelBasic;
import model.ModelUtilities;
import model.account.Customer;
import model.account.Seller;
import model.sellable.SubFile;
import model.sellable.SubSellable;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileLog implements ModelBasic {
    private static Map<String, FileLog> allFileLogs = new HashMap<>();
    private static int lastNum = 1;
    private String fileLogId;
    private String subFileId;
    private String customerId;
    private String sellerId;
    private Date date;
    private double price;
    private double saleAmount;
    private double discountAmount;

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
