package model.request;

import model.Product;
import model.SubProduct;

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
        boolean invalid;
        if (product != null)
            invalid = (product.getCategory() == null);
        else
            invalid = (subProduct.getProduct() == null || subProduct.getSeller() == null);

        if (invalid)
            terminate();

        return invalid;
    }

    public Product getProduct() {
        return product;
    }

    public SubProduct getSubProduct() {
        return subProduct;
    }
}
