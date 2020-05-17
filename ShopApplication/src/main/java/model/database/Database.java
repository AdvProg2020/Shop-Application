package model.database;

public interface Database {
    void loadDatabase();

    void cart();

    void request();

    void createAdmin();

    void createCustomer();

    void createSeller();

    void editAccount();

    void removeAdmin();

    void removeCustomer();

    void removeSeller();

    void purchase();

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

    void addReview();

    void addRating();

}
