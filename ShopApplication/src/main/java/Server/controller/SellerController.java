package Server.controller;


import Server.model.Auction;
import Server.model.Category;
import Server.model.Sale;
import Server.model.account.Account;
import Server.model.account.Customer;
import Server.model.account.Seller;
import Server.model.database.Database;
import Server.model.log.FileLog;
import Server.model.log.LogItem;
import Server.model.log.SellLog;
import Server.model.request.*;
import Server.model.sellable.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class SellerController {

    private static final DateFormat dateFormat = Utilities.getDateFormat();
    private Controller mainController;

    public SellerController(Controller controller) {
        mainController = controller;
    }

    private Account currentAccount() {
        return mainController.getCurrentAccount();
    }

    private Database database() {
        return mainController.getDatabase();
    }


    public void editPersonalInfo(String field, String newInformation) throws Exceptions.InvalidFieldException, Exceptions.SameAsPreviousValueException {
        if (field.equals("storeName")) {
            if (((Seller) currentAccount()).getStoreName().equals(newInformation))
                throw new Exceptions.SameAsPreviousValueException(field);
            ((Seller) currentAccount()).setStoreName(newInformation);
        } else if (field.equals("balance")) {
            if (((Seller) currentAccount()).getWallet().getBalance() == Double.parseDouble(newInformation))
                throw new Exceptions.SameAsPreviousValueException(newInformation);
            ((Seller) currentAccount()).getWallet().changeBalance(Double.parseDouble(newInformation) - ((Seller) currentAccount()).getWallet().getBalance());
        } else {
            mainController.editPersonalInfo(field, newInformation);
        }
        database().editAccount();
    }

    public String isProductWithNameAndBrand(String name, String brand) {
        Product p = Product.getProductByNameAndBrand(name, brand);
        if (p == null) return null;

        return p.getId();
    }

    public String isFileWithNameAndExtension(String name, String extension) {
        File f = File.getFileByNameAndExtension(name, extension);
        if (f == null) return null;

        return f.getId();
    }

    public boolean isNameAndBrandUsed(String name, String brand) {
        return Product.isProductNameAndBrandUsed(name, brand);
    }

    public boolean isNameAndExtensionUsed(String name, String extension) {
        return File.isFileNameAndExtensionUsed(name, extension);
    }

    public boolean doesSellerSellThisSellable(String sellableId) {
        Sellable sellable = Sellable.getSellableById(sellableId);
        return sellable.isSoldInStoreWithName(((Seller) currentAccount()).getStoreName());
    }

    public ArrayList<String[]> getAllSellLogs() {
        ArrayList<String[]> allSells = new ArrayList<>();
        for (SellLog sellLog : ((Seller) currentAccount()).getSellLogs()) {
            allSells.add(Utilities.Pack.sellLog(sellLog));
        }
        for (FileLog fileLog : ((Seller) currentAccount()).getFileLogs()) {
            allSells.add(Utilities.Pack.fileLogAsSellLog(fileLog));
        }
        return allSells;
    }

    public ArrayList<String[]> getSellLogWithId(String logId) throws Exceptions.InvalidLogIdException {
        SellLog sellLog = null;
        for (SellLog log : ((Seller) currentAccount()).getSellLogs()) {
            if (log.getId().equals(logId))
                sellLog = log;
        }
        if (sellLog == null) throw new Exceptions.InvalidLogIdException(logId);

        ArrayList<String[]> logInfo = new ArrayList<>();
        logInfo.add(Utilities.Pack.sellLog(sellLog));
        for (LogItem item : sellLog.getLogItems()) {
            logInfo.add(Utilities.Pack.sellLogItem(item));
        }
        return logInfo;
    }

    public ArrayList<String[]> manageProducts() {
        ArrayList<String[]> products = new ArrayList<>();
        for (SubProduct subProduct : ((Seller) currentAccount()).getSubProducts()) {
            products.add(Utilities.Pack.sellerSubProduct(subProduct));
        }
        return products;
    }

    public ArrayList<String[]> manageFiles() {
        ArrayList<String[]> files = new ArrayList<>();
        for (SubFile subFile : ((Seller) currentAccount()).getSubFiles()) {
            files.add(Utilities.Pack.sellerSubProduct(subFile));
        }
        return files;
    }


    public ArrayList<String> viewSellableBuyers(String sellableId) throws Exceptions.InvalidSellableIdException {
        Seller seller = ((Seller) currentAccount());
        ArrayList<SubSellable> subSellables = new ArrayList<>();
        subSellables.addAll(seller.getSubProducts());
        subSellables.addAll(seller.getSubFiles());
        for (SubSellable subSellable : subSellables) {
            if (subSellable.getSellable().getId().equals(sellableId)) {
                ArrayList<String> buyers = new ArrayList<>();
                for (Customer customer : subSellable.getCustomers()) {
                    buyers.add(customer.getId());
                }
                return buyers;
            }
        }
        throw new Exceptions.InvalidSellableIdException(sellableId);
    }

    public void editProduct(String productId, String field, String newInformation) throws Exceptions.InvalidSellableIdException, Exceptions.ExistingProductException, Exceptions.InvalidFieldException, Exceptions.SameAsPreviousValueException {
        SubProduct targetedSubProduct = null;
        for (SubProduct subProduct : ((Seller) currentAccount()).getSubProducts()) {
            if (subProduct.getProduct().getId().equals(productId)) {
                targetedSubProduct = subProduct;
                break;
            }
        }
        if (targetedSubProduct == null) throw new Exceptions.InvalidSellableIdException(productId);

        switch (field) {
            case "name": {
                String existingProductId;
                if ((existingProductId = isProductWithNameAndBrand(newInformation, targetedSubProduct.getProduct().getBrand())) != null)
                    throw new Exceptions.ExistingProductException(existingProductId);
                if (targetedSubProduct.getProduct().getName().equals(newInformation))
                    throw new Exceptions.SameAsPreviousValueException(field);

                new EditProductRequest(targetedSubProduct.getId(), EditProductRequest.Field.NAME, newInformation);
                database().request();
                break;
            }
            case "brand": {
                String existingProductId;
                if ((existingProductId = isProductWithNameAndBrand(targetedSubProduct.getProduct().getName(), newInformation)) != null)
                    throw new Exceptions.ExistingProductException(existingProductId);
                if (targetedSubProduct.getProduct().getBrand().equals(newInformation))
                    throw new Exceptions.SameAsPreviousValueException(field);

                new EditProductRequest(targetedSubProduct.getId(), EditProductRequest.Field.BRAND, newInformation);
                database().request();
                break;
            }
            case "info text":
                if (targetedSubProduct.getProduct().getInfoText().equals(newInformation))
                    throw new Exceptions.SameAsPreviousValueException(field);

                new EditProductRequest(targetedSubProduct.getId(), EditProductRequest.Field.INFO_TEXT, newInformation);
                database().request();
                break;
            case "property":
                new EditProductRequest(targetedSubProduct.getId(), EditProductRequest.Field.PROPERTY, newInformation);
                database().request();
                break;
            case "price":
                if (targetedSubProduct.getRawPrice() == Double.parseDouble(newInformation))
                    throw new Exceptions.SameAsPreviousValueException(field);

                new EditProductRequest(targetedSubProduct.getId(), EditProductRequest.Field.SUB_PRICE, newInformation);
                database().request();
                break;
            case "count":
                if (targetedSubProduct.getRemainingCount() == Integer.parseInt(newInformation))
                    throw new Exceptions.SameAsPreviousValueException(field);

                new EditProductRequest(targetedSubProduct.getId(), EditProductRequest.Field.SUB_COUNT, newInformation);
                database().request();
                break;
            default:
                throw new Exceptions.InvalidFieldException();
        }
    }

    public void editFile(String fileId, String field, String newInformation) throws Exceptions.InvalidFieldException, Exceptions.SameAsPreviousValueException, Exceptions.ExistingFileException, Exceptions.InvalidFileIdException {
        SubFile targetedSubFile = null;
        for (SubFile subFile : ((Seller) currentAccount()).getSubFiles()) {
            if (subFile.getFile().getId().equals(fileId)) {
                targetedSubFile = subFile;
                break;
            }
        }
        if (targetedSubFile == null) throw new Exceptions.InvalidFileIdException(fileId);

        switch (field) {
            case "name": {
                if (isNameAndExtensionUsed(newInformation, targetedSubFile.getFile().getExtension()))
                    throw new Exceptions.ExistingFileException();
                if (targetedSubFile.getFile().getName().equals(newInformation))
                    throw new Exceptions.SameAsPreviousValueException(field);

                new EditFileRequest(targetedSubFile.getId(), EditFileRequest.Field.NAME, newInformation);
                database().request();
                break;
            }
            case "extension": {
                if (isNameAndExtensionUsed(targetedSubFile.getFile().getName(), newInformation))
                    throw new Exceptions.ExistingFileException();
                if (targetedSubFile.getFile().getExtension().equals(newInformation))
                    throw new Exceptions.SameAsPreviousValueException(field);

                new EditFileRequest(targetedSubFile.getId(), EditFileRequest.Field.EXTENSION, newInformation);
                database().request();
                break;
            }
            case "info text":
                if (targetedSubFile.getFile().getInfoText().equals(newInformation))
                    throw new Exceptions.SameAsPreviousValueException(field);

                new EditFileRequest(targetedSubFile.getId(), EditFileRequest.Field.INFO_TEXT, newInformation);
                database().request();
                break;
            case "property":
                new EditFileRequest(targetedSubFile.getId(), EditFileRequest.Field.PROPERTY, newInformation);
                database().request();
                break;
            case "price":
                if (targetedSubFile.getRawPrice() == Double.parseDouble(newInformation))
                    throw new Exceptions.SameAsPreviousValueException(field);

                new EditFileRequest(targetedSubFile.getId(), EditFileRequest.Field.SUB_PRICE, newInformation);
                database().request();
                break;
            default:
                throw new Exceptions.InvalidFieldException();
        }
    }


    public void addNewProduct(String name, String brand, String infoText, byte[] image, String categoryName, HashMap<String, String> propertyValues,
                              double price, int count) throws Exceptions.ExistingProductException, Exceptions.InvalidCategoryException {

        Product product = Product.getProductByNameAndBrand(name, brand);
        if (product != null) throw new Exceptions.ExistingProductException(product.getId());
        Category category = Category.getCategoryByName(categoryName);
        if (category == null) throw new Exceptions.InvalidCategoryException(categoryName);

        SubProduct subProduct = new SubProduct(null, currentAccount().getId(), price, count, database());
        String imagePath = image.length != 0 ? mainController.saveFileInDataBase(image, "sellableImg", "PRO_" + name + "_" + brand + ".png") : null;
        new Product(name, brand, infoText, imagePath, category.getId(), propertyValues, subProduct, database());
        database().request();
    }

    public void addNewSubProductToAnExistingProduct(String productId, double price, int count) throws Exceptions.InvalidSellableIdException {
        if (Product.getProductById(productId) == null) throw new Exceptions.InvalidSellableIdException(productId);

        new SubProduct(productId, currentAccount().getId(), price, count, database());
        database().request();
    }

    public void addNewFile(String name, String extension, String infoText, byte[] image, String categoryName, Map<String, String> propertyValues, double price, byte[] file) throws Exceptions.ExistingFileException, Exceptions.InvalidCategoryException {
        if (File.isFileNameAndExtensionUsed(name, extension)) throw new Exceptions.ExistingFileException();
        Category category = Category.getCategoryByName(categoryName);
        if (category == null) throw new Exceptions.InvalidCategoryException(categoryName);

        String downloadPath = mainController.saveFileInDataBase(file, "files", currentAccount().getUsername() + "_" + name + "." + extension);
        String imagePath = image.length != 0 ? mainController.saveFileInDataBase(image, "sellableImg", "FILE_" + name + "_" + extension + ".png") : null;
        SubFile subFile = new SubFile(null, currentAccount().getId(), price, downloadPath, database());
        new File(name, extension, infoText, imagePath, category.getId(), propertyValues, subFile, database());
        database().request();
    }

    public void addNewSubFileToAnExistingFile(String fileId, double price, byte[] file) throws Exceptions.InvalidFileIdException {
        if (File.getFileById(fileId) == null) throw new Exceptions.InvalidFileIdException(fileId);

        File f = File.getFileById(fileId);
        String downloadPath;
        if (file.length != 0)
            downloadPath = mainController.saveFileInDataBase(file, "files", currentAccount().getUsername() + "_" + f.getName() + "." + f.getExtension());
        else
            downloadPath = null;

        new SubFile(fileId, currentAccount().getId(), price, downloadPath, database());
        database().request();
    }


    public void removeSellable(String sellableId) throws Exceptions.InvalidFileIdException {
        for (SubSellable subSellable : ((Seller) currentAccount()).getSubSellables()) {
            if (subSellable.getSellable().getId().equals(sellableId)) {
                subSellable.suspend();
                if (subSellable instanceof SubProduct)
                    database().removeSubFile();
                else
                    database().removeSubProduct();
                return;
            }
        }
        throw new Exceptions.InvalidFileIdException(sellableId);
    }


    public ArrayList<String[]> viewActiveSales() {
        ArrayList<String[]> saleInfos = new ArrayList<>();
        for (Sale sale : ((Seller) currentAccount()).getActiveSales()) {
            saleInfos.add(Utilities.Pack.saleInfo(sale));
        }
        return saleInfos;
    }

    public ArrayList<String[]> viewActiveAuctions() {
        ArrayList<String[]> auctionInfos = new ArrayList<>();
        for (Auction auction : ((Seller) currentAccount()).getActiveAuctions()) {
            auctionInfos.add(Utilities.Pack.auctionInfo(auction));
        }
        return auctionInfos;
    }

    public ArrayList<String[]> viewArchiveSales() {
        ArrayList<String[]> saleInfos = new ArrayList<>();
        for (Sale sale : ((Seller) currentAccount()).getSaleArchive()) {
            saleInfos.add(Utilities.Pack.saleInfo(sale));
        }
        return saleInfos;
    }

    public ArrayList<String[]> viewArchiveAuctions() {
        ArrayList<String[]> auctionInfos = new ArrayList<>();
        for (Auction auction : ((Seller) currentAccount()).getAuctionArchive()) {
            auctionInfos.add(Utilities.Pack.auctionInfo(auction));
        }
        return auctionInfos;
    }

    public String[] viewSaleWithId(String saleId) throws Exceptions.InvalidSaleIdException {
        for (Sale sale : ((Seller) currentAccount()).getActiveSales()) {
            if (sale.getId().equals(saleId)) {
                return Utilities.Pack.saleInfo(sale);
            }
        }
        throw new Exceptions.InvalidSaleIdException(saleId);
    }

    public String[] viewAuctionWithId(String auctionId) throws Exceptions.InvalidAuctionIdException {
        for (Auction auction : ((Seller) currentAccount()).getActiveAuctions()) {
            if (auction.getId().equals(auctionId)) {
                return Utilities.Pack.auctionInfo(auction);
            }
        }
        throw new Exceptions.InvalidAuctionIdException(auctionId);
    }

    public ArrayList<String[]> getSellablesInSale(String saleId) throws Exceptions.InvalidSaleIdException {
        Sale sale = Sale.getSaleById(saleId);
        if (sale == null) throw new Exceptions.InvalidSaleIdException(saleId);

        return sale.getSubSellables().stream().map(Utilities.Pack::sellableInSale).collect(Collectors.toCollection(ArrayList::new));
    }

    public void editSale(String saleId, String field, String newInformation) throws
            Exceptions.InvalidSaleIdException, Exceptions.InvalidFormatException, Exceptions.InvalidDateException, Exceptions.InvalidFieldException, Exceptions.SameAsPreviousValueException {
        Sale targetedSale = null;
        for (Sale sale : ((Seller) currentAccount()).getActiveSales()) {
            if (sale.getId().equals(saleId)) {
                targetedSale = sale;
                break;
            }
        }
        if (targetedSale == null) throw new Exceptions.InvalidSaleIdException(saleId);

        switch (field) {
            case "start date":
                try {
                    if (!targetedSale.getEndDate().after(dateFormat.parse(newInformation)))
                        throw new Exceptions.InvalidDateException();
                    if (dateFormat.parse(newInformation).equals(targetedSale.getStartDate()))
                        throw new Exceptions.SameAsPreviousValueException("start date");

                    new EditSaleRequest(saleId, newInformation, EditSaleRequest.Field.START_DATE);
                    database().request();
                } catch (ParseException e) {
                    throw new Exceptions.InvalidFormatException("date");
                }
                break;
            case "end date":
                try {
                    if (!targetedSale.getStartDate().before(dateFormat.parse(newInformation)))
                        throw new Exceptions.InvalidDateException();
                    if (dateFormat.parse(newInformation).equals(targetedSale.getEndDate()))
                        throw new Exceptions.SameAsPreviousValueException("end date");

                    new EditSaleRequest(saleId, newInformation, EditSaleRequest.Field.END_DATE);
                    database().request();
                } catch (ParseException e) {
                    throw new Exceptions.InvalidFormatException("date");
                }
                break;
            case "percentage":
                if (Double.parseDouble(newInformation) == targetedSale.getPercentage())
                    throw new Exceptions.SameAsPreviousValueException("percentage");

                new EditSaleRequest(saleId, newInformation, EditSaleRequest.Field.PERCENTAGE);
                database().request();
                break;
            case "maximum":
                if (Double.parseDouble(newInformation) == targetedSale.getMaximumAmount())
                    throw new Exceptions.SameAsPreviousValueException("maximum");

                new EditSaleRequest(saleId, newInformation, EditSaleRequest.Field.MAXIMUM);
                database().request();
                break;
            default:
                throw new Exceptions.InvalidFieldException();
        }

    }

    public void addSale(String StartDate, String EndDate, double percentage, double maximum, ArrayList<String> productIds) throws Exceptions.InvalidDateException, Exceptions.InvalidProductIdsForASeller, Exceptions.InvalidFormatException {
        Date startDate;
        Date endDate;
        try {
            startDate = dateFormat.parse(StartDate);
            endDate = dateFormat.parse(EndDate);
        } catch (ParseException e) {
            throw new Exceptions.InvalidFormatException("date");
        }

        if (!startDate.before(endDate)) throw new Exceptions.InvalidDateException();

        Sale sale = new Sale(currentAccount().getId(), startDate, endDate, percentage, maximum, database());
        Product product;
        SubProduct subProduct;
        ArrayList<String> invalidSubProductIds = new ArrayList<>();
        for (String productId : productIds) {
            product = Product.getProductById(productId);
            if (product != null) {
                subProduct = product.getSubProductOfSeller(currentAccount().getId());
                if (subProduct != null)
                    sale.addSubSellable(subProduct.getId());
                else
                    invalidSubProductIds.add(productId);
            } else {
                invalidSubProductIds.add(productId);
            }
        }
        if (invalidSubProductIds.size() > 0)
            throw new Exceptions.InvalidProductIdsForASeller(Utilities.Pack.invalidProductIds(invalidSubProductIds));
        database().request();
    }

    public void addAuction(String StartDate, String EndDate, String subSellableID) throws Exceptions.InvalidFormatException, Exceptions.InvalidDateException {
        Date startDate;
        Date endDate;
        try {
            startDate = dateFormat.parse(StartDate);
            endDate = dateFormat.parse(EndDate);
        } catch (ParseException e) {
            throw new Exceptions.InvalidFormatException("date");
        }
        if (!startDate.before(endDate)) throw new Exceptions.InvalidDateException();

        new Auction(currentAccount().getId(), subSellableID, startDate, endDate, database());
        database().request();
    }

    public void editAuction(String auctionId, String field, String newInformation) throws Exceptions.InvalidAuctionIdException, Exceptions.InvalidFieldException, Exceptions.InvalidDateException, Exceptions.InvalidFormatException, Exceptions.SameAsPreviousValueException {
        Auction targetedAuction = null;
        for (Auction auction : ((Seller) currentAccount()).getActiveAuctions()) {
            if (auction.getId().equals(auctionId)) {
                targetedAuction = auction;
                break;
            }
        }
        if (targetedAuction == null) throw new Exceptions.InvalidAuctionIdException(auctionId);

        switch (field) {
            case "start date":
                try {
                    if (!targetedAuction.getEndDate().after(dateFormat.parse(newInformation)))
                        throw new Exceptions.InvalidDateException();
                    if (dateFormat.parse(newInformation).equals(targetedAuction.getStartDate()))
                        throw new Exceptions.SameAsPreviousValueException("start date");

                    new EditAuctionRequest(auctionId, newInformation, EditAuctionRequest.Field.START_DATE);
                    database().request();
                } catch (ParseException | Exceptions.SameAsPreviousValueException e) {
                    throw new Exceptions.InvalidFormatException("date");
                }
                break;
            case "end date":
                try {
                    if (!targetedAuction.getStartDate().before(dateFormat.parse(newInformation)))
                        throw new Exceptions.InvalidDateException();
                    if (dateFormat.parse(newInformation).equals(targetedAuction.getEndDate()))
                        throw new Exceptions.SameAsPreviousValueException("end date");

                    new EditAuctionRequest(auctionId, newInformation, EditAuctionRequest.Field.END_DATE);
                    database().request();
                } catch (ParseException e) {
                    throw new Exceptions.InvalidFormatException("date");
                }
                break;
            default:
                throw new Exceptions.InvalidFieldException();
        }
    }

    public void addSellablesToSale(String saleId, ArrayList<String> subSellableIds) {
        Sale sale = Sale.getSaleById(saleId);
        for (String subSellableId : subSellableIds) {
            sale.addSubSellable(subSellableId);
        }
        database().editSale();
    }

    public void removeSellablesFromSale(String saleId, ArrayList<String> subSellableIds) {
        Sale sale = Sale.getSaleById(saleId);
        for (String subSellableId : subSellableIds) {
            sale.removeSubSellable(subSellableId);
        }
        database().removeSale();
    }

    public double viewBalance() {
        return ((Seller) currentAccount()).getWallet().getBalance();
    }

    public void removeAuction(String auctionId) throws Exceptions.InvalidAuctionIdException {
        Auction auction = Auction.getAuctionById(auctionId);
        if (auction == null) throw new Exceptions.InvalidAuctionIdException(auctionId);

        auction.suspend();
        database().removeAuction();
    }

    public void removeSale(String saleId) throws Exceptions.InvalidSaleIdException {
        Sale sale = Sale.getSaleById(saleId);
        if (sale == null) throw new Exceptions.InvalidSaleIdException(saleId);

        sale.suspend();
        database().removeSale();
    }

    public ArrayList<String[]> getPendingRequests(){
        ArrayList<Request> requests = new ArrayList<>(((Seller)currentAccount()).getPendingRequests());
        ArrayList<String[]> requestPacks = new ArrayList<>();
        for (Request request : requests) {
            requestPacks.add(Utilities.Pack.request(request));
        }
        return requestPacks;
    }

    public ArrayList<String[]> getAllCategories() {
        ArrayList<Category> categories = new ArrayList<>(Category.getAllCategories());
        ArrayList<String[]> categoryNames = new ArrayList<>();
        for (Category category : categories) {
            categoryNames.add(new String[]{category.getName(), category.getParent().getName()});
        }
        return categoryNames;
    }

    public boolean doesSellSubSellable(String subProductId) throws Exceptions.InvalidSubProductIdException {
        SubSellable subSellable = SubSellable.getSubSellableById(subProductId);
        if (subSellable == null) throw new Exceptions.InvalidSubProductIdException(subProductId);

        return (currentAccount() == subSellable.getSeller());
    }


}
