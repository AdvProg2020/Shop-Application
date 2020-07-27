package Client.HollowController;

import Client.HollowController.Exceptions.*;
import Client.view.Constants;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;


public class CustomerController {
    private static Type doubleType = new TypeToken<Double>(){}.getType();
    private static Type booleanType = new TypeToken<Boolean>(){}.getType();
    private static Type stringType = new TypeToken<String>(){}.getType();
    private static Type stringArrayListType = new TypeToken<ArrayList<String[]>>(){}.getType();
    private static Type byteArrayType = new TypeToken<byte[]>(){}.getType();

    private Sender sender;

    public CustomerController() {
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

    public void editPersonalInfo(String field, String newInformation) throws InvalidFieldException,
            SameAsPreviousValueException {
        String body = convertToJson(field, newInformation);
        String response = sender.sendRequest(Constants.Commands.customerEditPersonalInfo, body);
        if (response.startsWith("exception:")) {
            String[] nameBody = getExceptionNameAndBody(response);
            if (nameBody[0].startsWith("Invalid")) throw new InvalidFieldException();
            else throw new SameAsPreviousValueException(nameBody[1]);
        }
    }

    public double getTotalPriceOfCartWithDiscount(String discountCode) throws InvalidDiscountException {
        String body = convertToJson(discountCode);
        String response = sender.sendRequest(Constants.Commands.customerGetTotalPriceOfCartWithDiscount, body);
        if (response.startsWith("exception:")) {
            String[] nameBody = getExceptionNameAndBody(response);
            throw new InvalidDiscountException(nameBody[1]);
        } else {
            return new Gson().fromJson(response, doubleType);
        }
    }

    //Todo: check please
    public void purchaseTheCart(String receiverName, String address, String receiverPhone, String discountCode) throws InsufficientCreditException,
            NotAvailableSubProductsInCart, InvalidDiscountException, EmptyCartException {
        String body = convertToJson(receiverName, address, receiverPhone, discountCode);
        String response = sender.sendRequest(Constants.Commands.customerPurchaseTheCart, body);
        if (response.startsWith("exception:")) {
            String[] nameBody = getExceptionNameAndBody(response);
            if (nameBody[0].startsWith("Insufficient")) throw new InsufficientCreditException(nameBody[1]);
            else if (nameBody[0].startsWith("Not")) throw new NotAvailableSubProductsInCart(nameBody[1]);
            else if (nameBody[0].startsWith("Invalid")) throw new InvalidDiscountException(nameBody[1]);
            else throw new EmptyCartException();
        }
    }

    public ArrayList<String[]> getOrders() throws CustomerLoginException {
        String body = convertToJson();
        String response = sender.sendRequest(Constants.Commands.customerGetOrders, body);
        if (response.startsWith("exception:")) {
            throw new CustomerLoginException();
        } else {
            return new Gson().fromJson(response, stringArrayListType);
        }
    }

    public ArrayList<String[]> getOrderWithId(String orderId) throws InvalidLogIdException, CustomerLoginException {
        String body = convertToJson(orderId);
        String response = sender.sendRequest(Constants.Commands.customerGetOrderWithId, body);
        if (response.startsWith("exception:")) {
            String[] nameBody = getExceptionNameAndBody(response);
            if (nameBody[0].startsWith("Invalid")) throw new InvalidLogIdException(nameBody[1]);
            else throw new CustomerLoginException();
        } else {
            return new Gson().fromJson(response, stringArrayListType);
        }
    }

    public void rateProduct(String productID, int score) throws
            InvalidSellableIdException, HaveNotBoughtException {
        String body = convertToJson(productID, score);
        String response = sender.sendRequest(Constants.Commands.customerRateProduct, body);
        if (response.startsWith("exception:")) {
            String[] nameBody = getExceptionNameAndBody(response);
            if (nameBody[0].startsWith("Invalid")) throw new InvalidSellableIdException(nameBody[1]);
            else throw new HaveNotBoughtException(nameBody[1]);
        }
    }

    public double viewBalance() {
        String body = convertToJson();
        String response = sender.sendRequest(Constants.Commands.customerViewBalance, body);
        return new Gson().fromJson(response, doubleType);
    }

    public ArrayList<String[]> viewDiscountCodes() {
        String body = convertToJson();
        String response = sender.sendRequest(Constants.Commands.customerViewDiscountCodes, body);
        return new Gson().fromJson(response, stringArrayListType);
    }

    public double getTotalPriceOfCart() throws UnAuthorizedAccountException {
        String body = convertToJson();
        String response = sender.sendRequest(Constants.Commands.customerGetTotalPriceOfCart, body);
        if (response.startsWith("exception:")) {
            throw new UnAuthorizedAccountException();
        } else {
            return new Gson().fromJson(response, doubleType);
        }
    }

    public boolean hasBought(String productId) throws InvalidSellableIdException {
        String body = convertToJson(productId);
        String response = sender.sendRequest(Constants.Commands.customerHasBought, body);
        if (response.startsWith("exception:")) {
            String[] nameBody = getExceptionNameAndBody(response);
            throw new InvalidSellableIdException(nameBody[1]);
        } else {
            return new Gson().fromJson(response, booleanType);
        }
    }

    public void bid(String auctionId, double bidAmount) throws InvalidAuctionIdException {
        String body = convertToJson(auctionId, bidAmount);
        String response = sender.sendRequest(Constants.Commands.customerBid, body);
        if (response.startsWith("exception:")) {
            String[] nameBody = getExceptionNameAndBody(response);
            throw new InvalidAuctionIdException(nameBody[1]);
        }
    }

    public String getSupportChatId() throws DontHaveChatException {
        String body = convertToJson();
        String response = sender.sendRequest(Constants.Commands.customerGetSupportChatId, body);
        if (response.startsWith("exception:")) {
            String[] nameBody = getExceptionNameAndBody(response);
            throw new DontHaveChatException(nameBody[1]);
        } else {
            return new Gson().fromJson(response, stringType);
        }
    }

    public ArrayList<String[]> getAllSupporters() {
        String body = convertToJson();
        String response = sender.sendRequest(Constants.Commands.customerGetAllSupporters, body);
        return new Gson().fromJson(response, stringArrayListType);
    }

    public String createSupportChat(String supporterId) throws AlreadyInAChatException, InvalidSupporterIdException {
        String body = convertToJson(supporterId);
        String response = sender.sendRequest(Constants.Commands.customerCreateSupportChat, body);
        if (response.startsWith("exception:")) {
            String[] nameBody = getExceptionNameAndBody(response);
            if (nameBody[0].startsWith("Already")) throw new AlreadyInAChatException(nameBody[1]);
            else throw new InvalidSupporterIdException(nameBody[1]);
        } else {
            return new Gson().fromJson(response, stringType);
        }
    }

    public byte[] downloadFile(String subFileId) throws InvalidFileIdException, HaveNotBoughtException {
        String body = convertToJson(subFileId);
        String response = sender.sendRequest(Constants.Commands.customerDownloadFile, body);
        if (response.startsWith("exception:")) {
            String[] nameBody = getExceptionNameAndBody(response);
            if (nameBody[0].startsWith("Invalid")) throw new InvalidFileIdException(nameBody[1]);
            else throw new HaveNotBoughtException(nameBody[1]);
        } else {
            return new Gson().fromJson(response, byteArrayType);
        }
    }

    public void purchaseTheFile(String subFileId, String discountCode) throws InvalidFileIdException, InvalidDiscountException, InsufficientCreditException {
        String body = convertToJson(subFileId, discountCode);
        String response = sender.sendRequest(Constants.Commands.customerPurchaseTheFile, body);
        if (response.startsWith("exception:")) {
            String[] nameBody = getExceptionNameAndBody(response);
            if (nameBody[0].startsWith("InvalidFile")) throw new InvalidFileIdException(nameBody[1]);
            else if (nameBody[0].startsWith("InvalidDiscount")) throw new InvalidDiscountException(nameBody[1]);
            else throw new InsufficientCreditException(nameBody[1]);
        }
    }

    public double getTotalPriceOfFileWithDiscount(String discountCode, double fileCost) throws InvalidDiscountException {
        String body = convertToJson(discountCode, fileCost);
        String response = sender.sendRequest(Constants.Commands.customerGetTotalPriceOfFileWithDiscount, body);
        if (response.startsWith("exception:")) {
            String[] nameBody = getExceptionNameAndBody(response);
            throw new InvalidDiscountException(nameBody[1]);
        } else {
            return new Gson().fromJson(response, doubleType);
        }
    }

}
