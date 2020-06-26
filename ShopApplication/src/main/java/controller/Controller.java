package controller;


import model.*;
import model.account.Account;
import model.account.Admin;
import model.account.Customer;
import model.account.Seller;
import model.database.Database;

import java.util.*;

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
                             String email, String phone, double balance, String storeName, String imagePath) throws Exceptions.UsernameAlreadyTakenException, Exceptions.AdminRegisterException {
        usernameTypeValidation(username, type);
        switch (type) {
            case "Customer":
                new Customer(username, password, firstName, lastName, email, phone, imagePath, balance);
                database.createCustomer();
                break;
            case "Admin":
                new Admin(username, password, firstName, lastName, email, phone, imagePath);
                database.createAdmin();
                break;
            case "Seller":
                new Seller(username, password, firstName, lastName, email, phone, imagePath, storeName, balance, database);
                database.request();
                break;
        }
    }

    public boolean isManager() {
        return (currentAccount != null) && currentAccount == Admin.getManager();
    }

    public boolean doesExistManager() {
        return Admin.getManager() == null;
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
        if (currentAccount == null)
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

    private void sortProducts(ArrayList<Product> products) {
        products.sort(new Utilities.Sort.ProductViewCountComparator(true));
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

    public ArrayList<String> getSubCategoriesOfACategory(String categoryName) throws Exceptions.InvalidCategoryException {
        Category category = Category.getCategoryByName(categoryName);
        if (category == null)
            throw new Exceptions.InvalidCategoryException(categoryName);
        else {
            ArrayList<String> categoryNames = new ArrayList<>();
            for (Category subCategory : category.getSubCategories()) {
                categoryNames.add(subCategory.getName());
            }
            return categoryNames;
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
        sortProducts( products);
        return products;
    }


    public ArrayList<String[]> sortFilterProducts(String categoryName, boolean inSale, String sortBy, boolean isIncreasing, boolean available, double minPrice, double maxPrice, String contains, String brand,
                                                  String storeName, double minRatingScore, HashMap<String, String> propertyFilters) {
        Category category = Category.getCategoryByName(categoryName) != null ? Category.getCategoryByName(categoryName) : Category.getSuperCategory();
        ArrayList<Product> products;
        if (category != null) {
            products = new ArrayList<>(category.getProducts(true));
        } else {
            products = new ArrayList<>(Product.getAllProducts());
        }

        ArrayList<SubProduct> subProducts = new ArrayList<>();
        if (inSale) {
            for (Product product : products) {
                subProducts.addAll(product.getSubProductsInSale());
            }
        } else {
            for (Product product : products) {
                subProducts.add(product.getDefaultSubProduct());
            }
        }

        filterSubProducts(available, minPrice, maxPrice, contains, brand, storeName, minRatingScore, subProducts);
        for (String propertyFilter : propertyFilters.keySet()) {
            if (propertyFilters.get(propertyFilter) != null)
                Utilities.Filter.ProductFilter.property(products, propertyFilter, propertyFilters.get(propertyFilter));
        }
        sortSubProducts(sortBy, isIncreasing, subProducts);
        return Utilities.Pack.subProductBoxes(subProducts);
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

    public HashMap<String, String> getPropertyValuesOfAProduct(String productId) throws Exceptions.InvalidProductIdException {
        Product product = Product.getProductById(productId);
        if (product == null)
            throw new Exceptions.InvalidProductIdException(productId);
        else {
            return new HashMap<>(product.getPropertyValues());
        }
    }

    public ArrayList<String> getPropertiesOfCategory(String categoryName, boolean deep) throws Exceptions.InvalidCategoryException {
        Category category = Category.getCategoryByName(categoryName);
        if (category == null)
            throw new Exceptions.InvalidCategoryException(categoryName);
        else
            return new ArrayList<>(category.getProperties(deep));
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
            subProducts.add(Utilities.Pack.subProductInProduct(subProduct));
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
        Map<SubProduct, Integer> subProducts;
        if (currentAccount == null) subProducts = currentCart.getSubProducts();
        else subProducts = ((Customer) currentAccount).getCart().getSubProducts();
        for (SubProduct subProduct : subProducts.keySet()) {
            shoppingCart.add(Utilities.Pack.productInCart(subProduct, subProducts.get(subProduct)));
        }
        return shoppingCart;
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
                new Review(currentAccount.getId(), productId, title, text, database);
                database.request();
            }
        }
    }

    /**
     * @return if
     * admin:     6: username, type, firstName, lastName, email, phone;
     * customer:  7: username, type, firstName, lastName, email, phone, balance;
     * seller:    8: username, type, firstName, lastName, email, phone, balance, storeName;
     */
    public String[] viewPersonalInfo() throws Exceptions.NotLoggedInException {
        if (currentAccount == null) throw new Exceptions.NotLoggedInException();
        return Utilities.Pack.personalInfo(currentAccount);
    }


    public String[] viewPersonalInfo(String username) throws Exceptions.UsernameDoesntExistException {
        Account account = Account.getAccountByUsername(username);
        if (account == null) throw new Exceptions.UsernameDoesntExistException(username);
        else return Utilities.Pack.personalInfo(account);
    }

    public void editPersonalInfo(String field, String newInformation) throws Exceptions.InvalidFieldException, Exceptions.SameAsPreviousValueException {
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
            case "image path":
                if (currentAccount.getImagePath().equals(newInformation))
                    throw new Exceptions.SameAsPreviousValueException(field);
                currentAccount.setImagePath(newInformation);
                break;
            default:
                throw new Exceptions.InvalidFieldException();
        }
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

    public void clearCart() {
        currentCart.clearCart();
    }

    public void removeSubProductFromCart(String subProductId) throws Exceptions.InvalidSubProductIdException {
        if (SubProduct.getSubProductById(subProductId) == null)
            throw new Exceptions.InvalidSubProductIdException(subProductId);
        else currentCart.removeSubProduct(subProductId);
    }

    public String[] getDefaultSubProductOfAProduct(String productId) throws Exceptions.InvalidProductIdException {
        Product product = Product.getProductById(productId);
        if (product == null)
            throw new Exceptions.InvalidProductIdException(productId);
        return Utilities.Pack.subProduct(product.getDefaultSubProduct());
    }

    public ArrayList<String> getPropertyValuesInCategory(String categoryName, String property) throws Exceptions.InvalidCategoryException {
        Category category = Category.getCategoryByName(categoryName);
        if (category != null) {
            ArrayList<String> values = new ArrayList<>();
            values.add(null);
            for (Product product : category.getProducts(true)) {
                values.add(product.getValue(property));
            }
            return values;
        } else
            throw new Exceptions.InvalidCategoryException(categoryName);
    }

    public ArrayList<String[]> getSubProductsForAdvertisements(int number) {
        ArrayList<Product> selectedProducts = new ArrayList<>(Product.getAllProducts());
        int rangeSize = number * 3;
        int numberOfProducts = selectedProducts.size();
        if (numberOfProducts > rangeSize) {
            ArrayList<Product> allProducts = selectedProducts;
            selectedProducts = new ArrayList<>();
            for (int i = 0; i < rangeSize; i++) {
                selectedProducts.add(allProducts.get(numberOfProducts - 1 - i));
            }
            numberOfProducts = rangeSize;
        }
        ArrayList<String[]> productsToShow = new ArrayList<>();
        SubProduct chosenSubProduct;
        Random r = new Random();
        int randomNumber;
        number = Math.min(number, numberOfProducts);
        for (int i = 0; i < number; i++) {
            randomNumber = r.nextInt(numberOfProducts);
            chosenSubProduct = selectedProducts.get(randomNumber).getDefaultSubProduct();
            productsToShow.add(Utilities.Pack.subProduct(chosenSubProduct));
            selectedProducts.remove(randomNumber);
            numberOfProducts--;
        }

        return productsToShow;
    }

    public ArrayList<String[]> getSubProductsInSale(int number) {
        HashSet<Product> candidateProducts = new HashSet<>();
        choosingProduct:
        for (Sale sale : Sale.getAllSales()) {
            for (SubProduct subProduct : sale.getSubProducts()) {
                candidateProducts.add(subProduct.getProduct());
                if (candidateProducts.size() > number * 3) {
                    break choosingProduct;
                }
            }
        }

        int numberOfProducts = candidateProducts.size();
        number = Math.min(numberOfProducts, number);
        ArrayList<Product> orderedProducts = new ArrayList<>(candidateProducts);
        ArrayList<String[]> subProductsToShow = new ArrayList<>();
        Random r = new Random();
        int randomNumber;
        Product chosenProduct;
        SubProduct chosenSubProduct;
        ArrayList<SubProduct> inSaleSubProducts;
        for (int i = 0; i < number; i++) {
            randomNumber = r.nextInt(numberOfProducts);
            chosenProduct = orderedProducts.get(randomNumber);
            orderedProducts.remove(randomNumber);
            numberOfProducts--;

            inSaleSubProducts = new ArrayList<>(chosenProduct.getSubProductsInSale());
            chosenSubProduct = inSaleSubProducts.get(r.nextInt(inSaleSubProducts.size()));
            subProductsToShow.add(Utilities.Pack.subProduct(chosenSubProduct));
        }

        return subProductsToShow;
    }

    public ArrayList<String> getCategoryTreeOfAProduct(String productId) throws Exceptions.InvalidProductIdException {
        Product product = Product.getProductById(productId);
        if (product == null) {
            throw new Exceptions.InvalidProductIdException(productId);
        } else {
            return getCategoryTreeOfACategory(product.getCategory().getName());
        }
    }

    public ArrayList<String> getCategoryTreeOfACategory(String categoryName) {
        Category superCategory = Category.getSuperCategory();
        Category category = Category.getCategoryByName(categoryName);
        ArrayList<String> categoryTree = new ArrayList<>();
        if (category != null) {
            while (!category.equals(superCategory)) {
                categoryTree.add(0, category.getName());
                category.getParent();
            }
        }
        return categoryTree;
    }

    public ArrayList<String> getBuyersOfASubProduct(String subProductId) throws Exceptions.InvalidSubProductIdException {
        SubProduct subProduct = SubProduct.getSubProductById(subProductId);
        if (subProduct == null) {
            throw new Exceptions.InvalidSubProductIdException(subProductId);
        } else {
            ArrayList<String> buyerUserNames = new ArrayList<>();
            for (Customer customer : subProduct.getCustomers()) {
                buyerUserNames.add(customer.getUsername());
            }
            return buyerUserNames;
        }
    }

}
