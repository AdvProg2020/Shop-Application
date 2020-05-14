package model.request;

import model.Sale;

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

    public Sale getSale() {
        return sale;
    }
}
