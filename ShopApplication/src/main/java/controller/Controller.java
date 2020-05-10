package controller;

import model.*;
import model.account.Account;
import model.account.Admin;
import model.account.Customer;
import model.account.Seller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;

public class Controller {
    protected static Account currentAccount;
    protected static ShoppingCart currentCart;
    protected static DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");

    //Done!
    public void usernameTypeValidation(String username, String type) throws Exceptions.ExistedUsernameException, Exceptions.AdminRegisterException {
        if (Account.getAccountByUsername(username) == null)
            throw new Exceptions.ExistedUsernameException(username);
        else if (type.equalsIgnoreCase("admin") && (Admin.getManager() != null))
            throw new Exceptions.AdminRegisterException();
    }

    //Todo
    public void creatAccount(String type, ArrayList<String> information) {// lazeme inja ham exception bezarim?
    }

    //Todo
    public void login(String username, String password) throws Exceptions.WrongPasswordException, Exceptions.NotExistedUsernameException {
        Account account = Account.getAccountByUsername(username);
        if (account == null)
            throw new Exceptions.NotExistedUsernameException(username);
        if (!account.getPassword().equals(password))
            throw new Exceptions.WrongPasswordException();
        currentAccount = account;
        // baraye cart kari lazem nist bokonim? account cart nadare...
    }

    //Done!!
    public String getType() {
        if (currentAccount == null)
            return "anonymous";
        return currentAccount.getType();
    }

    //Todo
    public ArrayList<String> productsStatus() {
        return null;
    }

    //Done!!
    private ArrayList<Product> sortProducts(String sortBy, ArrayList<Product> products) {
        if (sortBy.equalsIgnoreCase("price")) {
            products.sort(new Comparator<Product>() {
                @Override
                public int compare(Product o1, Product o2) {
                    return Double.compare(o1.getMinPrice(), o2.getMinPrice());
                }
            });
        } else if (sortBy.equalsIgnoreCase("rating score")) {
            products.sort(new Comparator<Product>() {
                @Override
                public int compare(Product o1, Product o2) {
                    return Double.compare(o1.getAverageRatingScore(), o2.getAverageRatingScore());
                }
            });
        } else if (sortBy.equalsIgnoreCase("name")) {
            products.sort(new Comparator<Product>() {
                @Override
                public int compare(Product o1, Product o2) {
                    return o1.getName().compareTo(o2.getName());
                }
            });
        } else if (sortBy.equalsIgnoreCase("category name")) {
            products.sort(new Comparator<Product>() {
                @Override
                public int compare(Product o1, Product o2) {
                    return o1.getCategory().getName().compareTo(o2.getCategory().getName());
                }
            });
        } else {
            products.sort(new Comparator<Product>() {
                @Override
                public int compare(Product o1, Product o2) {
                    return Integer.compare(o1.getViewCount(), o2.getViewCount());
                }
            });
        }
        return products;
    }

    //Done!!
    private ArrayList<Product> filterProducts(boolean available, double minPrice, double maxPrice, String contains, String brand,
                                              String storeName, double minRatingScore, ArrayList<Product> products) {
        if (available)
            products.removeIf(product -> (product.getTotalRemainingCount() == 0));
        products.removeIf(product -> product.getMinPrice() > maxPrice);
        if (maxPrice != 0)
            products.removeIf(product -> product.getMaxPrice() < minPrice);
        if (!contains.equals(""))
            products.removeIf(product -> !(product.getName().toLowerCase().contains(contains.toLowerCase())));
        if (!brand.equals(""))
            products.removeIf(product -> !(product.getBrand().toLowerCase().contains(brand.toLowerCase())));
        if (!storeName.equals("")) {
            products.removeIf(product -> !product.isSoldInStoreWithName(storeName.toLowerCase()));
        }
        products.removeIf(product -> product.getAverageRatingScore() < minRatingScore);
        return products;
    }

    //Done!!
    private ArrayList<String[]> productToIdName(ArrayList<Product> products) {
        ArrayList<String[]> productIdNames = new ArrayList<>();
        for (Product product : products) {
            productIdNames.add(productPack(product));
        }
        return productIdNames;
    }

    private String[] productPack(Product product) {
        String[] productPack = new String[2];
        productPack[0] = product.getId();
        productPack[1] = product.getName();
        return productPack;
    }

