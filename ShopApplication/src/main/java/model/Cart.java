package model;

import model.ModelUtilities.ModelOnly;
import model.account.Customer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Cart implements ModelBasic {
    private static Map<String, Cart> allCarts = new HashMap<>();
    private static int lastNum = 1;
    private String cartId;
    private String customerId; // can be null
    private Map<String, Integer> subProductIds;

    public Cart(String customerId) {
        this.customerId = customerId;
        subProductIds = new HashMap<>();
        initialize();
    }

    public static List<Cart> getAllCarts() {
        return new ArrayList<>(allCarts.values());
    }

    public static Cart getCartById(String cartId) {
        return allCarts.get(cartId);
    }

    @ModelOnly
    public static void removeSubProductFromCarts(String subProductId) {
        for (Cart cart : allCarts.values()) {
            cart.subProductIds.remove(subProductId);
        }
    }

    @ModelOnly
    public static void mergeCarts(String srcCartId, String destCartId) {
        Cart srcCart = allCarts.get(srcCartId);
        Cart destCart = allCarts.get(destCartId);
        for (Map.Entry<String, Integer> entry : srcCart.subProductIds.entrySet()) {
            destCart.addSubProductCount(entry.getKey(), entry.getValue());
        }
        srcCart.terminate();
    }

    @Override
    public void initialize() {
        if (cartId == null)
            cartId = ModelUtilities.generateNewId(getClass().getSimpleName(), lastNum);
        allCarts.put(cartId, this);
        lastNum++;

        if (customerId != null)
            getCustomer().setCart(cartId);
    }

    @ModelOnly
    public void terminate() {
        allCarts.remove(cartId);
    }

    @Override
    public boolean isSuspended() {
        return false;
    }

    @Override
    public String getId() {
        return cartId;
    }

    public Customer getCustomer() {
        return Customer.getCustomerById(customerId);
    }

    public Map<SubProduct, Integer> getSubProducts() {
        Map<SubProduct, Integer> subProducts = new HashMap<>();
        for (Map.Entry<String, Integer> entry : subProductIds.entrySet()) {
            SubProduct subProduct = SubProduct.getSubProductById(entry.getKey());
            subProducts.put(subProduct, entry.getValue());
        }

        return subProducts;
    }

    public int getCountOfaSubProduct(String subProductId) {
        if (!subProductIds.containsKey(subProductId))
            return 0;

        return subProductIds.get(subProductId);
    }

    public void addSubProductCount(String subProductId, int count) {
        if (subProductIds.containsKey(subProductId))
            count += subProductIds.get(subProductId);

        if (count <= 0)
            removeSubProduct(subProductId);
        else
            subProductIds.put(subProductId, count);
    }

    public void removeSubProduct(String subProductId) {
        subProductIds.remove(subProductId);
    }

    public double getTotalPrice() {
        double total = 0;
        Map<SubProduct, Integer> subProducts = getSubProducts();
        for (SubProduct subProduct : subProducts.keySet()) {
            total += subProduct.getPriceWithSale() * subProducts.get(subProduct);
        }

        return total;
    }

    public void clearCart() {
        subProductIds = new HashMap<>();
    }
}
