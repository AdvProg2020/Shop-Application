package model.request;

import model.Sale;
import model.account.Seller;
import model.database.Database;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class EditSaleRequest extends Request implements SellerRequest {
    private String saleId;
    private Field field;
    private String newValue;
    private String oldValue;


    public EditSaleRequest(String saleId, String newValue, Field field) {
        super();
        this.saleId = saleId;
        this.field = field;
        this.newValue = newValue;
        initialize();
    }

    @Override
    public void accept() {
        setOldValue();
        Sale sale = Sale.getSaleById(saleId);
        try {
            SimpleDateFormat parser = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
            switch (field) {
                case START_DATE:
                    sale.setStartDate(parser.parse(newValue));
                    break;
                case END_DATE:
                    sale.setEndDate(parser.parse(newValue));
                    break;
                case PERCENTAGE:
                    sale.setPercentage(Double.parseDouble(newValue));
                    break;
                case MAXIMUM:
                    sale.setMaximumAmount(Double.parseDouble(newValue));
            }
        } catch (ParseException ignored) {
        }
        super.accept();
    }

    @Override
    public void decline() {
        setOldValue();
        super.decline();
    }

    @Override
    protected boolean isInvalid() {
        return (status == RequestStatus.PENDING) && (getSale() == null);
    }

    public Sale getSale() {
        return Sale.getSaleById(saleId);
    }

    @Override
    public Seller getSeller() {
        return getSale().getSeller();
    }

    public Field getField() {
        return field;
    }

    public String getNewValue() {
        return newValue;
    }

    public String getOldValue() {
        if (suspended) return oldValue;

        setOldValue();
        return oldValue;
    }

    private void setOldValue() {
        Sale sale = Sale.getSaleById(saleId);
        SimpleDateFormat parser = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
        switch (field) {
            case START_DATE:
                oldValue = parser.format(sale.getStartDate());
                break;
            case END_DATE:
                oldValue = parser.format(sale.getEndDate());
                break;
            case PERCENTAGE:
                oldValue = String.valueOf(sale.getPercentage());
                break;
            case MAXIMUM:
                oldValue = String.valueOf(sale.getMaximumAmount());
        }
    }

    @Override
    public void updateDatabase(Database database) {
        database.editSale();
    }

    public enum Field {
        START_DATE,
        END_DATE,
        PERCENTAGE,
        MAXIMUM
    }

}
