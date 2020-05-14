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

    @Override
    protected boolean isInvalid() {
        boolean invalid = (sale.getSeller() == null);

        if (invalid)
            terminate();

        return invalid;
    }

    public Sale getSale() {
        return sale;
    }
}
