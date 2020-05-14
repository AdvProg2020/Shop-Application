package model.request;

import model.Product;
import model.SubProduct;

public class EditProductRequest extends Request {
    private String subProductId;
    private Field field;
    private String newValue;

    public EditProductRequest(String subProductId, Field field, String newValue) {
        super();
        this.subProductId = subProductId;
        this.field = field;
        this.newValue = newValue;
    }

    @Override
    public void accept() {
        super.accept();
        SubProduct subProduct = SubProduct.getSubProductById(subProductId);
        Product product = subProduct.getProduct();
        switch (field) {
            case NAME:
                product.setName(newValue);
                break;
            case BRAND:
                product.setBrand(newValue);
                break;
            case INFO_TEXT:
                product.setInfoText(newValue);
                break;
            case PRICE:
                subProduct.setPrice(Double.parseDouble(newValue));
                break;
            case COUNT:
                int changeAmount = Integer.parseInt(newValue) - subProduct.getRemainingCount();
                subProduct.changeRemainingCount(changeAmount);
        }
    }

    @Override
    protected boolean isInvalid() {
        boolean invalid = (getSubProduct() == null);

        if (invalid)
            terminate();

        return invalid;
    }

    public SubProduct getSubProduct() {
        return SubProduct.getSubProductById(subProductId);
    }

    public Field getField() {
        return field;
    }

    public String getNewValue() {
        return newValue;
    }

    public enum Field {
        NAME,
        BRAND,
        INFO_TEXT,
        PRICE,
        COUNT
    }
}
