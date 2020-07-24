package Client.HollowController;

import Client.HollowController.Exceptions.*;
import Server.ServerGate.Task;
import Server.controller.Utilities;
import Server.model.Auction;
import Server.model.account.Seller;
import Server.model.request.EditAuctionRequest;
import Server.model.sellable.SubFile;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import Client.view.Constants;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;

public class SellerController {
    private static Type stringType = new TypeToken<String>(){}.getType();
    private static Type stringArrayType = new TypeToken<String[]>(){}.getType();
    private static Type doubleType = new TypeToken<Double>(){}.getType();
    private static Type booleanType = new TypeToken<Boolean>(){}.getType();
    private static Type stringArrayListType = new TypeToken<ArrayList<String[]>>(){}.getType();

    private Sender sender;

    public SellerController() {
        sender = Sender.getInstance();
    }

    private String convertToJson(Object... args) {
        StringBuilder array = new StringBuilder();
        Gson gson = new Gson();
        for (int i = 0; i < args.length; i++) {
            array.append(gson.toJson(args[i]));
            if (i != args.length - 1) array.append("*");
        }
        return array.toString();
    }

    private String[] getExceptionNameAndBody(String response) {
        String name = response.substring(response.indexOf(":") + 1, response.indexOf("\n"));
        String body = response.substring(response.indexOf("\n") + 1);
        return new String[]{name, body};
    }

    public void editPersonalInfo(String field, String newInformation) throws InvalidFieldException, SameAsPreviousValueException {
        String body = convertToJson(field, newInformation);
        String response = sender.sendRequest(Constants.Commands.sellerEditPersonalInfo, body);
        if (response.startsWith("exception:")) {
            String[] nameBody = getExceptionNameAndBody(response);
            if (nameBody[0].startsWith("Invalid")) throw new InvalidFieldException();
            else throw new SameAsPreviousValueException(nameBody[1]);
        }
    }

//    public ArrayList<String> viewCompanyInformation() {
//        String body = convertToJson();
//        String response = sender.sendRequest(Constants.Commands.viewCom, body);
//        if (response.startsWith("exception:")) {
//            String[] nameBody = getExceptionNameAndBody(response);
//        } else {
//            return new Gson().fromJson(response, );
//        }
//    }

    public String isProductWithNameAndBrand(String name, String brand){
        String body = convertToJson(name, brand);
        String response = sender.sendRequest(Constants.Commands.sellerIsProductWithNameAndBrand, body);
        return new Gson().fromJson(response, stringType);
    }

    public boolean isNameAndBrandUsed(String name, String brand){
        String body = convertToJson(name, brand);
        String response = sender.sendRequest(Constants.Commands.sellerIsNameAndBrandUsed, body);
        return new Gson().fromJson(response, booleanType);
    }

    public boolean doesSellerSellThisProduct(String productId){
        String body = convertToJson(productId);
        String response = sender.sendRequest(Constants.Commands.sellerDoesSellerSellThisProduct, body);
        return new Gson().fromJson(response, booleanType);
    }

    public ArrayList<String[]> getAllSellLogs() {
        String body = convertToJson();
        String response = sender.sendRequest(Constants.Commands.sellerGetAllSellLogs, body);
        return new Gson().fromJson(response, stringArrayListType);
    }

    public ArrayList<String[]> getSellLogWithId(String logId) throws InvalidLogIdException {
        String body = convertToJson(logId);
        String response = sender.sendRequest(Constants.Commands.sellerGetSellLogWithId, body);
        if (response.startsWith("exception:")) {
            String[] nameBody = getExceptionNameAndBody(response);
            throw new InvalidLogIdException(nameBody[1]);
        } else {
            return new Gson().fromJson(response, stringArrayListType);
        }
    }

    public ArrayList<String[]> manageProducts() {
        String body = convertToJson();
        String response = sender.sendRequest(Constants.Commands.sellerManageProducts, body);
        return new Gson().fromJson(response, stringArrayListType);
    }

//    public ArrayList<String> viewProductBuyers(String productID) throws InvalidProductIdException {
//        String body = convertToJson(productID);
//        String response = sender.sendRequest(Constants.Commands., body);
//        if (response.startsWith("exception:")) {
//            String[] nameBody = getExceptionNameAndBody(response);
//        } else {
//            return new Gson().fromJson(response, );
//        }
//    }

