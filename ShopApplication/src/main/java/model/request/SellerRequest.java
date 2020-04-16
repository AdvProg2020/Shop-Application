package model.request;

import model.account.Seller;

public class SellerRequest extends Request{
    private Seller seller;
    private SellerStatus status;

    public SellerRequest(Seller seller) {
        this.seller = seller;
    }

    public Seller getSeller() {
        return seller;
    }

    public SellerStatus getStatus() {
        return status;
    }

    public void setStatus(SellerStatus status) {
        this.status = status;
    }

    public enum SellerStatus {
        pending, editing, verified;
    }
}
