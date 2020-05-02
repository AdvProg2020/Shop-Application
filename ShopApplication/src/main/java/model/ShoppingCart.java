package model;

import model.account.Customer;

import java.util.HashMap;

public class ShoppingCart {
    private static HashMap<String, ShoppingCart> allShoppingCarts = new HashMap<>();
    private String shoppingCartId;
    private String customerId;
    private HashMap<String, Integer> subProductIds;

    public ShoppingCart(String customerId) {
        this.customerId = customerId;
        subProductIds = new HashMap<>();
        initialize();
    }

    private static String generateNewId(String customerId) {
        //TODO: implement
        return null;
    }

    public static ShoppingCart getShoppingCartById(String shoppingCartId) {
        return allShoppingCarts.get(shoppingCartId);
    }

    public void initialize() {
        if (shoppingCartId == null) {
            shoppingCartId = generateNewId(customerId);
        }
        allShoppingCarts.put(shoppingCartId, this);
        if (getCustomer() != null) {
            getCustomer().setShoppingCart(shoppingCartId);
        }
    }

    public void terminate() {
        allShoppingCarts.remove(shoppingCartId);
    }

    public String getId() {
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
            if (subProduct == null) {
                subProductIds.remove(subProductId);
            } else {
                subProducts.put(subProduct, count);
            }
        }
        return subProducts;
    }

    public void addSubProductCount(String subProductId, int count) {
        if (subProductIds.containsKey(subProductId)) {
            count += subProductIds.get(subProductId);
        }
        if (count <= 0) {
            removeSubProduct(subProductId);
        } else {
            subProductIds.put(subProductId, count);
        }
    }

    public void removeSubProduct(String subProductId) {
        subProductIds.remove(subProductId);
    }
}
