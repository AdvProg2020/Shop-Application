package controller;


import model.Cart;
import model.Category;
import model.Review;
import model.Sale;
import model.account.Account;
import model.account.Admin;
import model.account.Customer;
import model.account.Seller;
import model.database.Database;
import model.sellable.Sellable;
import model.sellable.SubProduct;
import model.sellable.SubSellable;

import java.util.*;


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

    public boolean doesManagerExist() {
        return Admin.getManager() != null;
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

    private void sortProducts(ArrayList<Sellable> sellables) {
        sellables.sort(new Utilities.Sort.ProductViewCountComparator(true));
    }


    private ArrayList<String[]> productToIdNameBrand(ArrayList<Sellable> sellables) {
        ArrayList<String[]> productIdNames = new ArrayList<>();
        for (Sellable sellable : sellables) {
            productIdNames.add(Utilities.Pack.product(sellable));
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

    private ArrayList<Sellable> getProductsInCategory(Category category) {
        ArrayList<Sellable> sellables = new ArrayList<>(category.getSellables(true));
        sortProducts(sellables);
        return sellables;
    }


    public ArrayList<String[]> sortFilterProducts(String categoryName, boolean inSale, String sortBy, boolean isIncreasing, boolean available, double minPrice, double maxPrice, String contains, String brand,
                                                  String storeName, double minRatingScore, HashMap<String, String> propertyFilters) {
        Category category = Category.getCategoryByName(categoryName) != null ? Category.getCategoryByName(categoryName) : Category.getSuperCategory();
        ArrayList<Sellable> sellables;
        if (category != null) {
            sellables = new ArrayList<>(category.getSellables(true));
        } else {
            sellables = new ArrayList<>(Sellable.getAllSellables());
        }

        for (String propertyFilter : propertyFilters.keySet()) {
            if ( ! propertyFilters.get(propertyFilter).equals(""))
                Utilities.Filter.ProductFilter.property(sellables, propertyFilter, propertyFilters.get(propertyFilter));
        }

        ArrayList<SubSellable> subSellables = new ArrayList<>();
        if (inSale) {
            for (Sellable sellable : sellables) {
                subSellables.addAll(sellable.getSubSellablesInSale());
            }
        } else {
            for (Sellable sellable : sellables) {
                subSellables.add(sellable.getDefaultSubSellable());
            }
        }

        filterSubProducts(available, minPrice, maxPrice, contains, brand, storeName, minRatingScore, subSellables);

        sortSubProducts(sortBy, isIncreasing, subSellables);
        return Utilities.Pack.subProductBoxes(subSellables);
    }

    public void showProduct(String productId) throws Exceptions.InvalidProductIdException {
        Sellable sellable = Sellable.getSellableById(productId);
        if (sellable == null)
            throw new Exceptions.InvalidProductIdException(productId);
        else {
            sellable.increaseViewCount();
        }
    }


    /**
     * @param productId
     * @return String[5]: ID, name, brand, infoText, averageRatingScore.
     * @throws Exceptions.InvalidProductIdException
     */
    public String[] digest(String productId) throws Exceptions.InvalidProductIdException {
        Sellable sellable = Sellable.getSellableById(productId);
        if (sellable == null)
            throw new Exceptions.InvalidProductIdException(productId);
        return Utilities.Pack.digest(sellable);
    }

    public HashMap<String, String> getPropertyValuesOfAProduct(String productId) throws Exceptions.InvalidProductIdException {
        Sellable sellable = Sellable.getSellableById(productId);
        if (sellable == null)
            throw new Exceptions.InvalidProductIdException(productId);
        else {
            return new HashMap<>(sellable.getPropertyValues());
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
        Sellable sellable = Sellable.getSellableById(productId);
        if (sellable == null)
            throw new Exceptions.InvalidProductIdException(productId);
        ArrayList<String[]> subProducts = new ArrayList<>();
        for (SubSellable subSellable : sellable.getSubSellables()) {
            subProducts.add(Utilities.Pack.subProduct(subSellable));
        }
        return subProducts;
    }

    public String[] getSubProductByID(String subProductId) throws Exceptions.InvalidSubProductIdException {
        SubSellable subSellable = SubSellable.getSubSellableById(subProductId);
        if (subSellable == null)
            throw new Exceptions.InvalidSubProductIdException(subProductId);
        else
            return Utilities.Pack.subProduct(subSellable);
    }

    /**
     * @param productId
     * @return String[3]: usernameOfReviewer, title, body, hasBought.
     * @throws Exceptions.InvalidProductIdException
     */
    public ArrayList<String[]> reviewsOfProductWithId(String productId) throws Exceptions.InvalidProductIdException {
        Sellable sellable = Sellable.getSellableById(productId);
        if (sellable == null)
            throw new Exceptions.InvalidProductIdException(productId);
        ArrayList<String[]> reviews = new ArrayList<>();
        for (Review review : sellable.getReviews()) {
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
        SubSellable subSellable = SubSellable.getSubSellableById(subProductId);
        if (subSellable == null)
            throw new Exceptions.InvalidSubProductIdException(subProductId);
        else if (((SubProduct)subSellable).getRemainingCount() < count + currentCart.getCountOfaSubSellable(subProductId))
            throw new Exceptions.UnavailableProductException(subProductId);
        else {
            currentCart.addSubSellableCount(subProductId, count);
            database.cart();
        }
    }

    public ArrayList<String[]> getProductsInCart() throws Exceptions.UnAuthorizedAccountException {
        checkAuthorityOverCart();
        ArrayList<String[]> shoppingCart = new ArrayList<>();
        Map<SubSellable, Integer> subProducts;
        if (currentAccount == null) subProducts = currentCart.getSubSellables();
        else subProducts = ((Customer) currentAccount).getCart().getSubSellables();
        for (SubSellable subSellable : subProducts.keySet()) {
            shoppingCart.add(Utilities.Pack.productInCart(subSellable, subProducts.get(subSellable)));
        }
        return shoppingCart;
    }

    public void increaseProductInCart(String subProductId, int number) throws Exceptions.NotSubProductIdInTheCartException,
            Exceptions.UnavailableProductException, Exceptions.InvalidSubProductIdException, Exceptions.UnAuthorizedAccountException {
        checkAuthorityOverCart();
        Map<SubSellable, Integer> subProducts = currentCart.getSubSellables();
        SubSellable subSellable = SubSellable.getSubSellableById(subProductId);
        if (subSellable == null)
            throw new Exceptions.InvalidSubProductIdException(subProductId);
        else if (!subProducts.containsKey(subSellable))
            throw new Exceptions.NotSubProductIdInTheCartException(subProductId);
        else if (number + subProducts.get(subSellable) > ((SubProduct)subSellable).getRemainingCount())
            throw new Exceptions.UnavailableProductException(subProductId);
        else {
            currentCart.addSubSellableCount(subProductId, number);
            database.cart();
        }
    }

    public void decreaseProductInCart(String subProductId, int number) throws Exceptions.InvalidSubProductIdException,
            Exceptions.NotSubProductIdInTheCartException, Exceptions.UnAuthorizedAccountException {
        checkAuthorityOverCart();
        SubSellable subSellable = SubSellable.getSubSellableById(subProductId);
        if (subSellable == null)
            throw new Exceptions.InvalidSubProductIdException(subProductId);
        else {
            Map<SubSellable, Integer> subProductsInCart = currentCart.getSubSellables();
            if (subProductsInCart.containsKey(subSellable)) {
                currentCart.addSubSellableCount(subProductId, -number);
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
            if (Sellable.getSellableById(productId) == null)
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
                                   String storeName, double minRatingScore, ArrayList<SubSellable> subSellables) {
        Utilities.Filter.SubProductFilter.available(subSellables, available);
        Utilities.Filter.SubProductFilter.minPrice(subSellables, minPrice);
        Utilities.Filter.SubProductFilter.maxPrice(subSellables, maxPrice);
        Utilities.Filter.SubProductFilter.name(subSellables, contains);
        Utilities.Filter.SubProductFilter.brand(subSellables, brand);
        Utilities.Filter.SubProductFilter.storeName(subSellables, storeName);
        Utilities.Filter.SubProductFilter.ratingScore(subSellables, minRatingScore);
    }

    private void sortSubProducts(String sortBy, boolean isIncreasing, ArrayList<SubSellable> subSellables) {
        if (sortBy == null) {
            sortBy = "view count";
        }
        switch (sortBy) {
            case "view count":
                subSellables.sort(new Utilities.Sort.SubProductViewCountComparator(isIncreasing));
                break;
            case "price":
                subSellables.sort(new Utilities.Sort.SubProductPriceComparator(isIncreasing));
                break;
            case "name":
                subSellables.sort(new Utilities.Sort.SubProductNameComparator(isIncreasing));
                break;
            case "rating score":
                subSellables.sort(new Utilities.Sort.SubProductRatingScoreComparator(isIncreasing));
                break;
            case "category name":
                subSellables.sort(new Utilities.Sort.SubProductCategoryNameComparator(isIncreasing));
                break;
            case "remaining count":
                subSellables.sort(new Utilities.Sort.SubProductRemainingCountComparator(isIncreasing));
                break;
            default:
                subSellables.sort(new Utilities.Sort.SubProductViewCountComparator(true));
        }
    }

    public void clearCart() {
        currentCart.clearCart();
    }

    public void removeSubProductFromCart(String subProductId) throws Exceptions.InvalidSubProductIdException {
        if (SubSellable.getSubSellableById(subProductId) == null)
            throw new Exceptions.InvalidSubProductIdException(subProductId);
        else currentCart.removeSubSellable(subProductId);
    }

    public String[] getDefaultSubProductOfAProduct(String productId) throws Exceptions.InvalidProductIdException {
        Sellable sellable = Sellable.getSellableById(productId);
        if (sellable == null)
            throw new Exceptions.InvalidProductIdException(productId);
        return Utilities.Pack.subProduct(sellable.getDefaultSubSellable());
    }

    public ArrayList<String> getPropertyValuesInCategory(String categoryName, String property) throws Exceptions.InvalidCategoryException {
        Category category = Category.getCategoryByName(categoryName);
        if (category != null) {
            ArrayList<String> values = new ArrayList<>();
            for (Sellable sellable : category.getSellables(true)) {
                values.add(sellable.getValue(property));
            }
            return values;
        } else
            throw new Exceptions.InvalidCategoryException(categoryName);
    }

    public ArrayList<String[]> getSubProductsForAdvertisements(int number) {
        ArrayList<Sellable> selectedSellables = new ArrayList<>(Sellable.getAllSellables());
        int rangeSize = number * 3;
        int numberOfProducts = selectedSellables.size();
        if (numberOfProducts > rangeSize) {
            ArrayList<Sellable> allSellables = selectedSellables;
            selectedSellables = new ArrayList<>();
            for (int i = 0; i < rangeSize; i++) {
                selectedSellables.add(allSellables.get(numberOfProducts - 1 - i));
            }
            numberOfProducts = rangeSize;
        }
        ArrayList<String[]> productsToShow = new ArrayList<>();
        SubSellable chosenSubSellable;
        Random r = new Random();
        int randomNumber;
        number = Math.min(number, numberOfProducts);
        for (int i = 0; i < number; i++) {
            randomNumber = r.nextInt(numberOfProducts);
            chosenSubSellable = selectedSellables.get(randomNumber).getDefaultSubSellable();
            productsToShow.add(Utilities.Pack.subProduct(chosenSubSellable));
            selectedSellables.remove(randomNumber);
            numberOfProducts--;
        }

        return productsToShow;
    }

    public ArrayList<String[]> getSubProductsInSale(int number) {
        HashSet<Sellable> candidateSellables = new HashSet<>();
        choosingProduct:
        for (Sale sale : Sale.getAllSales()) {
            if (!sale.hasStarted()) continue;
            for (SubSellable subSellable : sale.getSubSellables()) {
                candidateSellables.add(subSellable.getSellable());
                if (candidateSellables.size() > number * 3) {
                    break choosingProduct;
                }
            }
        }

        int numberOfProducts = candidateSellables.size();
        number = Math.min(numberOfProducts, number);
        ArrayList<Sellable> orderedSellables = new ArrayList<>(candidateSellables);
        ArrayList<String[]> subProductsToShow = new ArrayList<>();
        Random r = new Random();
        int randomNumber;
        Sellable chosenSellable;
        SubSellable chosenSubSellable;
        ArrayList<SubSellable> inSaleSubSellables;
        for (int i = 0; i < number; i++) {
            randomNumber = r.nextInt(numberOfProducts);
            chosenSellable = orderedSellables.remove(randomNumber);
            numberOfProducts--;

            inSaleSubSellables = new ArrayList<>(chosenSellable.getSubSellablesInSale());
            chosenSubSellable = inSaleSubSellables.get(r.nextInt(inSaleSubSellables.size()));
            subProductsToShow.add(Utilities.Pack.subProduct(chosenSubSellable));
        }

        return subProductsToShow;
    }

    public ArrayList<String> getCategoryTreeOfAProduct(String productId) throws Exceptions.InvalidProductIdException {
        Sellable sellable = Sellable.getSellableById(productId);
        if (sellable == null) {
            throw new Exceptions.InvalidProductIdException(productId);
        } else {
            return getCategoryTreeOfACategory(sellable.getCategory().getName());
        }
    }

    public ArrayList<String> getCategoryTreeOfACategory(String categoryName) {
        Category superCategory = Category.getSuperCategory();
        Category category = Category.getCategoryByName(categoryName);
        ArrayList<String> categoryTree = new ArrayList<>();
        if (category != null) {
            while (!category.equals(superCategory)) {
                categoryTree.add(0, category.getName());
                category = category.getParent();
            }
        }
        return categoryTree;
    }

    public ArrayList<String> getBuyersOfASubProduct(String subProductId) throws Exceptions.InvalidSubProductIdException {
        SubSellable subSellable = SubSellable.getSubSellableById(subProductId);
        if (subSellable == null) {
            throw new Exceptions.InvalidSubProductIdException(subProductId);
        } else {
            ArrayList<String> buyerUserNames = new ArrayList<>();
            for (Customer customer : subSellable.getCustomers()) {
                buyerUserNames.add(customer.getUsername());
            }
            return buyerUserNames;
        }
    }

}
