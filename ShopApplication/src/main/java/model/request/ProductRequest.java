package model.request;

import model.Product;
import model.SubProduct;

public class ProductRequest extends Request {
    private Product product;
    private SubProduct subProduct;
    private ProductStatus status;

    public ProductRequest(Product product, SubProduct subProduct, ProductStatus status) {
        this.product = product;
        this.subProduct = subProduct;
        this.status = status;
        initialize();
    }

    @Override
    public String getType() {
        return "product";
    }

    public Product getProduct() {
        return product;
    }

    public SubProduct getSubProduct() {
        return subProduct;
    }

    public ProductStatus getStatus() {
        return status;
    }

    public void setStatus(ProductStatus status) {
        this.status = status;
    }

    public enum ProductStatus {
        pending, editing, verified
    }
}