    //Done!!
    public ArrayList<String[]> viewCategories() {
        try {
            return getSubCategoriesOfThisCategory(Category.getSuperCategory().getName());
        } catch (Exceptions.InvalidCategoryException e) {
            return null;
        }
    }

    //Done!!
    public ArrayList<String[]> getSubCategoriesOfThisCategory(String categoryName) throws Exceptions.InvalidCategoryException {
        Category category = Category.getCategoryByName(categoryName);
        if (category == null)
            throw new Exceptions.InvalidCategoryException(categoryName);
        else {
            ArrayList<String[]> categoryIdNames = new ArrayList<>();
            String[] categoryPack = new String[2];
            for (Category subCategory : category.getSubCategories()) {
                categoryPack[0] = subCategory.getId();
                categoryPack[1] = subCategory.getName();
                categoryIdNames.add(categoryPack);
            }
            return categoryIdNames;
        }
    }

    //Done!!
    public ArrayList<String[]> getProductsOfThisCategory(String categoryName) throws Exceptions.InvalidCategoryException {
        Category category = Category.getCategoryByName(categoryName);
        if (category == null)
            throw new Exceptions.InvalidCategoryException(categoryName);
        else
            return productToIdName(category.getProducts());
    }

    //Done!!
    public ArrayList<String[]> showProducts(ArrayList<String> productIds, String sortBy, String[] filterBy) throws Exceptions.InvalidProductIdException {
        ArrayList<Product> products = new ArrayList<>();
        Product product;
        for (String productId : productIds) {
            if ((product = Product.getProductById(productId)) == null)
                throw new Exceptions.InvalidProductIdException(productId);
            else
                products.add(product);
        }
        products = filterProducts(filterBy[0].equals("true"), Double.parseDouble(filterBy[1]), Double.parseDouble(filterBy[2])
                , filterBy[3], filterBy[4], filterBy[5], Double.parseDouble(filterBy[6]), products);

        products = sortProducts(sortBy, products);
        return productToIdName(products);
    }

    //Done!!
    public void showProduct(String productId) throws Exceptions.InvalidProductIdException {
        Product product = Product.getProductById(productId);
        if (product == null)
            throw new Exceptions.InvalidProductIdException(productId);
        else {
            product.increaseViewCount();
        }
    }

    //Done!!
    public String[] digest(String productId) throws Exceptions.InvalidProductIdException {
        Product product = Product.getProductById(productId);
        if (product == null)
            throw new Exceptions.InvalidProductIdException(productId);
        return digest(product);
    }

    //Done!!
    protected String[] digest(Product product) {
        String[] productInfo = new String[5];
        productInfo[0] = product.getId();
        productInfo[1] = product.getName();
        productInfo[2] = product.getBrand();
        productInfo[3] = product.getInfoText();
        productInfo[4] = Double.toString(product.getAverageRatingScore());
        return productInfo;
    }

    //Todo
    public ArrayList<String> attributes(String productId) {
        return null;
    }

    //Done!!
    public ArrayList<String[]> subProductsOfAProduct(String productId) throws Exceptions.InvalidProductIdException {
        Product product = Product.getProductById(productId);
        if (product == null)
            throw new Exceptions.InvalidProductIdException(productId);
        ArrayList<String[]> subProducts = new ArrayList<>();
        String[] subProductPack = new String[4];
        for (SubProduct subProduct : product.getSubProducts()) {
            subProductPack[0] = subProduct.getId();
            subProductPack[1] = subProduct.getSeller().getStoreName();
            subProductPack[2] = Double.toString(subProduct.getPriceWithSale());
            subProductPack[3] = Integer.toString(subProduct.getRemainingCount());
            subProducts.add(subProductPack);
        }
        return subProducts;
    }

    //Done!!
    public ArrayList<String[]> reviewsOfAProduct(String productId) throws Exceptions.InvalidProductIdException {
        Product product = Product.getProductById(productId);
        if (product == null)
            throw new Exceptions.InvalidProductIdException(productId);
        ArrayList<String[]> reviews = new ArrayList<>();
        String[] reviewPack = new String[3];
        for (Review review : product.getReviews()) {
            reviewPack[0] = review.getReviewer().getUsername();
            reviewPack[1] = review.getTitle();
            reviewPack[2] = review.getText();
            reviews.add(reviewPack);
        }
        return reviews;
    }

