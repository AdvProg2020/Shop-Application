package model.request;

import model.account.Seller;

public class AddSellerRequest extends Request {
    private Seller seller;

    public AddSellerRequest(Seller seller) {
        super();
        this.seller = seller;
    }

    @Override
    public void accept() {
        super.accept();
        seller.initialize();
    }

    public Seller getSeller() {
        return seller;
    }
}
