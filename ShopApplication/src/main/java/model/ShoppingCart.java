package model;

import model.account.Customer;

import java.util.HashMap;

public class ShoppingCart {
    private String shoppingCartId;
    private Customer customer;
    private HashMap<SubProduct, Integer> products;

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public HashMap<SubProduct, Integer> getProducts() {
        return products;
    }

    public void addProduct(Product product, int count) {}

    public void removeProduct(Product product) {}
}
