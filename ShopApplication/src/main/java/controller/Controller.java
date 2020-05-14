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
    /**
     * @param username
     * @param type
     * @throws Exceptions.ExistedUsernameException
     * @throws Exceptions.AdminRegisterException
     */
    public void usernameTypeValidation(String username, String type) throws Exceptions.ExistedUsernameException, Exceptions.AdminRegisterException {
        if (Account.getAccountByUsername(username) == null)
            throw new Exceptions.ExistedUsernameException(username);
        else if (type.equalsIgnoreCase("admin") && (Admin.getManager() == null))
            throw new Exceptions.AdminRegisterException();
    }

    //Done!! Todo: Shayan please check this
    /**
     * @param type
     *        information: 1- customer:
     *                     * String username, String password, String firstName, String lastName, String email, String phone, double balance
     *                     2- seller:
     *                     * String username, String password, String firstName, String lastName, String email, String phone, String storeName, double balance
     *                     3- admin:
     *                     * String username, String password, String firstName, String lastName, String email, String phone
     */
    public void creatAccount(String type, String username, String password, String firstName, String lastName,
                             String email, String phone, double balance, String storeName) throws Exceptions.ExistedUsernameException, Exceptions.AdminRegisterException {
        if(Account.getAccountByUsername(username) != null)
            throw new Exceptions.ExistedUsernameException(username);
        switch (type) {
            case "customer":
                new Customer(username, password, firstName, lastName, email, phone, balance);
                break;
            case "admin":
                if (Admin.getManager() != null) {
                    throw new Exceptions.AdminRegisterException();
                } else
                    new Admin(username, password, firstName, lastName, email, phone);
                break;
            case "seller":
                new Seller(username, password, firstName, lastName, email, phone, storeName, balance);
                break;
        }
    }

    //Done!! TODO: check this please!
    public void login(String username, String password) throws Exceptions.WrongPasswordException, Exceptions.NotExistedUsernameException {
        Account account = Account.getAccountByUsername(username);
        if (account == null)
            throw new Exceptions.NotExistedUsernameException(username);
        if (!account.getPassword().equals(password))
            throw new Exceptions.WrongPasswordException();
        currentAccount = account;
        if(currentAccount.getType().equalsIgnoreCase("customer")){
            ((Customer) currentAccount).mergeShoppingCart(currentCart.getId());
            currentCart = ((Customer) currentAccount).getShoppingCart();
         }
    }

    //Done!!
    public void logout(){
        currentAccount = null;
        currentCart = new ShoppingCart(null);
    }

    //Done!!

    /**
     * @return returns the currentAccount type: anonymous, customer, seller, admin.
     */
    public String getType() {
        if (currentAccount == null)
            return "anonymous";
        return currentAccount.getType();
    }

    //Done!

    /**
     *
     * @return String[5]: {price, rating score, name, category name, view counts}
     */
    public String[] getAvailableSorts(){
        String[] availableSorts = new String[5];
        availableSorts[0] = "price";
        availableSorts[1] = "rating score";
        availableSorts[2] = "name";
        availableSorts[3] = "category name";
        availableSorts[4] = "default: view counts";
        return availableSorts;
    }

    //Done!! check the directions in the test
    private ArrayList<Product> sortProducts(String sortBy, boolean isIncreasing, ArrayList<Product> products) {
        int direction = isIncreasing ? 1 : -1 ;
        if (sortBy.equalsIgnoreCase("price")) {
            products.sort(new Comparator<Product>() {
                @Override
                public int compare(Product o1, Product o2) {
                    return direction * Double.compare(o1.getMinPrice(), o2.getMinPrice());
                }
            });
        } else if (sortBy.equalsIgnoreCase("rating score")) {
            products.sort(new Comparator<Product>() {
                @Override
                public int compare(Product o1, Product o2) {
                    return direction * Double.compare(o1.getAverageRatingScore(), o2.getAverageRatingScore());
                }
            });
        } else if (sortBy.equalsIgnoreCase("name")) {
            products.sort(new Comparator<Product>() {
                @Override
                public int compare(Product o1, Product o2) {
                    return direction * o1.getName().compareTo(o2.getName());
                }
            });
        } else if (sortBy.equalsIgnoreCase("category name")) {
            products.sort(new Comparator<Product>() {
                @Override
                public int compare(Product o1, Product o2) {
                    return direction * o1.getCategory().getName().compareTo(o2.getCategory().getName());
                }
            });
        } else {
            products.sort(new Comparator<Product>() {
                @Override
                public int compare(Product o1, Product o2) {
                    return direction * Integer.compare(o1.getViewCount(), o2.getViewCount());
                }
            });
        }
        return products;
    }

    //Done!!
    //TODO: getAvailableFilters.
    private void filterProducts(boolean available, double minPrice, double maxPrice, String contains, String brand,
                                              String storeName, double minRatingScore, ArrayList<Product> products) {
        if (available)
            products.removeIf(product -> (product.getTotalRemainingCount() == 0));
        if (minPrice != 0)
            products.removeIf(product -> product.getMaxPrice() < minPrice);
        if (maxPrice != 0)
            products.removeIf(product -> product.getMinPrice() > maxPrice);
        if (!contains.equals(""))
            products.removeIf(product -> !(product.getName().toLowerCase().contains(contains.toLowerCase())));
        if (!brand.equals(""))
            products.removeIf(product -> !(product.getBrand().toLowerCase().contains(brand.toLowerCase())));
        if (!storeName.equals("")) {
            products.removeIf(product -> !product.isSoldInStoreWithName(storeName.toLowerCase()));
        }
        products.removeIf(product -> product.getAverageRatingScore() < minRatingScore);
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

    /**
     * for show category stuff.
     *
     * @return returns String[2]: category ID, category name
     */
    public ArrayList<String[]> viewCategories() {
        try {
            return getSubCategoriesOfThisCategory(Category.getSuperCategory().getName());
        } catch (Exceptions.InvalidCategoryException e) {
            return null;
        }
    }

    //Done!!

    /**
     * for show category action without all.
     *
     * @param categoryName
     * @return
     * @throws Exceptions.InvalidCategoryException
     */
    public ArrayList<String[]> getSubCategoriesOfThisCategory(String categoryName) throws Exceptions.InvalidCategoryException {
        Category category = Category.getCategoryByName(categoryName);
        if (category == null)
            throw new Exceptions.InvalidCategoryException(categoryName);
        else {
            ArrayList<String[]> categoryIdNames = new ArrayList<>();
            for (Category subCategory : category.getSubCategories()) {
                String[] categoryPack = new String[2];
                categoryPack[0] = subCategory.getId();
                categoryPack[1] = subCategory.getName();
                categoryIdNames.add(categoryPack);
            }
            return categoryIdNames;
        }
    }

    //Done!!

    /**
     * @param categoryName
     * @return String[2]: ID, name
     * @throws Exceptions.InvalidCategoryException
     */
    //Todo: child ham befrest
    public ArrayList<String[]> getProductsOfThisCategory(String categoryName) throws Exceptions.InvalidCategoryException {
        Category category = Category.getCategoryByName(categoryName);
        if (category == null)
            throw new Exceptions.InvalidCategoryException(categoryName);
        else
            return productToIdName(category.getProducts());
    }

    //Done!!

    /**
     * @param productIds
     * @param sortBy
     * @param filterBy
     * @return
     * @throws Exceptions.InvalidProductIdException
     * for filtering:
     * available true if only available are to be shown.
     * minPrice if N/A pass 0.00
     * maxPrice if N/A pass 0.00
     * contains if N/A pass null
     * brand if N/A pass null
     * storeName if N/A pass null
     * minRatingScore if N/A pass 0.00
     * products
     */
    public ArrayList<String[]> showProducts(ArrayList<String> productIds, String sortBy, boolean isIncreasing, String[] filterBy) throws Exceptions.InvalidProductIdException {
        ArrayList<Product> products = new ArrayList<>();
        Product product;
        for (String productId : productIds) {
            if ((product = Product.getProductById(productId)) == null)
                throw new Exceptions.InvalidProductIdException(productId);
            else
                products.add(product);
        }

        filterProducts(filterBy[0].equals("true"), Double.parseDouble(filterBy[1]), Double.parseDouble(filterBy[2])
                , filterBy[3], filterBy[4], filterBy[5], Double.parseDouble(filterBy[6]), products);

        products = sortProducts(sortBy,isIncreasing, products);

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

    /**
     * @param productId
     * @return String[5]: ID, name, brand, infoText, averageRatingScore.
     * @throws Exceptions.InvalidProductIdException
     */
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

    //Done!!
    public ArrayList<String> getSpecialPropertiesOfAProduct(String productId) throws Exceptions.InvalidProductIdException {
        Product product = Product.getProductById(productId);
        if(product == null)
            throw new Exceptions.InvalidProductIdException(productId);
        else
            return product.getSpecialProperties();
    }

    //Done!!

    /**
     * @param productId
     * @return String[4]: ID, storeName, price, remaining count.
     * @throws Exceptions.InvalidProductIdException
     */
    public ArrayList<String[]> subProductsOfAProduct(String productId) throws Exceptions.InvalidProductIdException {
        Product product = Product.getProductById(productId);
        if (product == null)
            throw new Exceptions.InvalidProductIdException(productId);
        ArrayList<String[]> subProducts = new ArrayList<>();
        for (SubProduct subProduct : product.getSubProducts()) {
            String[] subProductPack = new String[4];
            subProductPack[0] = subProduct.getId();
            subProductPack[1] = subProduct.getSeller().getStoreName();
            subProductPack[2] = Double.toString(subProduct.getPriceWithSale());
            subProductPack[3] = Integer.toString(subProduct.getRemainingCount());
            subProducts.add(subProductPack);
        }
        return subProducts;
    }

    //Done!!

    /**
     * @param productId
     * @return String[3]: usernameOfReviewer, title, body.
     * @throws Exceptions.InvalidProductIdException
     */
    public ArrayList<String[]> reviewsOfProductWithId(String productId) throws Exceptions.InvalidProductIdException {
        Product product = Product.getProductById(productId);
        if (product == null)
            throw new Exceptions.InvalidProductIdException(productId);
        ArrayList<String[]> reviews = new ArrayList<>();
        for (Review review : product.getReviews()) {
            String[] reviewPack = new String[3];
            reviewPack[0] = review.getReviewer().getUsername();
            reviewPack[1] = review.getTitle();
            reviewPack[2] = review.getText();
            reviews.add(reviewPack);
        }
        return reviews;
    }

    //Done!! TODO: check please
    public void addToCart(String subProductId, int count) throws Exceptions.UnavailableProductException, Exceptions.InvalidSubProductIdException {
        SubProduct subProduct = SubProduct.getSubProductById(subProductId);
        if(subProduct == null)
            throw new Exceptions.InvalidSubProductIdException(subProductId);
        else if(subProduct.getRemainingCount() < count)
            throw new Exceptions.UnavailableProductException(subProductId);
        else {
            currentCart.addSubProductCount(subProductId, count);
            subProduct.changeRemainingCount(-count);
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
    /**
     * @return String[6]: ID, percentage, sellerStoreName, startDate, endDate, numberOfProductsInSale.
     */
    public ArrayList<String[]> sales() {
        ArrayList<String[]> sales = new ArrayList<>();
        for (Sale sale : Sale.getAllSales()) {
            sales.add(getSaleInfo(sale));
        }
        return sales;
    }

    //Done!!
    /**
     * @return
     *                     *1- seller:String[6]
     *                     * { String firstName, String lastName, String phone, String email, String password, String storeName}
     *                     2- customer:
     *                     * { String firstName, String lastName, String phone, String email, String password}
     *                     3- admin:
     *                     * { String firstName, String lastName, String phone, String email, String password, String storeName}
     * @throws Exceptions.NotLoggedInException
     */
    public String[] getPersonalInfoEditableFields() throws Exceptions.NotLoggedInException {
        if(currentAccount == null)
            throw new Exceptions.NotLoggedInException();
        else {
            String[] editableFields = new String[5];
            if(currentAccount.getType().equals("seller")){
                editableFields = new String[6];
                editableFields[5] = "storeName";
            }
            editableFields[0] = "firstName";
            editableFields[1] = "lastName";
            editableFields[2] = "phone";
            editableFields[3] = "email";
            editableFields[4] = "password";
            return editableFields;
        }
    }

    //Done!!
    protected String[] getPersonalInfo(Account account) {
        String[] info;
        if (account.getType().equals("customer")) {
            info = new String[7];
            info[6] = String.valueOf(((Customer) account).getBalance());
        } else if (account.getType().equals("seller")) {
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

    /**
     *
     * @return if
     *      admin:     6: username, type, firstName, lastName, email, phone;
     *      customer:  7: username, type, firstName, lastName, email, phone, balance;
     *      seller:    8: username, type, firstName, lastName, email, phone, balance, storeName;
     */
    public String[] viewPersonalInfo() {
        return getPersonalInfo(currentAccount);
    }

    //Done!!
    protected void editPersonalInfo(String field, String newInformation) throws Exceptions.InvalidFieldException, Exceptions.SameAsPreviousValueException {
        switch (field) {
            case "firstName":
                if(currentAccount.getFirstName().equals(newInformation))
                    throw new Exceptions.SameAsPreviousValueException(field);
                currentAccount.setFirstName(newInformation);
                break;
            case "lastName":
                if(currentAccount.getLastName().equals(newInformation))
                    throw new Exceptions.SameAsPreviousValueException(field);
                currentAccount.setLastName(newInformation);
                break;
            case "email":
                if(currentAccount.getEmail().equals(newInformation))
                    throw new Exceptions.SameAsPreviousValueException(field);
                currentAccount.setEmail(newInformation);
                break;
            case "phone":
                if(currentAccount.getPhone().equals(newInformation))
                    throw new Exceptions.SameAsPreviousValueException(field);
                currentAccount.setPhone(newInformation);
                break;
            case "password":
                if(currentAccount.getPassword().equals(newInformation))
                    throw new Exceptions.SameAsPreviousValueException(field);
                currentAccount.setPassword(newInformation);
                break;
            default:
                throw new Exceptions.InvalidFieldException();
        }
    }

    //Done!!
    protected String[] getSaleInfo(Sale sale) {
        String[] salePack = new String[6];
        salePack[0] = sale.getId();
        salePack[1] = Double.toString(sale.getPercentage());
        salePack[2] = sale.getSeller().getStoreName();
        salePack[3] = dateFormat.format(sale.getStartDate());
        salePack[4] = dateFormat.format(sale.getEndDate());
        salePack[5] = Integer.toString(sale.getSubProducts().size());
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
        for (SubProduct subProduct : sale.getSubProducts()) {
            String[] productPack = new String[2];
            productPack[0] = subProduct.getProduct().getId();
            productPack[1] = subProduct.getProduct().getName();

            productsInSale.add(productPack);
        }
        return productsInSale;
    }

    //Todo
    public ArrayList<String[]> showInSaleProducts(String sortBy, boolean isIncreasing, String[] filterBy){
        return null;
    }
}
