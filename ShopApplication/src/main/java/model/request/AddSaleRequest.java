package model.request;

import model.Sale;
import model.account.Seller;
import model.database.Database;

public class AddSaleRequest extends Request implements SellerRequest {
    private Sale sale;

    public AddSaleRequest(Sale sale) {
        super();
        this.sale = sale;
        initialize();
    }

    @Override
    public void accept() {
        sale.initialize();
        super.accept();
    }

    @Override
    protected boolean isInvalid() {
        return (status == RequestStatus.PENDING) && (sale.getSeller() == null);
    }

    public Sale getSale() {
        return sale;
    }

    @Override
    public Seller getSeller() {
        return sale.getSeller();
    }

    @Override
    public void updateDatabase(Database database) {
        database.createSale();
    }
}