    public void editProduct(String productID, String field, String newInformation) throws InvalidSellableIdException, ExistingProductException, InvalidFieldException, SameAsPreviousValueException {
        String body = convertToJson(productID, field, newInformation);
        String response = sender.sendRequest(Constants.Commands.sellerEditProduct, body);
        if (response.startsWith("exception:")) {
            String[] nameBody = getExceptionNameAndBody(response);
            if (nameBody[0].startsWith("InvalidProduct")) throw new InvalidSellableIdException(nameBody[1]);
            else if (nameBody[0].startsWith("Existing")) throw new ExistingProductException(nameBody[1]);
            else if (nameBody[0].startsWith("InvalidField")) throw new InvalidFieldException();
            else throw new SameAsPreviousValueException(nameBody[1]);
        }
    }

//    public String exist(String productName, String brand) {
//        Product product = Product.getProductByNameAndBrand(productName, brand);
//        if (product != null)
//            return product.getId();
//        else
//            return null;
//    }

    //Todo: change it again!
    public void addNewProduct(String name, String brand, String infoText, String imagePath, String categoryName, HashMap<String, String> propertyValues,
                              double price, int count) throws ExistingProductException, InvalidCategoryException {

        String body = convertToJson(name, brand, infoText, imagePath, categoryName, propertyValues, price, count);
        String response = sender.sendRequest(Constants.Commands.sellerAddNewProduct, body);
        if (response.startsWith("exception:")) {
            String[] nameBody = getExceptionNameAndBody(response);
            if (nameBody[0].startsWith("Existing")) throw new ExistingProductException(nameBody[1]);
            else throw new InvalidCategoryException(nameBody[1]);
        }
    }

    //TODO
    public void addNewSubProductToAnExistingProduct(String productId, double price, int count) throws InvalidSellableIdException {
        String body = convertToJson(productId, price, count);
        String response = sender.sendRequest(Constants.Commands.sellerAddNewSubProductToAnExistingProduct, body);
        if (response.startsWith("exception:")) {
            String[] nameBody = getExceptionNameAndBody(response);
            throw new InvalidSellableIdException(nameBody[1]);
        }
    }

    public void removeProduct(String productID) throws InvalidSellableIdException {
        String body = convertToJson(productID);
        String response = sender.sendRequest(Constants.Commands.sellerRemoveProduct, body);
        if (response.startsWith("exception:")) {
            String[] nameBody = getExceptionNameAndBody(response);
            throw new InvalidSellableIdException(nameBody[1]);
        }
    }

    public ArrayList<String[]> viewActiveSales() {
        String body = convertToJson();
        String response = sender.sendRequest(Constants.Commands.sellerViewActiveSales, body);
        return new Gson().fromJson(response, stringArrayListType);
    }

    public ArrayList<String[]> viewArchiveSales() {
        String body = convertToJson();
        String response = sender.sendRequest(Constants.Commands.sellerViewArchiveSales, body);
        return new Gson().fromJson(response, stringArrayListType);
    }

    public String[] viewSaleWithId(String saleId) throws InvalidSaleIdException {
        String body = convertToJson(saleId);
        String response = sender.sendRequest(Constants.Commands.sellerViewSaleWithId, body);
        if (response.startsWith("exception:")) {
            String[] nameBody = getExceptionNameAndBody(response);
            throw new InvalidSaleIdException(nameBody[1]);
        } else {
            return new Gson().fromJson(response, stringArrayType);
        }
    }

    public ArrayList<String[]> getProductsInSale(String saleId) throws InvalidSaleIdException {
        String body = convertToJson(saleId);
        String response = sender.sendRequest(Constants.Commands.sellerGetProductsInSale, body);
        if (response.startsWith("exception:")) {
            String[] nameBody = getExceptionNameAndBody(response);
            throw new InvalidSaleIdException(nameBody[1]);
        } else {
            return new Gson().fromJson(response, stringArrayListType);
        }
    }

