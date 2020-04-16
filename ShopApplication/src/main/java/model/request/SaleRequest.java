package model.request;

import model.Sale;

public class SaleRequest extends Request {
    private Sale sale;
    private SaleStatus status;

    public SaleRequest(Sale sale) {
        this.sale = sale;
    }

    public SaleStatus getStatus() {
        return status;
    }

    public Sale getSale() {
        return sale;
    }

    public void setStatus(SaleStatus status) {
        this.status = status;
    }

    public enum SaleStatus {
        pending, editing, verified;
    }
}
