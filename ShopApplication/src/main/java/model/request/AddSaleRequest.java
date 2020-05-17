package model.request;

import model.Sale;
import model.database.Database;

public class AddSaleRequest extends Request {
    private Sale sale;

    public AddSaleRequest(Sale sale) {
        super();
        this.sale = sale;
    }

    @Override
    public void accept() {
        super.accept();
        sale.initialize();
    }

    @Override
    protected boolean isInvalid() {
        return (sale.getSeller() == null);
    }

    public Sale getSale() {
        return sale;
    }

    @Override
    public void updateDatabase(Database database) {
        database.createSale();
    }
}
