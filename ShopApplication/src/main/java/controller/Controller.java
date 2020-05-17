package controller;


import model.*;
import model.account.Account;
import model.account.Admin;
import model.account.Customer;
import model.account.Seller;
import model.database.Database;

import java.text.DateFormat;

import java.util.ArrayList;
import java.util.Map;

//TODO: compare to products!
public class Controller {
    private Account currentAccount;
    private Cart currentCart;
    protected static final DateFormat dateFormat = Utilities.getDateFormat();
    protected Database databaseManager;


    //Done
    public Controller(Database DataBaseManager) {
        databaseManager = DataBaseManager;
        currentCart = new Cart(null);
        currentAccount = null;
    }

    Database getDatabaseManager(){
        return databaseManager;
    }

    Account getCurrentAccount() {
        return currentAccount;
    }

    Cart getCurrentCart() {
        return currentCart;
    }

    //Done!

    /**
     * @param username
     * @param type
     * @throws Exceptions.UsernameAlreadyTakenException
     * @throws Exceptions.AdminRegisterException
     */
    public void usernameTypeValidation(String username, String type) throws Exceptions.UsernameAlreadyTakenException, Exceptions.AdminRegisterException {
        if (Account.getAccountByUsername(username) != null)
            throw new Exceptions.UsernameAlreadyTakenException(username);
        else if (type.equalsIgnoreCase("Admin") && (Admin.getManager() != null))
            throw new Exceptions.AdminRegisterException();
    }