    //Todo
    public void addToCart(String subProductId) throws Exceptions.InvalidSubProductIdException {
    }

    //Todo
    public boolean selectSeller(String productId, String sellerId) {
        return false;
    }

    //Todo
    public ArrayList<String> compare(String productId1, String productId2) {
        return null;
    }

    //Done!!  [ reviewerUsername, reviewText]
    public ArrayList<String[]> reviews(String productId) throws Exceptions.InvalidProductIdException {
        Product product = Product.getProductById(productId);
        if (product == null)
            throw new Exceptions.InvalidProductIdException(productId);
        else {
            ArrayList<String[]> reviews = new ArrayList<>();
            String[] reviewPack = new String[2];
            for (Review review : product.getReviews()) {
                reviewPack[0] = review.getReviewer().getUsername();
                reviewPack[1] = review.getText();
                reviews.add(reviewPack);
            }
            return reviews;
        }
    }

    //Done!!
    public void addReview(String productId, String title, String text) throws Exceptions.InvalidProductIdException, Exceptions.NotLoggedInException {
        if (currentAccount == null)
            throw new Exceptions.NotLoggedInException();
        else {
            if (Product.getProductById(productId) == null)
                throw new Exceptions.InvalidProductIdException(productId);
            else
                new Review(currentAccount.getId(), productId, title, text);
        }
    }

    //Done!!
    public ArrayList<String[]> sales() {
        ArrayList<String[]> sales = new ArrayList<>();
        for (Sale sale : Sale.getAllSales()) {
            sales.add(getSaleInfo(sale));
        }
        return sales;
    }

    //Done!!
    protected String[] getPersonalInfo(Account account) {
        String[] info;
        if (account instanceof Customer) {
            info = new String[7];
            info[6] = String.valueOf(((Customer) account).getBalance());
        } else if (account instanceof Seller) {
            info = new String[8];
            info[6] = String.valueOf(((Seller) account).getBalance());
            info[7] = ((Seller) account).getStoreName();
        } else
            info = new String[6];
        info[0] = account.getUsername();
        info[1] = account.getType();
        info[2] = account.getFirstName();
        info[3] = account.getLastName();
        info[4] = account.getEmail();
        info[5] = account.getPhone();
        return info;
    }

    //Done!!
    public String[] viewPersonalInfo() {
        return getPersonalInfo(currentAccount);
    }

    //Done!!
    protected void editCommonInformation(String field, String newInformation) throws Exceptions.InvalidFieldException {
        switch (field) {
            case "firstName":
                currentAccount.setFirstName(newInformation);
                break;
            case "lastName":
                currentAccount.setLastName(newInformation);
                break;
            case "email":
                currentAccount.setEmail(newInformation);
                break;
            case "phone":
                currentAccount.setPhone(newInformation);
                break;
            case "password":
                currentAccount.setPassword(newInformation);
                break;
            default:
                throw new Exceptions.InvalidFieldException();
        }
    }

    //Done!!
    protected String[] getSaleInfo(Sale sale) {
        String[] salePack = new String[5];
        salePack[0] = Double.toString(sale.getPercentage());
        salePack[1] = sale.getSeller().getStoreName();
        salePack[2] = dateFormat.format(sale.getStartDate());
        salePack[3] = dateFormat.format(sale.getEndDate());
        salePack[4] = Integer.toString(sale.getSubProducts().size());
        return salePack;
    }

    //Done!!
    public ArrayList<String[]> getProductInSale(String saleId) throws Exceptions.InvalidSaleIdException {
        Sale sale = Sale.getSaleById(saleId);
        if (sale == null)
            throw new Exceptions.InvalidSaleIdException(saleId);
        else
            return getProductsInSale(sale);
    }

    //Done!!
    protected ArrayList<String[]> getProductsInSale(Sale sale) {
        ArrayList<String[]> productsInSale = new ArrayList<>();
        String[] productPack = new String[2];
        for (SubProduct subProduct : sale.getSubProducts()) {
            productPack[0] = subProduct.getProduct().getName();
            productPack[1] = subProduct.getProduct().getId();
            productsInSale.add(productPack);
        }
        return productsInSale;
    }
}
