package model.database;

public interface Database {
    void loadAll();

    void updateAll();

    void createAdmin();

    void createCustomer();

    void createSeller();

    void editAccount();

    void removeAdmin();

    void removeCustomer();

    void removeSeller();

    void request();

    void createDiscount();

    void editDiscount();

    void removeDiscount();

    void createSale();

    void editSale();

    void removeSale();

    void createCategory();

    void editCategory();

    void removeCategory();

    void createProduct();

    void editProduct();

    void removeProduct();

    void createSubProduct();

    void editSubProduct();

    void removeSubProduct();

    void cart();

    void purchase();

    void addReview();

    void addRating();

}
