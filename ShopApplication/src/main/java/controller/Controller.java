package controller;


import model.*;
import model.account.Account;
import model.account.Admin;
import model.account.Customer;
import model.account.Seller;
import model.database.Database;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

//TODO: compare to products!
public class Controller {
    private Account currentAccount;
    private Cart currentCart;
    private Database database;


    public Controller(Database DataBaseManager) {
        database = DataBaseManager;
        currentCart = new Cart(null);
        currentAccount = null;
    }

    Database getDatabase() {
        return database;
    }

    Account getCurrentAccount() {
        return currentAccount;
    }

    Cart getCurrentCart() {
        return currentCart;
    }

    /**
     * @param username
     * @param type
     * @throws Exceptions.UsernameAlreadyTakenException
     * @throws Exceptions.AdminRegisterException
     */
    public void usernameTypeValidation(String username, String type) throws Exceptions.UsernameAlreadyTakenException, Exceptions.AdminRegisterException {
        if (Account.isUsernameUsed(username))
            throw new Exceptions.UsernameAlreadyTakenException(username);
        else if (type.equalsIgnoreCase("Admin") && (Admin.getManager() != null))
            throw new Exceptions.AdminRegisterException();
    }

    /**
     * @param type information: 1- customer:
     *             * String username, String password, String firstName, String lastName, String email, String phone, double balance
     *             2- seller:
     *             * String username, String password, String firstName, String lastName, String email, String phone, String storeName, double balance
     *             3- admin:
     *             * String username, String password, String firstName, String lastName, String email, String phone
     */
    public void creatAccount(String type, String username, String password, String firstName, String lastName,
                             String email, String phone, double balance, String storeName) throws Exceptions.UsernameAlreadyTakenException, Exceptions.AdminRegisterException {
        usernameTypeValidation(username, type);
        switch (type) {
            case "Customer":
                new Customer(username, password, firstName, lastName, email, phone, balance);
                database.createCustomer();
                break;
            case "Admin":
                new Admin(username, password, firstName, lastName, email, phone);
                database.createAdmin();
                break;
            case "Seller":
                new Seller(username, password, firstName, lastName, email, phone, storeName, balance);
                database.request();
                break;
        }
    }

    public void login(String username, String password) throws Exceptions.WrongPasswordException, Exceptions.UsernameDoesntExistException {
        Account account = Account.getAccountByUsername(username);
        if (account == null)
            throw new Exceptions.UsernameDoesntExistException(username);
        if (!account.getPassword().equals(password))
            throw new Exceptions.WrongPasswordException();
        currentAccount = account;
        if (currentAccount instanceof Customer) {
            ((Customer) currentAccount).mergeCart(currentCart.getId());
            currentCart = ((Customer) currentAccount).getCart();
            database.cart();
        }
    }

    public void logout() throws Exceptions.NotLoggedInException {
        if( currentAccount == null)
            throw new Exceptions.NotLoggedInException();
        currentAccount = null;
        currentCart = new Cart(null);
    }

    /**
     * @return returns the currentAccount type: anonymous, customer, seller, admin.
     */
    public String getType() {
        if (currentAccount == null)
            return "Anonymous";
        return currentAccount.getClass().getSimpleName();
    }

    /**
     * @return String[5]: {price, rating score, name, category name, view counts}
     */
    public String[] getProductAvailableSorts() {
        return Utilities.Sort.productAvailableSorts();
    }

    private void sortProducts(String sortBy, boolean isIncreasing, ArrayList<Product> products) {
        if (sortBy == null)
            sortBy = "view count";

        switch (sortBy) {
            case "view count":
                products.sort(new Utilities.Sort.ProductViewCountComparator(isIncreasing));
                break;
            case "price":
                products.sort(new Utilities.Sort.ProductPriceComparator(isIncreasing));
                break;
            case "rating score":
                products.sort(new Utilities.Sort.ProductRatingScoreComparator(isIncreasing));
                break;
            case "name":
                products.sort(new Utilities.Sort.ProductNameComparator(isIncreasing));
                break;
            case "category name":
                products.sort(new Utilities.Sort.ProductCategoryNameComparator(isIncreasing));
                break;
            case "remaining count":
                products.sort(new Utilities.Sort.ProductRemainingCountComparator(isIncreasing));
                break;
            default:
                products.sort(new Utilities.Sort.ProductViewCountComparator(true));
        }
    }

