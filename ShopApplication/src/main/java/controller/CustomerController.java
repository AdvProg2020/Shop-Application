package controller;

import model.ShoppingCart;
import model.SubProduct;
import model.account.Customer;

import java.util.ArrayList;
import java.util.HashMap;

public class CustomerController extends Controller {
    public ArrayList<String> viewPersonalInfo() {
        ArrayList<String> info = viewCommonPersonalInfo();
        info.add(String.valueOf(((Customer) currentAccount).getCredit()));
        return info;
    }

    public void editInformation(String field, String newInformation) throws Exceptions.InvalidFieldException {// baraye credit ro koja handle koim?
        try {
            editCommonInformation(field, newInformation);
        }
        catch (Exceptions.InvalidFieldException ex){
            throw ex;
        }
    }

    public ArrayList<String> viewCart() {
        return showProducts();
    }

    public ArrayList<String> showProducts() {
        ArrayList<String> shoppingCart = new ArrayList<>();
        HashMap<SubProduct, Integer> subProducts = ((Customer)currentAccount).getShoppingCart().getSubProducts();
        for (SubProduct subProduct : subProducts.keySet()) {
            shoppingCart.add(subProduct.getSubProductId() + "   "+subProduct.getProduct().getName()+"  "+subProduct.getSeller().getCompanyName()+" number in carts: "+subProducts.get(subProduct));
        }
        return shoppingCart;
    }

    public ArrayList<String> viewProductInCart(String subProductId) throws Exceptions.InvalidSubProductIdException{
        SubProduct subProduct = SubProduct.getSubProductById(subProductId);
        if(!currentCart.getSubProducts().containsKey(subProduct))
            throw new Exceptions.InvalidSubProductIdException(subProductId);
        else{
            try {
                return showProduct(subProduct.getProduct().getProductId());
            }
            catch (Exceptions.InvalidProductIdException ex) {
                return null;
            }
        }
    }

    public void increaseProductInCart(String subProductId, int number) throws Exceptions.InvalidSubProductIdException, Exceptions.UnavailableProductException {
        HashMap<SubProduct, Integer> subProducts = currentCart.getSubProducts();
        SubProduct subProduct = SubProduct.getSubProductById(subProductId);
        if(subProduct == null)
            throw new Exceptions.InvalidSubProductIdException();
        else if(!subProducts.containsKey(subProduct))
            throw new Exceptions.InvalidSubProductIdException(subProductId);
        else if(number + subProducts.get(subProduct) > subProduct.getRemainingCount())
            throw new Exceptions.UnavailableProductException();
        else
            currentCart.changeCount(subProductId, number);
    }

    public void decreaseProductInCart(String subProductId, int number) {

    }

    public double showTotalPrice() {
        return 0;
    }

    public String purchaseTheCart() {return null; }

    public ArrayList<String> showOrder(String orderId) {
        return null;
    }

    public String rateProduct(String productID, int rate) {
        return null;
    }

    public int viewBalance() {
        return ((Customer)currentAccount).getCredit();
    }

    public ArrayList<String> viewDiscountCodes() {
        return null;
    }
}