    public void editSale(String saleId, String field, String newInformation) throws
            InvalidSaleIdException, InvalidFormatException, InvalidDateException, InvalidFieldException, SameAsPreviousValueException {
        String body = convertToJson(saleId, field, newInformation);
        String response = sender.sendRequest(Constants.Commands.sellerEditSale, body);
        if (response.startsWith("exception:")) {
            String[] nameBody = getExceptionNameAndBody(response);
            if (nameBody[0].startsWith("InvalidSale")) throw new InvalidSaleIdException(nameBody[1]);
            else if (nameBody[0].startsWith("InvalidFormat")) throw new InvalidFormatException(nameBody[1]);
            else if (nameBody[0].startsWith("InvalidDate")) throw new InvalidDateException();
            else if (nameBody[0].startsWith("InvalidField")) throw new InvalidFieldException();
            else throw new SameAsPreviousValueException(nameBody[1]);
        }
    }

    public void addSale(String startDate, String endDate, double percentage, double maximum, ArrayList<String> productIds) throws InvalidDateException, InvalidProductIdsForASeller, InvalidFormatException {
        String body = convertToJson(startDate, endDate, percentage, maximum, productIds);
        String response = sender.sendRequest(Constants.Commands.sellerAddSale, body);
        if (response.startsWith("exception:")) {
            String[] nameBody = getExceptionNameAndBody(response);
            if (nameBody[0].startsWith("InvalidDate")) throw new InvalidDateException();
            else if (nameBody[0].startsWith("InvalidProduct")) throw new InvalidProductIdsForASeller(nameBody[1]);
            else throw new InvalidFormatException(nameBody[1]);
        }
    }

    public void addProductsToSale(String saleId, ArrayList<String> subProductIds){
        String body = convertToJson(saleId, subProductIds);
        sender.sendRequest(Constants.Commands.sellerAddProductsToSale, body);
    }

    public void removeProductsFromSale(String saleId, ArrayList<String> subProductIds){
        String body = convertToJson(saleId, subProductIds);
        sender.sendRequest(Constants.Commands.sellerRemoveProductsFromSale, body);
    }

    public double viewBalance() {
        String body = convertToJson();
        String response = sender.sendRequest(Constants.Commands.sellerViewBalance, body);
        return new Gson().fromJson(response, doubleType);
    }

    public void removeSale(String saleId) throws InvalidSaleIdException {
        String body = convertToJson(saleId);
        String response = sender.sendRequest(Constants.Commands.sellerRemoveSale, body);
        if (response.startsWith("exception:")) {
            String[] nameBody = getExceptionNameAndBody(response);
            throw new InvalidSaleIdException(nameBody[1]);
        }
    }

//    public void removeSale(ArrayList<String> saleIds) throws InvalidSaleIdException {
//        for (String saleId : saleIds) {
//            removeSale(saleId);
//        }
//    }

    public ArrayList<String[]> getPendingRequests(){
        String body = convertToJson();
        String response = sender.sendRequest(Constants.Commands.sellerGetPendingRequests, body);
        return new Gson().fromJson(response, stringArrayListType);
    }

    public ArrayList<String[]> getAllCategories(){
        String body = convertToJson();
        String response = sender.sendRequest(Constants.Commands.sellerGetAllCategories, body);
        return new Gson().fromJson(response, stringArrayListType);
    }

    public boolean doesSellSubProduct(String subProductId) throws InvalidSubProductIdException {
        String body = convertToJson(subProductId);
        String response = sender.sendRequest(Constants.Commands.sellerDoesSellSubProduct, body);
        if (response.startsWith("exception:")) {
            String[] nameBody = getExceptionNameAndBody(response);
            throw new InvalidSubProductIdException(nameBody[1]);
        } else {
            return new Gson().fromJson(response, booleanType);
        }
    }

    public String isFileWithNameAndExtension(String name, String extension) {
        String body = convertToJson(name, extension);
        String response = sender.sendRequest(Constants.Commands.sellerIsFileWithNameAndExtension, body);
        return new Gson().fromJson(response, stringType);
    }

    public boolean isNameAndExtensionUsed(String name, String extension) {
        String body = convertToJson(name, extension);
        String response = sender.sendRequest(Constants.Commands.sellerIsNameAndExtensionUsed, body);
        return new Gson().fromJson(response, booleanType);
    }

    public boolean doesSellerSellThisFile(String fileId) {
        String body = convertToJson(fileId);
        String response = sender.sendRequest(Constants.Commands.sellerDoesSellerSellThisFile, body);
        return new Gson().fromJson(response, booleanType);
    }