    public String[] getProductAvailableFilters() {
        return Utilities.Filter.productAvailableFilters();
    }

    private void filterProducts(boolean available, double minPrice, double maxPrice, String contains, String brand,
                                String storeName, double minRatingScore, ArrayList<Product> products) {
        Utilities.Filter.ProductFilter.available(products, available);
        Utilities.Filter.ProductFilter.minPrice(products, minPrice);
        Utilities.Filter.ProductFilter.maxPrice(products, maxPrice);
        Utilities.Filter.ProductFilter.name(products, contains);
        Utilities.Filter.ProductFilter.brand(products, brand);
        Utilities.Filter.ProductFilter.storeName(products, storeName);
        Utilities.Filter.ProductFilter.ratingScore(products, minRatingScore);
    }

    private ArrayList<String[]> productToIdNameBrand(ArrayList<Product> products) {
        ArrayList<String[]> productIdNames = new ArrayList<>();
        for (Product product : products) {
            productIdNames.add(Utilities.Pack.product(product));
        }
        return productIdNames;
    }

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
                categoryIdNames.add(Utilities.Pack.category(subCategory));
            }
            return categoryIdNames;
        }
    }

    /**
     * @param categoryName
     * @return String[2]: ID, name
     * @throws Exceptions.InvalidCategoryException
     */
    public ArrayList<String[]> getProductsOfThisCategory(String categoryName) throws Exceptions.InvalidCategoryException {
        Category category = Category.getCategoryByName(categoryName);
        if (category == null)
            throw new Exceptions.InvalidCategoryException(categoryName);
        else {
            return productToIdNameBrand(getProductsInCategory(category));
        }
    }

    private ArrayList<Product> getProductsInCategory(Category category) {
        ArrayList<Product> products = new ArrayList<>(category.getProducts(true));
        sortProducts("view count", true, products);
        return products;
    }

    /**
     * @param productIds
     * @param sortBy
     * @param filterBy
     * @return
     * @throws Exceptions.InvalidProductIdException for filtering:
     *                                              available true if only available are to be shown.
     *                                              minPrice if N/A pass 0.00
     *                                              maxPrice if N/A pass 0.00
     *                                              contains if N/A pass null
     *                                              brand if N/A pass null
     *                                              storeName if N/A pass null
     *                                              minRatingScore if N/A pass 0.00
     *                                              products
     */
    public ArrayList<String[]> showProducts(ArrayList<String> productIds, String sortBy, boolean isIncreasing, String[] filterBy, HashMap<String, String> propertyFilters) throws Exceptions.InvalidProductIdException {
        ArrayList<Product> products = new ArrayList<>();
        Product product;

        for (String productId : productIds) {
            product = Product.getProductById(productId);
            if (product == null)
                throw new Exceptions.InvalidProductIdException(productId);
            else
                products.add(product);
        }
        for (int i = 0; i < filterBy.length; i++) {
            if (filterBy[i] == null)
                filterBy[i] = "";
        }
        filterProducts(filterBy[0].equalsIgnoreCase("true"), Double.parseDouble(filterBy[1]), Double.parseDouble(filterBy[2])
                , filterBy[3], filterBy[4], filterBy[5], Double.parseDouble(filterBy[6]), products);

        for (String propertyFilter : propertyFilters.keySet()) {
            if(propertyFilters.get(propertyFilter) != null)
                Utilities.Filter.ProductFilter.property(products, propertyFilter, propertyFilters.get(propertyFilter));
        }
        sortProducts(sortBy, isIncreasing, products);

        return productToIdNameBrand(products);
    }

    public ArrayList<String> getAvailableValuesOfAPropertyOfACategory(String categoryName, String property) throws Exceptions.InvalidCategoryException {
        return Utilities.Filter.getAvailableValuesOfAPropertyOfACategory(categoryName, property);
    }

    public void showProduct(String productId) throws Exceptions.InvalidProductIdException {
        Product product = Product.getProductById(productId);
        if (product == null)
            throw new Exceptions.InvalidProductIdException(productId);
        else {
            product.increaseViewCount();
        }
    }

    /**
     * @param productId
     * @return String[5]: ID, name, brand, infoText, averageRatingScore.
     * @throws Exceptions.InvalidProductIdException
     */
    public String[] digest(String productId) throws Exceptions.InvalidProductIdException {
        Product product = Product.getProductById(productId);
        if (product == null)
            throw new Exceptions.InvalidProductIdException(productId);
        return Utilities.Pack.digest(product);
    }

    public ArrayList<String> getPropertyValuesOfAProduct(String productId) throws Exceptions.InvalidProductIdException {
        Product product = Product.getProductById(productId);
        if (product == null)
            throw new Exceptions.InvalidProductIdException(productId);
        else {
            ArrayList<String> properties = new ArrayList<>();
            for (String property : product.getCategory().getProperties()) {
                properties.add(product.getPropertyValue(property));
            }
            return properties;
        }
    }

    public ArrayList<String> getPropertiesOfCategory(String categoryName) throws Exceptions.InvalidCategoryException{
        Category category = Category.getCategoryByName(categoryName);
        if( category == null)
            throw new Exceptions.InvalidCategoryException(categoryName);
        else
            return new ArrayList<>(category.getProperties());
    }

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
            subProducts.add(Utilities.Pack.subProduct(subProduct));
        }
        return subProducts;
    }

    public String[] getSubProductByID(String subProductId) throws Exceptions.InvalidSubProductIdException {
        SubProduct subProduct = SubProduct.getSubProductById(subProductId);
        if (subProduct == null)
            throw new Exceptions.InvalidSubProductIdException(subProductId);
        else
            return Utilities.Pack.subProduct(subProduct);
    }

    /**
     * @param productId
     * @return String[3]: usernameOfReviewer, title, body, hasBought.
     * @throws Exceptions.InvalidProductIdException
     */
    public ArrayList<String[]> reviewsOfProductWithId(String productId) throws Exceptions.InvalidProductIdException {
        Product product = Product.getProductById(productId);
        if (product == null)
            throw new Exceptions.InvalidProductIdException(productId);
        ArrayList<String[]> reviews = new ArrayList<>();
        for (Review review : product.getReviews()) {
            reviews.add(Utilities.Pack.review(review));
        }
        return reviews;
    }

    public void checkAuthorityOverCart() throws Exceptions.UnAuthorizedAccountException {
        if ((currentAccount != null) && !(currentAccount instanceof Customer))
            throw new Exceptions.UnAuthorizedAccountException();
    }

    public void addToCart(String subProductId, int count) throws Exceptions.UnavailableProductException, Exceptions.InvalidSubProductIdException, Exceptions.UnAuthorizedAccountException {
        checkAuthorityOverCart();
        SubProduct subProduct = SubProduct.getSubProductById(subProductId);
        if (subProduct == null)
            throw new Exceptions.InvalidSubProductIdException(subProductId);
        else if (subProduct.getRemainingCount() < count + currentCart.getCountOfaSubProduct(subProductId))
            throw new Exceptions.UnavailableProductException(subProductId);
        else {
            currentCart.addSubProductCount(subProductId, count);
            database.cart();
        }
    }

    public ArrayList<String[]> getProductsInCart() throws Exceptions.UnAuthorizedAccountException {
        checkAuthorityOverCart();
        ArrayList<String[]> shoppingCart = new ArrayList<>();
        Map<SubProduct, Integer> subProducts = ((Customer) currentAccount).getCart().getSubProducts();
        for (SubProduct subProduct : subProducts.keySet()) {
            shoppingCart.add(Utilities.Pack.productInCart(subProduct, subProducts.get(subProduct)));
        }
        return shoppingCart;
    }

    public void viewProductInCart(String subProductId) throws Exceptions.InvalidSubProductIdException, Exceptions.UnAuthorizedAccountException {
        checkAuthorityOverCart();
        SubProduct subProduct = SubProduct.getSubProductById(subProductId);
        if (!currentCart.getSubProducts().containsKey(subProduct))
            throw new Exceptions.InvalidSubProductIdException(subProductId);
        else {
            try {
                showProduct(subProduct.getProduct().getId());
            } catch (Exceptions.InvalidProductIdException ignored) {
            }
        }
    }

    public void increaseProductInCart(String subProductId, int number) throws Exceptions.NotSubProductIdInTheCartException,
            Exceptions.UnavailableProductException, Exceptions.InvalidSubProductIdException, Exceptions.UnAuthorizedAccountException {
        checkAuthorityOverCart();
        Map<SubProduct, Integer> subProducts = currentCart.getSubProducts();
        SubProduct subProduct = SubProduct.getSubProductById(subProductId);
        if (subProduct == null)
            throw new Exceptions.InvalidSubProductIdException(subProductId);
        else if (!subProducts.containsKey(subProduct))
            throw new Exceptions.NotSubProductIdInTheCartException(subProductId);
        else if (number + subProducts.get(subProduct) > subProduct.getRemainingCount())
            throw new Exceptions.UnavailableProductException(subProductId);
        else {
            currentCart.addSubProductCount(subProductId, number);
            database.cart();
        }
    }

    public void decreaseProductInCart(String subProductId, int number) throws Exceptions.InvalidSubProductIdException,
            Exceptions.NotSubProductIdInTheCartException, Exceptions.UnAuthorizedAccountException {
        checkAuthorityOverCart();
        SubProduct subProduct = SubProduct.getSubProductById(subProductId);
        if (subProduct == null)
            throw new Exceptions.InvalidSubProductIdException(subProductId);
        else {
            Map<SubProduct, Integer> subProductsInCart = currentCart.getSubProducts();
            if (subProductsInCart.containsKey(subProduct)) {
                currentCart.addSubProductCount(subProductId, -number);
                database.cart();
            } else
                throw new Exceptions.NotSubProductIdInTheCartException(subProductId);
        }
    }

    public double getTotalPriceOfCart() throws Exceptions.UnAuthorizedAccountException {
        checkAuthorityOverCart();
        return currentCart.getTotalPrice();
    }

    public void addReview(String productId, String title, String text) throws Exceptions.InvalidProductIdException, Exceptions.NotLoggedInException {
        if (currentAccount == null)
            throw new Exceptions.NotLoggedInException();
        else {
            if (Product.getProductById(productId) == null)
                throw new Exceptions.InvalidProductIdException(productId);
            else {
                new Review(currentAccount.getId(), productId, title, text);
                database.request();
            }
        }
    }

    /**
     * @return String[6]: ID, percentage, sellerStoreName, startDate, endDate, numberOfProductsInSale.
     */
    public ArrayList<String[]> sales() {
        ArrayList<String[]> sales = new ArrayList<>();
        for (Sale sale : Sale.getAllSales()) {
            sales.add(Utilities.Pack.saleInfo(sale));
        }
        return sales;
    }

    /**
     * @return if
     * admin:     6: username, type, firstName, lastName, email, phone;
     * customer:  7: username, type, firstName, lastName, email, phone, balance;
     * seller:    8: username, type, firstName, lastName, email, phone, balance, storeName;
     */
    public String[] viewPersonalInfo() {
        return Utilities.Pack.personalInfo(currentAccount);
    }

    void editPersonalInfo(String field, String newInformation) throws Exceptions.InvalidFieldException, Exceptions.SameAsPreviousValueException {
        switch (field) {
            case "firstName":
                if (currentAccount.getFirstName().equals(newInformation))
                    throw new Exceptions.SameAsPreviousValueException(field);
                currentAccount.setFirstName(newInformation);
                break;
            case "lastName":
                if (currentAccount.getLastName().equals(newInformation))
                    throw new Exceptions.SameAsPreviousValueException(field);
                currentAccount.setLastName(newInformation);
                break;
            case "email":
                if (currentAccount.getEmail().equals(newInformation))
                    throw new Exceptions.SameAsPreviousValueException(field);
                currentAccount.setEmail(newInformation);
                break;
            case "phone":
                if (currentAccount.getPhone().equals(newInformation))
                    throw new Exceptions.SameAsPreviousValueException(field);
                currentAccount.setPhone(newInformation);
                break;
            case "password":
                if (currentAccount.getPassword().equals(newInformation))
                    throw new Exceptions.SameAsPreviousValueException(field);
                currentAccount.setPassword(newInformation);
                break;
            default:
                throw new Exceptions.InvalidFieldException();
        }
    }

    public ArrayList<String[]> getProductsInSale(String saleId) throws Exceptions.InvalidSaleIdException {
        Sale sale = Sale.getSaleById(saleId);
        if (sale == null)
            throw new Exceptions.InvalidSaleIdException(saleId);
        else
            return getProductsInSale(sale);
    }

    private ArrayList<String[]> getProductsInSale(Sale sale) {
        ArrayList<String[]> productsInSale = new ArrayList<>();
        ArrayList<SubProduct> subProducts = new ArrayList<>(sale.getSubProducts());
        sortSubProducts("view count", false, subProducts);
        for (SubProduct subProduct : subProducts) {
            productsInSale.add(Utilities.Pack.productInSale(subProduct));
        }
        return productsInSale;
    }

    public ArrayList<String[]> showInSaleProducts(String sortBy, boolean isIncreasing, String[] filterBy) {
        ArrayList<String[]> subProductsSalePacks = new ArrayList<>();
        ArrayList<SubProduct> subProductsInSale = new ArrayList<>();
        for (Sale sale : Sale.getAllSales()) {
            subProductsInSale.addAll(sale.getSubProducts());
        }
        for( int i = 0; i < filterBy.length; i++){
            if( filterBy[i] == null)
                filterBy[i] = "";
        }
        filterSubProducts(filterBy[0].equalsIgnoreCase("true"), Double.parseDouble(filterBy[1]), Double.parseDouble(filterBy[2])
                , filterBy[3], filterBy[4], filterBy[5], Double.parseDouble(filterBy[6]), subProductsInSale);
        sortSubProducts(sortBy, isIncreasing, subProductsInSale);
        for (SubProduct subProduct : subProductsInSale) {
            subProductsSalePacks.add(Utilities.Pack.productInSale(subProduct));
        }
        return subProductsSalePacks;
    }

    private void filterSubProducts(boolean available, double minPrice, double maxPrice, String contains, String brand,
                                   String storeName, double minRatingScore, ArrayList<SubProduct> subProducts) {
        Utilities.Filter.SubProductFilter.available(subProducts, available);
        Utilities.Filter.SubProductFilter.minPrice(subProducts, minPrice);
        Utilities.Filter.SubProductFilter.maxPrice(subProducts, maxPrice);
        Utilities.Filter.SubProductFilter.name(subProducts, contains);
        Utilities.Filter.SubProductFilter.brand(subProducts, brand);
        Utilities.Filter.SubProductFilter.storeName(subProducts, storeName);
        Utilities.Filter.SubProductFilter.ratingScore(subProducts, minRatingScore);
    }

    private void sortSubProducts(String sortBy, boolean isIncreasing, ArrayList<SubProduct> subProducts) {
        if (sortBy == null) {
            sortBy = "view count";
        }
        switch (sortBy) {
            case "view count":
                subProducts.sort(new Utilities.Sort.SubProductViewCountComparator(isIncreasing));
                break;
            case "price":
                subProducts.sort(new Utilities.Sort.SubProductPriceComparator(isIncreasing));
                break;
            case "name":
                subProducts.sort(new Utilities.Sort.SubProductNameComparator(isIncreasing));
                break;
            case "rating score":
                subProducts.sort(new Utilities.Sort.SubProductRatingScoreComparator(isIncreasing));
                break;
            case "category name":
                subProducts.sort(new Utilities.Sort.SubProductCategoryNameComparator(isIncreasing));
                break;
            case "remaining count":
                subProducts.sort(new Utilities.Sort.SubProductRemainingCountComparator(isIncreasing));
                break;
            default:
                subProducts.sort(new Utilities.Sort.SubProductViewCountComparator(true));
        }
    }
}
