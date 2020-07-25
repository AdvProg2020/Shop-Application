package Server.model.request;

import Server.model.Wallet;
import Server.model.account.Seller;
import Server.model.database.Database;

public class AddSellerRequest extends Request {
    private Seller seller;
    private double balance;

    public AddSellerRequest(Seller seller, double balance) {
        super();
        this.seller = seller;
        initialize();
    }

    @Override
    public void accept() {
        seller.initialize();
        new Wallet(seller.getId(), balance);
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
