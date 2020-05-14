package model.database;

public interface Database {
    void loadDatabase();

    void createAdmin();

    void createCustomer();

    void createSeller();

    void editAccount();

    void removeAdmin();

    void removeCustomer();

    void removeSeller();

    void purchase();

    void cart();

    void request();

    void discount();

    void sale();

    void createCategory();

    void editCategory();

    void removeCategory();

    void createProduct();

    void editProduct();

    void removeProduct();

    void addSubProduct();

    void editSubProduct();

    void removeSubProduct();

    void addReview();

    void addRating();

}
