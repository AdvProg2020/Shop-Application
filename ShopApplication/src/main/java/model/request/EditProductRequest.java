package model.request;

import model.Product;
import model.SubProduct;
import model.account.Seller;
import model.database.Database;

public class EditProductRequest extends Request implements SellerRequest {
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
            case SUB_PRICE:
                subProduct.setPrice(Double.parseDouble(newValue));
                break;
            case SUB_COUNT:
                int changeAmount = Integer.parseInt(newValue) - subProduct.getRemainingCount();
                subProduct.changeRemainingCount(changeAmount);
        }
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
        SUB_PRICE,
        SUB_COUNT
    }
}
