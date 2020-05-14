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

    public Product getProduct() {
        return product;
    }

    public SubProduct getSubProduct() {
        return subProduct;
    }
}
