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
            case CATEGORY:
                product.setCategory(newValue);
                break;
            case PRICE:
                subProduct.setPrice(Double.parseDouble(newValue));
        }
    }

    public String getSubProductId() {
        return subProductId;
    }

    public Field getField() {
        return field;
    }

    public String getNewValue() {
        return newValue;
    }

    //TODO: count, delete category it is hard to handle
    public enum Field {
        NAME,
        BRAND,
        INFO_TEXT,
        CATEGORY,
        PRICE
    }
}
