package model.request;

import model.Sale;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class EditSaleRequest extends Request {
    private String saleId;
    private Field field;
    private String newValue;


    public EditSaleRequest(String saleId, String newValue, Field field) {
        super();
        this.saleId = saleId;
        this.field = field;
        this.newValue = newValue;
    }

    @Override
    public void accept() {
        super.accept();
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
    }

    @Override
    public String getType() {
        return "EditSale";
    }

    public String getSaleId() {
        return saleId;
    }

    public Field getField() {
        return field;
    }

    public String getNewValue() {
        return newValue;
    }

    public enum Field {
        START_DATE,
        END_DATE,
        PERCENTAGE,
        MAXIMUM
    }

}
