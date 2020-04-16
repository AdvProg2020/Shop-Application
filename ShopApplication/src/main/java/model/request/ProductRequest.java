package model.request;

import model.Product;

public class ProductRequest extends Request{
    private Product product;
    private ProductStatus status;

    public ProductRequest(Product product) {
        this.product = product;
    }

    public Product getProduct() {
        return product;
    }

    public ProductStatus getStatus() {
        return status;
    }

    public void setStatus(ProductStatus status) {
        this.status = status;
    }

    public enum ProductStatus {
        pending, editing, verified;
    }
}
