package model.request;

import model.account.Seller;
import model.database.Database;
import model.sellable.Product;
import model.sellable.SubProduct;

public class AddProductRequest extends Request implements SellerRequest {
    private Product product;
    private SubProduct subProduct;

    public AddProductRequest(Product product, SubProduct subProduct) {
        super();
        this.product = product;
        this.subProduct = subProduct;
        initialize();
    }

    @Override
    public void accept() {
        if (product != null) {
            product.initialize();
            subProduct.setSellableId(product.getId());
        }
        subProduct.initialize();
        super.accept();
    }

    @Override
    protected boolean isInvalid() {
        if (product != null)
            return (status == RequestStatus.PENDING) &&(product.getCategory() == null);

        return (status == RequestStatus.PENDING) && (subProduct.getProduct() == null || subProduct.getSeller() == null);
    }

    public Product getProduct() {
        return product;
    }

    public SubProduct getSubProduct() {
        return subProduct;
    }

    @Override
    public Seller getSeller() {
        return subProduct.getSeller();
    }

    @Override
    public void updateDatabase(Database database) {
        if (product == null)
            database.createSubSellable();
        else
            database.createSellable();
    }
}
