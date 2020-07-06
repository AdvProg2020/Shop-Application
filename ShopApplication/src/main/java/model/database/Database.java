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

    void createSellable();

    void editSellable();

    void removeSellable();

    void createSubSellable();

    void editSubSellable();

    void removeSubSellable();

    void cart();

    void wallet();

    void purchase();

    void addReview();

    void addRating();

    void chat();

}
