package model.request;

import model.Product;
import model.SubProduct;
import model.database.Database;

public class AddProductRequest extends Request {
    private Product product;
    private SubProduct subProduct;

    public AddProductRequest(Product product, SubProduct subProduct) {
        super();
        this.product = product;
        this.subProduct = subProduct;
    }

    @Override
    public void accept() {
        super.accept();
        if (product != null) {
            product.initialize();
            subProduct.setProductId(product.getId());
        }
        subProduct.initialize();
    }

    @Override
    protected boolean isInvalid() {
        if (product != null)
            return (product.getCategory() == null);

        return (subProduct.getProduct() == null || subProduct.getSeller() == null);

    }

    public Product getProduct() {
        return product;
    }

    public SubProduct getSubProduct() {
        return subProduct;
    }

    @Override
    public void updateDatabase(Database database) {
        if (product == null)
            database.createSubProduct();
        else
            database.createProduct();
    }
}
