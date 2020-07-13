package model.database;

public interface Database {
    void loadAll();

    void updateAll();

    void createAdmin();

    void createCustomer();

    void createSeller();

    void createSupporter();

    void editAccount();

    void removeAdmin();

    void removeCustomer();

    void removeSeller();

    void removeSupporter();

    void cart();

    void wallet();

    void purchase();

    void chat();

    void request();

    void createDiscount();

    void editDiscount();

    void removeDiscount();

    void createSale();

    void editSale();

    void removeSale();

    void createAuction();

    void editAuction();

    void removeAuction();

    void createCategory();

    void editCategory();

    void removeCategory();

    void createProduct();

    void editProduct();

    void removeProduct();

    void createSubProduct();

    void editSubProduct();

    void removeSubProduct();

    void createFile();

    void editFile();

    void removeFile();

    void createSubFile();

    void editSubFile();

    void removeSubFile();

    void addReview();

    void addRating();


}
