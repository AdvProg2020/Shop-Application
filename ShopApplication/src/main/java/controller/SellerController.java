package controller;

import model.account.Seller;

import java.util.ArrayList;

public class SellerController extends Controller {
    public ArrayList<String> viewPersonalInfo() {
        ArrayList<String> info = viewCommonPersonalInfo();
        info.add(((Seller) currentAccount).getCompanyName());
        info.add(String.valueOf(((Seller) currentAccount).getBalance()));
        return info;
    }

    @SuppressWarnings("DuplicatedCode")
    public void editInformation(String field, String newInformation) throws Exceptions.InvalidFieldException {
        try{
            if(field.equals("companyName"))
                ((Seller) currentAccount).setCompanyName(newInformation);
            else
                editCommonInformation(field, newInformation);
        }
        catch (Exceptions.InvalidFieldException ex){
            throw ex;
        }
    }

    public ArrayList<String> viewCompanyInformation() {
        return null;
    }

    public ArrayList<String> viewSalesHistory() {
        return null;
    }

    public ArrayList<String> manageProducts() {
        return null;
    }

    public boolean isProductWithId(String productId) {
        return false;
    }

    public ArrayList<String> viewProducts(String categoryName){return null;}
    
    public ArrayList<String> viewProduct(String productID) {
        return null;
    }

    public ArrayList<String> viewProductBuyers(String productID) {
        return null;
    }

    public void editProduct(String productID, String field, String newInformation) {
    }

    public void addProduct(ArrayList<String> information) {
    }

    public void removeProduct(String productID) {
    }

    public ArrayList<String> viewSales() {
        return null;
    }

    public ArrayList<String> viewSaleWithId(String saleId) {
        return null;
    }

    public void editSale(String saleId, String field, String newInformation) {
    }

    public void addSale(ArrayList<String> saleInformation) {
    }

    public int viewBalance() {
        return 0;
    }
}