    //Done!!

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
                databaseManager.createCustomer();
                break;
            case "Admin":
                new Admin(username, password, firstName, lastName, email, phone);
                databaseManager.createAdmin();
                break;
            case "Seller":
                new Seller(username, password, firstName, lastName, email, phone, storeName, balance);
                databaseManager.createSeller();
                break;
        }
    }

    //Done!!
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
            databaseManager.cart();
        }
    }

    //Done!!
    public void logout() {
        currentAccount = null;
        currentCart = new Cart(null);
    }

    //Done!!

    /**
     * @return returns the currentAccount type: anonymous, customer, seller, admin.
     */
    public String getType() {
        if (currentAccount == null)
            return "Anonymous";
        return currentAccount.getClass().getSimpleName();
    }

    //Done!

    /**
     * @return String[5]: {price, rating score, name, category name, view counts}
     */
    public String[] getProductAvailableSorts() {
        return Utilities.Sort.productAvailableSorts();
    }

    //Done!! check the directions in the test
    private void sortProducts(String sortBy, boolean isIncreasing, ArrayList<Product> products) {
        switch (sortBy) {
            case "price":
                products.sort( new Utilities.Sort.ProductPriceComparator(isIncreasing));
                break;
            case "rating score":
                products.sort( new Utilities.Sort.ProductRatingScoreComparator(isIncreasing));
                break;
            case "name":
                products.sort( new Utilities.Sort.ProductNameComparator(isIncreasing));
                break;
            case "category name":
                products.sort( new Utilities.Sort.ProductCategoryNameComparator(isIncreasing));
                break;
            case "remaining count":
                products.sort( new Utilities.Sort.ProductRemainingCountComparator(isIncreasing));
                break;
            case "view count":
                products.sort( new Utilities.Sort.ProductViewCountComparator(isIncreasing));
            default:
                products.sort( new Utilities.Sort.ProductViewCountComparator(true));
                break;
        }
    }

    //Done!!
    public String[] getProductAvailableFilters() {
        return Utilities.Filter.productAvailableFilters();
    }

    //Done!!
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
    private ArrayList<String[]> productToIdNameBrand(ArrayList<Product> products) {
        ArrayList<String[]> productIdNames = new ArrayList<>();
        for (Product product : products) {
            productIdNames.add(Utilities.Pack.product(product));
        }
        return productIdNames;
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
        Category category;
        if (categoryName.equals("superCategory"))
            category = Category.getCategoryByName(categoryName);
        else
            category = Category.getSuperCategory();
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

    //Done!!

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

    //Done!!
    private ArrayList<Product> getProductsInCategory(Category category) {
        ArrayList<Product> products = new ArrayList<>(category.getProducts());
        sortProducts("view count", true, products);
        return products;
    }

    //Done!!

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
    public ArrayList<String[]> showProducts(ArrayList<String> productIds, String sortBy, boolean isIncreasing, String[] filterBy) throws Exceptions.InvalidProductIdException {
        if (sortBy == null)
            sortBy = "viewCount";
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

        sortProducts(sortBy, isIncreasing, products);

        return productToIdNameBrand(products);
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
        return Utilities.Pack.digest(product);
    }

    //Done!!
    public ArrayList<String> getSpecialPropertiesOfAProduct(String productId) throws Exceptions.InvalidProductIdException {
        Product product = Product.getProductById(productId);
        if (product == null)
            throw new Exceptions.InvalidProductIdException(productId);
        else
            return new ArrayList<>(product.getSpecialProperties());
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
            subProducts.add(Utilities.Pack.subProduct(subProduct));
        }
        return subProducts;
    }

    //Done!!
    public String[] getSubProductByID(String subProductId) throws Exceptions.InvalidSubProductIdException {
        SubProduct subProduct = SubProduct.getSubProductById(subProductId);
        if (subProduct == null)
            throw new Exceptions.InvalidSubProductIdException(subProductId);
        else
            return Utilities.Pack.subProduct(subProduct);
    }

    //Done!!

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

    //Done!!
    public void addToCart(String subProductId, int count) throws Exceptions.UnavailableProductException, Exceptions.InvalidSubProductIdException {
        SubProduct subProduct = SubProduct.getSubProductById(subProductId);
        if (subProduct == null)
            throw new Exceptions.InvalidSubProductIdException(subProductId);
        else if (subProduct.getRemainingCount() < count + currentCart.getCountOfaSubProduct(subProductId))
            throw new Exceptions.UnavailableProductException(subProductId);
        else {
            currentCart.addSubProductCount(subProductId, count);
            databaseManager.cart();
        }
    }

    //Done!!
    public ArrayList<String[]> getProductsInCart() {
        ArrayList<String[]> shoppingCart = new ArrayList<>();
        Map<SubProduct, Integer> subProducts = ((Customer) currentAccount).getCart().getSubProducts();
        for (SubProduct subProduct : subProducts.keySet()) {
            shoppingCart.add(Utilities.Pack.productInCart(subProduct, subProducts.get(subProduct)));
        }
        return shoppingCart;
    }

    //Done!!
    public void viewProductInCart(String subProductId) throws Exceptions.InvalidSubProductIdException {
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

    //Done!!
    public void increaseProductInCart(String subProductId, int number) throws Exceptions.NotSubProductIdInTheCartException,
            Exceptions.UnavailableProductException, Exceptions.InvalidSubProductIdException {
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
            databaseManager.cart();
        }
    }

    //Done!!
    public void decreaseProductInCart(String subProductId, int number) throws Exceptions.InvalidSubProductIdException,
            Exceptions.NotSubProductIdInTheCartException {
        SubProduct subProduct = SubProduct.getSubProductById(subProductId);
        if (subProduct == null)
            throw new Exceptions.InvalidSubProductIdException(subProductId);
        else {
            Map<SubProduct, Integer> subProductsInCart = currentCart.getSubProducts();
            if (subProductsInCart.containsKey(subProduct)) {
                currentCart.addSubProductCount(subProductId, -number);
                databaseManager.cart();
            } else
                throw new Exceptions.NotSubProductIdInTheCartException(subProductId);
        }
    }

    //Done!!
    public double getTotalPriceOfCart() {
        return currentCart.getTotalPrice();
    }


    //Done!!
    public void addReview(String productId, String title, String text) throws Exceptions.InvalidProductIdException, Exceptions.NotLoggedInException {
        if (currentAccount == null)
            throw new Exceptions.NotLoggedInException();
        else {
            if (Product.getProductById(productId) == null)
                throw new Exceptions.InvalidProductIdException(productId);
            else {
                new Review(currentAccount.getId(), productId, title, text);
                databaseManager.request();
            }
        }
    }

    //Done!!

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

    //Done!!

    /**
     * @return if
     * admin:     6: username, type, firstName, lastName, email, phone;
     * customer:  7: username, type, firstName, lastName, email, phone, balance;
     * seller:    8: username, type, firstName, lastName, email, phone, balance, storeName;
     */
    public String[] viewPersonalInfo() {
        return Utilities.Pack.personalInfo(currentAccount);
    }

    //Done!!
    protected void editPersonalInfo(String field, String newInformation) throws Exceptions.InvalidFieldException, Exceptions.SameAsPreviousValueException {
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

    //Done!!
    public ArrayList<String[]> getProductsInSale(String saleId) throws Exceptions.InvalidSaleIdException {
        Sale sale = Sale.getSaleById(saleId);
        if (sale == null)
            throw new Exceptions.InvalidSaleIdException(saleId);
        else
            return getProductsInSale(sale);
    }

    //Done!!
    protected ArrayList<String[]> getProductsInSale(Sale sale) {
        ArrayList<String[]> productsInSale = new ArrayList<>();
        ArrayList<SubProduct> subProducts = new ArrayList<>(sale.getSubProducts());
        sortSubProducts("view count", false, subProducts);
        for (SubProduct subProduct : subProducts) {
            productsInSale.add(Utilities.Pack.productInSale(subProduct));
        }
        return productsInSale;
    }

    //Done!!
    public ArrayList<String[]> showInSaleProducts(String sortBy, boolean isIncreasing, String[] filterBy) {
        ArrayList<String[]> subProductsSalePacks = new ArrayList<>();
        ArrayList<SubProduct> subProductsInSale = new ArrayList<>();
        for (Sale sale : Sale.getAllSales()) {
            subProductsInSale.addAll(sale.getSubProducts());
        }
        filterSubProducts(filterBy[0].equals("true"), Double.parseDouble(filterBy[1]), Double.parseDouble(filterBy[2])
                , filterBy[3], filterBy[4], filterBy[5], Double.parseDouble(filterBy[6]), subProductsInSale);
        sortSubProducts(sortBy, isIncreasing, subProductsInSale);
        for (SubProduct subProduct : subProductsInSale) {
            subProductsSalePacks.add(Utilities.Pack.productInSale(subProduct));
        }
        return subProductsSalePacks;
    }

    private void filterSubProducts(boolean available, double minPrice, double maxPrice, String contains, String brand,
                                   String storeName, double minRatingScore, ArrayList<SubProduct> subProducts) {
        if (available)
            subProducts.removeIf(subProduct -> (subProduct.getRemainingCount() == 0));
        if (minPrice != 0)
            subProducts.removeIf(subProduct -> subProduct.getPriceWithSale() < minPrice);
        if (maxPrice != 0)
            subProducts.removeIf(subProduct -> subProduct.getPriceWithSale() > maxPrice);
        if (!contains.equals(""))
            subProducts.removeIf(subProduct -> !(subProduct.getProduct().getName().toLowerCase().contains(contains.toLowerCase())));
        if (!brand.equals(""))
            subProducts.removeIf(subProduct -> !(subProduct.getProduct().getBrand().toLowerCase().contains(brand.toLowerCase())));
        if (!storeName.equals("")) {
            subProducts.removeIf(subProduct -> !subProduct.getSeller().getStoreName().contains(storeName.toLowerCase()));
        }
        subProducts.removeIf(subProduct -> subProduct.getProduct().getAverageRatingScore() < minRatingScore);
    }

    private void sortSubProducts(String sortBy, boolean isIncreasing, ArrayList<SubProduct> subProducts) {
        switch (sortBy) {
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
            case "view count":
                subProducts.sort(new Utilities.Sort.SubProductViewCountComparator(isIncreasing));
                break;
            default:
                subProducts.sort(new Utilities.Sort.SubProductViewCountComparator(true));
                break;
        }
    }
}
