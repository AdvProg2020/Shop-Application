package model.request;

import model.account.Seller;
import model.database.Database;

public class AddSellerRequest extends Request {
    private Seller seller;

    public AddSellerRequest(Seller seller) {
        super();
        this.seller = seller;
    }

    @Override
    public void accept() {
        seller.initialize();
        super.accept();
    }

    @Override
    protected boolean isInvalid() {
        return false;
    }

    public Seller getSeller() {
        return seller;
    }

    @Override
    public void updateDatabase(Database database) {
        database.createSeller();
    }
}
