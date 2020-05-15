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
    protected static Cart currentCart;
    protected static DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");

    //Done!

    /**
     * @param username
     * @param type
     * @throws Exceptions.ExistedUsernameException
     * @throws Exceptions.AdminRegisterException
     */
    //TODO: Account.getAccountByUsername(username) != null. , Admin.getManager() != null.
    public void usernameTypeValidation(String username, String type) throws Exceptions.ExistedUsernameException, Exceptions.AdminRegisterException {
        if (Account.getAccountByUsername(username) == null)
            throw new Exceptions.ExistedUsernameException(username);
        else if (type.equalsIgnoreCase("Admin") && (Admin.getManager() == null))
            throw new Exceptions.AdminRegisterException();
    }

    //Done!! Todo: Shayan please check this

    /**
     * @param type information: 1- customer:
     *             * String username, String password, String firstName, String lastName, String email, String phone, double balance
     *             2- seller:
     *             * String username, String password, String firstName, String lastName, String email, String phone, String storeName, double balance
     *             3- admin:
     *             * String username, String password, String firstName, String lastName, String email, String phone
     */
    public void creatAccount(String type, String username, String password, String firstName, String lastName,
                             String email, String phone, double balance, String storeName) throws Exceptions.ExistedUsernameException, Exceptions.AdminRegisterException {
        if (Account.getAccountByUsername(username) != null)
            throw new Exceptions.ExistedUsernameException(username);
        switch (type) {
            case "Customer":
                new Customer(username, password, firstName, lastName, email, phone, balance);
                break;
            case "Admin":
                if (Admin.getManager() != null) {
                    throw new Exceptions.AdminRegisterException();
                } else
                    new Admin(username, password, firstName, lastName, email, phone);
                break;
            case "Seller":
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
        if (currentAccount instanceof Customer) {
            ((Customer) currentAccount).mergeCart(currentCart.getId());
            currentCart = ((Customer) currentAccount).getCart();
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
    //TODO: shouldn't it be: getClass().getSimpleName()? check please.
    public String getType() {
        if (currentAccount == null)
            return "Anonymous";
        return currentAccount.getClass().getName();
    }

    //Done!

    /**
     * @return String[5]: {price, rating score, name, category name, view counts}
     */
    public String[] getAvailableSorts() {
        String[] availableSorts = new String[5];
        availableSorts[0] = "price";
        availableSorts[1] = "rating score";
        availableSorts[2] = "name";
        availableSorts[3] = "category name";
        availableSorts[4] = "default: view counts";
        return availableSorts;
    }

    //Done!! check the directions in the test
    //TODO: not sure if you can use switch case here!
    private ArrayList<Product> sortProducts(String sortBy, boolean isIncreasing, ArrayList<Product> products) {
        int direction = isIncreasing ? 1 : -1;
        switch (sortBy) {
            case "price":
                products.sort(new Comparator<Product>() {
                    @Override
                    public int compare(Product o1, Product o2) {
                        return direction * Double.compare(o1.getMinPrice(), o2.getMinPrice());
                    }
                });
                break;
            case "rating score":
                products.sort(new Comparator<Product>() {
                    @Override
                    public int compare(Product o1, Product o2) {
                        return direction * Double.compare(o1.getAverageRatingScore(), o2.getAverageRatingScore());
                    }
                });
                break;
            case "name":
                products.sort(new Comparator<Product>() {
                    @Override
                    public int compare(Product o1, Product o2) {
                        return direction * o1.getName().compareTo(o2.getName());
                    }
                });
                break;
            case "category name":
                products.sort(new Comparator<Product>() {
                    @Override
                    public int compare(Product o1, Product o2) {
                        return direction * o1.getCategory().getName().compareTo(o2.getCategory().getName());
                    }
                });
                break;
            case "remaining count":
                products.sort(new Comparator<Product>() {
                    @Override
                    public int compare(Product o1, Product o2) {
                        return direction * Integer.compare(o1.getTotalRemainingCount(), o2.getTotalRemainingCount());
                    }
                });
                break;
            default:
                products.sort(new Comparator<Product>() {
                    @Override
                    public int compare(Product o1, Product o2) {
                        return direction * Integer.compare(o1.getViewCount(), o2.getViewCount());
                    }
                });
                break;
        }
        return products;
    }

    //Done!!
    public String[] getProductAvailableFilters(){
        String[] availableFilters = new String[7];
        availableFilters[0] = "available";
        availableFilters[1] = "minPrice";
        availableFilters[2] = "macPrice";
        availableFilters[3] = "contains";
        availableFilters[4] = "brand";
        availableFilters[5] = "storeName";
        availableFilters[6] = "minRatingScore";
        return availableFilters;
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
            productIdNames.add(productPack(product));
        }
        return productIdNames;
    }

    protected String[] productPack(Product product) {
        String[] productPack = new String[3];
        productPack[0] = product.getId();
        productPack[1] = product.getName();
        productPack[2] = product.getBrand();
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
    public ArrayList<String[]> getProductsOfThisCategory(String categoryName) throws Exceptions.InvalidCategoryException {
        Category category = Category.getCategoryByName(categoryName);
        if (category == null)
            throw new Exceptions.InvalidCategoryException(categoryName);
        else{
            return productToIdNameBrand(getProductsInCategory(category));
        }
    }

    //Done!!
    private ArrayList<Product> getProductsInCategory(Category category){
        ArrayList<Product> products =new ArrayList<>();
        for (Category subCategory : category.getSubCategories()) {
            products.addAll(getProductsInCategory(subCategory));
        }
        products.addAll(category.getProducts());
        sortProducts("view count", false, products);
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

        products = sortProducts(sortBy, isIncreasing, products);

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
    //TODO: provide a method that returns the same String[4] by giving sub product ID. --> mainController.getSubProductByID(String subProductID);
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
        if (subProduct == null)
            throw new Exceptions.InvalidSubProductIdException(subProductId);
        else if (subProduct.getRemainingCount() < count)
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
     * @return *1- seller:String[6]
     * * { String firstName, String lastName, String phone, String email, String password, String storeName}
     * 2- customer:
     * * { String firstName, String lastName, String phone, String email, String password}
     * 3- admin:
     * * { String firstName, String lastName, String phone, String email, String password, String storeName}
     * @throws Exceptions.NotLoggedInException
     */
    public String[] getPersonalInfoEditableFields() throws Exceptions.NotLoggedInException {
        if (currentAccount == null)
            throw new Exceptions.NotLoggedInException();
        else {
            String[] editableFields = new String[5];
            if (currentAccount instanceof Seller) {
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
        info[1] = account.getClass().getName();
        info[2] = account.getFirstName();
        info[3] = account.getLastName();
        info[4] = account.getEmail();
        info[5] = account.getPhone();
        return info;
    }

    //Done!!

    /**
     * @return if
     * admin:     6: username, type, firstName, lastName, email, phone;
     * customer:  7: username, type, firstName, lastName, email, phone, balance;
     * seller:    8: username, type, firstName, lastName, email, phone, balance, storeName;
     */
    public String[] viewPersonalInfo() {
        return getPersonalInfo(currentAccount);
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
            productsInSale.add(productSalePack(subProduct));
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
            subProductsSalePacks.add(productSalePack(subProduct));
        }
        return subProductsSalePacks;
    }

    private String[] productSalePack(SubProduct subProduct){
        String[] productPack = new String[5];
        productPack[0] = subProduct.getProduct().getId();
        productPack[1] = subProduct.getProduct().getName();
        productPack[2] = subProduct.getProduct().getBrand();
        productPack[3] = Double.toString(subProduct.getRawPrice());
        productPack[4] = Double.toString(subProduct.getPriceWithSale());
        return productPack;
    }

    private ArrayList<Product> subProductToProduct( ArrayList<SubProduct> subProducts){
        ArrayList<Product> products = new ArrayList<>();
        for (SubProduct subProduct : subProducts) {
            products.add(subProduct.getProduct());
        }
        return products;
    }

    private void filterSubProducts(boolean available, double minPrice, double maxPrice, String contains, String brand,
                                   String storeName, double minRatingScore, ArrayList<SubProduct> subProducts){
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

    private void sortSubProducts( String sortBy, boolean isIncreasing, ArrayList<SubProduct> subProducts){
        int direction = isIncreasing ? 1 : -1;
        switch (sortBy) {
            case "price":
                subProducts.sort(new Comparator<SubProduct>() {
                    @Override
                    public int compare(SubProduct o1, SubProduct o2) {
                        return direction * Double.compare(o1.getPriceWithSale(), o2.getPriceWithSale());
                    }
                });
                break;
            case "name":
                subProducts.sort(new Comparator<SubProduct>() {
                    @Override
                    public int compare(SubProduct o1, SubProduct o2) {
                        return direction * o1.getProduct().getName().compareTo(o2.getProduct().getName());
                    }
                });
                break;
            case "rating score":
                subProducts.sort(new Comparator<SubProduct>() {
                    @Override
                    public int compare(SubProduct o1, SubProduct o2) {
                        return direction * Double.compare(o1.getProduct().getAverageRatingScore(), o2.getProduct().getAverageRatingScore());
                    }
                });
                break;
            case "category name":
                subProducts.sort(new Comparator<SubProduct>() {
                    @Override
                    public int compare(SubProduct o1, SubProduct o2) {
                        return direction * o1.getProduct().getCategory().getName().
                                compareTo(o2.getProduct().getCategory().getName());
                    }
                });
                break;
            case "remaining count":
                subProducts.sort(new Comparator<SubProduct>() {
                    @Override
                    public int compare(SubProduct o1, SubProduct o2) {
                        return direction * Integer.compare(o1.getRemainingCount(), o2.getRemainingCount());
                    }
                });
                break;
            default:
                subProducts.sort(new Comparator<SubProduct>() {
                    @Override
                    public int compare(SubProduct o1, SubProduct o2) {
                        return direction * Integer.compare(o1.getProduct().getViewCount(), o2.getProduct().getViewCount());
                    }
                });
                break;
        }
    }
}
