package model;

import model.account.Customer;

import java.util.HashMap;

public class ShoppingCart {
    private static HashMap<String, ShoppingCart> allShoppingCarts = new HashMap<>();
    private String shoppingCartId;
    private String customerId;
    private HashMap<String, Integer> subProductIds;

    public ShoppingCart(String customerId) {
        shoppingCartId = getNewId(customerId);
        this.customerId = customerId;
        allShoppingCarts.put(shoppingCartId, this);
        getCustomer().setShoppingCart(shoppingCartId);
    }

    private static String getNewId(String customerId) {
        //TODO: implement
        return null;
    }

    public static ShoppingCart getShoppingCartById(String shoppingCartId) {
        return allShoppingCarts.get(shoppingCartId);
    }

    public static void mergeShoppingCarts(String oldShoppingCartId, String newShoppingCartId) {
        ShoppingCart oldShoppingCart = ShoppingCart.getShoppingCartById(oldShoppingCartId);
        ShoppingCart newShoppingCart = ShoppingCart.getShoppingCartById(newShoppingCartId);
        for (String subProductId : oldShoppingCart.subProductIds.keySet()) {
            int count = oldShoppingCart.subProductIds.get(subProductId);
            newShoppingCart.subProductIds.put(subProductId, count);
        }
    }

    public String getShoppingCartId() {
        return shoppingCartId;
    }

    public Customer getCustomer() {
        return Customer.getCustomerById(customerId);
    }

    public HashMap<SubProduct, Integer> getSubProducts() {
        HashMap<SubProduct, Integer> subProducts = new HashMap<>();
        for (String subProductId : subProductIds.keySet()) {
            SubProduct subProduct = SubProduct.getSubProductById(subProductId);
            int count = subProductIds.get(subProductId);
            subProducts.put(subProduct, count);
        }
        return subProducts;
    }

    public void addSubProduct(String subProductId, int count) {
        subProductIds.put(subProductId, count);
    }

    public void changeCount(String subProductId, int changeAmount) {
        int newCount = subProductIds.get(subProductId) + changeAmount;
        if (newCount <= 0) {
            removeSubProduct(subProductId);
        } else {
            addSubProduct(subProductId, newCount);
        }
    }

    public void removeSubProduct(String subProductId) {
        subProductIds.remove(subProductId);
    }
}