    public void addNewFile(String name, String extension, String info, String imagePath, String category, HashMap<String, String> properties, double price, String path) throws ExistingFileException, InvalidCategoryException{
        String body = convertToJson(name, extension, info, imagePath, category, properties, price, path);
        String response = sender.sendRequest(Constants.Commands.sellerAddNewFile, body);
        if (response.startsWith("exception:")) {
            String[] nameBody = getExceptionNameAndBody(response);
            if (nameBody[0].startsWith("Existing")) throw new ExistingFileException(nameBody[1]);
            else throw new InvalidCategoryException(nameBody[1]);
        }
    }

    public void addNewSubFileToAnExistingFile(String fileId, double price, String path) throws InvalidFileIdException{
        String body = convertToJson(fileId, price, path);
        String response = sender.sendRequest(Constants.Commands.sellerAddNewSubFileToAnExistingFile, body);
        if (response.startsWith("exception:")) {
            String[] nameBody = getExceptionNameAndBody(response);
            throw new InvalidFileIdException(nameBody[1]);
        }
    }

    public void removeFile(String fileId) throws Exceptions.InvalidFileIdException {
        String body = convertToJson(fileId);
        String response = sender.sendRequest(Constants.Commands.sellerRemoveFile, body);
        if (response.startsWith("exception:")) {
            String[] nameBody = getExceptionNameAndBody(response);
            throw new InvalidFileIdException(nameBody[1]);
        }
    }

    public void removeAuction(String auctionId) throws Exceptions.InvalidAuctionIdException {
        Auction auction = Auction.getAuctionById(auctionId);
        if (auction != null) {
            auction.suspend();
        } else {
            throw new Exceptions.InvalidAuctionIdException(auctionId);
        }
    }

    public ArrayList<String[]> viewArchiveAuctions() {
        String body = convertToJson();
        String response = sender.sendRequest(Constants.Commands.sellerViewArchiveAuctions, body);
        return new Gson().fromJson(response, stringArrayListType);
    }

    public ArrayList<String[]> viewActiveAuctions() {
        String body = convertToJson();
        String response = sender.sendRequest(Constants.Commands.sellerViewActiveAuctions, body);
        return new Gson().fromJson(response, stringArrayListType);
    }

    public String[] viewAuctionWithId(String auctionId) throws Exceptions.InvalidAuctionIdException {
        String body = convertToJson(auctionId);
        String response = sender.sendRequest(Constants.Commands.sellerViewAuctionWithId, body);
        if (response.startsWith("exception:")) {
            String[] nameBody = getExceptionNameAndBody(response);
            throw new InvalidAuctionIdException(nameBody[1]);
        } else {
            return new Gson().fromJson(response, stringArrayType);
        }
    }

    public void editAuction(String auctionId, String fileId, String newInformation)
            throws Exceptions.InvalidAuctionIdException, Exceptions.InvalidFieldException, Exceptions.InvalidDateException, Exceptions.InvalidFormatException, Exceptions.SameAsPreviousValueException {
        String body = convertToJson(auctionId, fileId, newInformation);
        String response = sender.sendRequest(Constants.Commands.sellerEditAuction, body);
        if (response.startsWith("exception:")) {
            String[] nameBody = getExceptionNameAndBody(response);
            if (nameBody[0].startsWith("InvalidAuction")) throw new InvalidAuctionIdException(nameBody[1]);
            else if (nameBody[0].startsWith("InvalidField")) throw new InvalidFieldException();
            else if (nameBody[0].startsWith("InvalidDate")) throw new InvalidDateException();
            else if (nameBody[0].startsWith("InvalidFormat")) throw new InvalidFormatException(nameBody[1]);
            else throw new SameAsPreviousValueException(nameBody[1]);
        }
    }

    public void addAuction(String startDate, String endDate, String subSellableID) throws Exceptions.InvalidFormatException, Exceptions.InvalidDateException {
        String body = convertToJson(startDate, endDate, subSellableID);
        String response = sender.sendRequest(Constants.Commands.sellerAddAuction, body);
        if (response.startsWith("exception:")) {
            String[] nameBody = getExceptionNameAndBody(response);
            if (nameBody[0].startsWith("InvalidFormat")) throw new InvalidFormatException(nameBody[1]);
            else throw new InvalidDateException();
        }
    }

}
