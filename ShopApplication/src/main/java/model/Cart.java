package model;

import model.ModelUtilities.ModelOnly;
import model.account.Customer;
import model.sellable.SubSellable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Cart implements ModelBasic {
    private static Map<String, Cart> allCarts = new HashMap<>();
    private static int lastNum = 1;
    private String cartId;
    private String customerId; // can be null
    private Map<String, Integer> subSellableIds;

    public Cart(String customerId) {
        this.customerId = customerId;
        subSellableIds = new HashMap<>();
        initialize();
    }

    public static List<Cart> getAllCarts() {
        return ModelUtilities.getAllInstances(allCarts.values(), false);
    }

    public static Cart getCartById(String cartId) {
        return ModelUtilities.getInstanceById(allCarts, cartId, false);
    }

    @ModelOnly
    public static void removeSubSellableFromAll(String subSellableId) {
        for (Cart cart : allCarts.values()) {
            cart.subSellableIds.remove(subSellableId);
        }
    }

    @ModelOnly
    public static void mergeCarts(String srcCartId, String destCartId) {
        Cart srcCart = getCartById(srcCartId);
        Cart destCart = getCartById(destCartId);
        for (Map.Entry<String, Integer> entry : srcCart.subSellableIds.entrySet()) {
            destCart.addSubSellableCount(entry.getKey(), entry.getValue());
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

    public Map<SubSellable, Integer> getSubSellables() {
        Map<SubSellable, Integer> subSellables = new HashMap<>();
        for (Map.Entry<String, Integer> entry : subSellableIds.entrySet()) {
            SubSellable subSellable = SubSellable.getSubSellableById(entry.getKey());
            subSellables.put(subSellable, entry.getValue());
        }

        return subSellables;
    }

    public int getCountOfaSubSellable(String subSellableId) {
        if (!subSellableIds.containsKey(subSellableId))
            return 0;

        return subSellableIds.get(subSellableId);
    }

    public void addSubSellableCount(String subProductId, int count) {
        if (subSellableIds.containsKey(subProductId))
            count += subSellableIds.get(subProductId);

        if (count <= 0)
            removeSubSellable(subProductId);
        else
            subSellableIds.put(subProductId, count);
    }

    public void removeSubSellable(String subSellable) {
        subSellableIds.remove(subSellable);
    }

    public void clearCart() {
        subSellableIds = new HashMap<>();
    }

    public double getTotalPrice() {
        double total = 0;
        Map<SubSellable, Integer> subSellables = getSubSellables();
        for (SubSellable subSellable : subSellables.keySet()) {
            total += subSellable.getPriceWithSale() * subSellables.get(subSellable);
        }

        return total;
    }

    private Customer getCustomer() {
        return Customer.getCustomerById(customerId);
    }
}
