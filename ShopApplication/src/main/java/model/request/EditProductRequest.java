package model.request;

import model.Product;
import model.SubProduct;
import model.account.Seller;
import model.database.Database;

public class EditProductRequest extends Request implements SellerRequest {
    private String subProductId;
    private Field field;
    private String newValue;
    private String oldValue;

    public EditProductRequest(String subProductId, Field field, String newValue) {
        super();
        this.subProductId = subProductId;
        this.field = field;
        this.newValue = newValue;
        initialize();
    }

    @Override
    public void accept() {
        setOldValue();
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
            case IMAGE_PATH:
                product.setImagePath(newValue);
                break;
            case SUB_PRICE:
                subProduct.setPrice(Double.parseDouble(newValue));
                break;
            case SUB_COUNT:
                int changeAmount = Integer.parseInt(newValue) - subProduct.getRemainingCount();
                subProduct.changeRemainingCount(changeAmount);
                break;
            case PROPERTY:
                String[] data = newValue.split(",");
                product.setProperty(data[0], data[1]);
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
        return (status == RequestStatus.PENDING) && (getSubProduct() == null);
    }

    public SubProduct getSubProduct() {
        return SubProduct.getSubProductById(subProductId);
    }

    @Override
    public Seller getSeller() {
        return getSubProduct().getSeller();
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
        SubProduct subProduct = SubProduct.getSubProductById(subProductId);
        Product product = subProduct.getProduct();
        switch (field) {
            case NAME:
                oldValue = product.getName();
                break;
            case BRAND:
                oldValue = product.getBrand();
                break;
            case INFO_TEXT:
                oldValue = product.getInfoText();
                break;
            case IMAGE_PATH:
                oldValue = product.getImagePath();
                break;
            case SUB_PRICE:
                oldValue = String.valueOf(subProduct.getRawPrice());
                break;
            case SUB_COUNT:
                oldValue = String.valueOf(subProduct.getRemainingCount());
                break;
            case PROPERTY:
                String[] data = newValue.split(",");
                oldValue = data[0] + "," + product.getValue(data[0]);
        }
    }

    @Override
    public void updateDatabase(Database database) {
        if (field.toString().startsWith("SUB"))
            database.editSubProduct();
        else
            database.editProduct();
    }

    public enum Field {
        NAME,
        BRAND,
        INFO_TEXT,
        IMAGE_PATH,
        PROPERTY,
        SUB_PRICE,
        SUB_COUNT
    }
}
