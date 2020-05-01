package model.request;

import model.Sale;

public class SaleRequest extends Request {
    private Sale sale;
    private SaleStatus status;

    public SaleRequest(Sale sale, SaleStatus status) {
        this.sale = sale;
        this.status = status;
        initialize();
    }

    @Override
    public String getType() {
        return "sale";
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
        pending, editing, verified
    }
}
