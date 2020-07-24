package Server.controller;

import Server.model.*;
import Server.model.account.*;
import Server.model.chat.Message;
import Server.model.chat.SupportChat;
import Server.model.log.BuyLog;
import Server.model.log.LogItem;
import Server.model.log.SellLog;
import Server.model.request.EditFileRequest;
import Server.model.request.EditProductRequest;
import Server.model.request.EditSaleRequest;
import Server.model.request.Request;
import Server.model.sellable.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

public class Utilities {
    private static DateFormat dateFormat = new SimpleDateFormat("yy-MM-dd");

    public static DateFormat getDateFormat() {
        return dateFormat;
    }

    static class Pack {
        public static String[] saleInfo(Sale sale) {
            String[] salePack = new String[7];
            salePack[0] = sale.getId();
            salePack[1] = sale.getSeller().getUsername();
            salePack[2] = Double.toString(sale.getPercentage());
            salePack[3] = dateFormat.format(sale.getStartDate());
            salePack[4] = dateFormat.format(sale.getEndDate());
            salePack[5] = Integer.toString(sale.getSubSellables().size());
            salePack[6] = String.valueOf(sale.getMaximumAmount());
            return salePack;
        }

        public static String[] auctionInfo(Auction auction) {
            String[] auctionPack = new String[8];
            auctionPack[0] = auction.getId();
            auctionPack[1] = auction.getSeller().getUsername();
            auctionPack[2] = auction.getSubSellable().getId();
            auctionPack[3] = dateFormat.format(auction.getStartDate());
            auctionPack[4] = dateFormat.format(auction.getEndDate());
            auctionPack[5] = auction.getHighestBid() + "";
            auctionPack[6] = auction.getHighestBidder() == null ? "-" : auction.getHighestBidder().getUsername();
            auctionPack[7] = auction.getChat().getId();
            return auctionPack;
        }

        public static String[] newSaleInRequest(Sale sale) {
            String[] salePack = new String[5];
            salePack[0] = sale.getSeller().getUsername();
            salePack[1] = Double.toString(sale.getPercentage());
            salePack[2] = Double.toString(sale.getMaximumAmount());
            salePack[3] = dateFormat.format(sale.getStartDate());
            salePack[4] = dateFormat.format(sale.getEndDate());

            return salePack;
        }

        public static String[] product(Product product) {
            String[] productPack = new String[4];
            productPack[0] = product.getId();
            productPack[1] = product.getName();
            productPack[2] = product.getBrand();
            productPack[3] = product.getCategory().getName();
            return productPack;
        }

        public static String[] subProduct(SubProduct subProduct){
            String[] subProductBoxPack = new String[20];
            Product product = subProduct.getProduct();
            Sale sale = subProduct.getSale();
            Auction auction = subProduct.getAuction();
            subProductBoxPack[0] = product.getId();
            subProductBoxPack[1] = subProduct.getId();
            subProductBoxPack[2] = product.getName();
            subProductBoxPack[3] = product.getBrand();
            subProductBoxPack[4] = Double.toString(product.getAverageRatingScore());
            subProductBoxPack[5] = Integer.toString(product.getRatingsCount());
            subProductBoxPack[6] = product.getImagePath();
            subProductBoxPack[7] = Double.toString(subProduct.getRawPrice());
            subProductBoxPack[8] = Double.toString(subProduct.getPriceWithSale());
            subProductBoxPack[9] = Integer.toString(subProduct.getRemainingCount());
            subProductBoxPack[10] = sale != null ? dateFormat.format(sale.getEndDate()) : null;
            subProductBoxPack[11] = sale != null ? Double.toString(sale.getPercentage()) : null;
            subProductBoxPack[12] = subProduct.getSeller().getStoreName();
            subProductBoxPack[13] = subProduct.getProduct().getInfoText();
            subProductBoxPack[14] = subProduct.getSeller().getUsername();
            subProductBoxPack[15] = "SubProduct";
            subProductBoxPack[16] = auction != null ? Double.toString(auction.getHighestBid()) : null;
            subProductBoxPack[17] = auction != null ? auction.getHighestBidder().getUsername() : null;
            subProductBoxPack[18] = auction != null ? dateFormat.format(auction.getEndDate()) : null;
            subProductBoxPack[19] = auction != null ? auction.getId() : null;
            return subProductBoxPack;
        }

        public static String[] subFile(SubFile subFile){
            String[] subFileBoxPack = new String[20];
            File file = subFile.getFile();
            Sale sale = subFile.getSale();
            Auction auction = subFile.getAuction();
            subFileBoxPack[0] = file.getId();
            subFileBoxPack[1] = subFile.getId();
            subFileBoxPack[2] = file.getName();
            subFileBoxPack[3] = file.getExtension();
            subFileBoxPack[4] = Double.toString(file.getAverageRatingScore());
            subFileBoxPack[5] = Integer.toString(file.getRatingsCount());
            subFileBoxPack[6] = file.getImagePath();
            subFileBoxPack[7] = Double.toString(subFile.getRawPrice());
            subFileBoxPack[8] = Double.toString(subFile.getPriceWithSale());
            subFileBoxPack[9] = "-";
            subFileBoxPack[10] = sale != null ? dateFormat.format(sale.getEndDate()) : null;
            subFileBoxPack[11] = sale != null ? Double.toString(sale.getPercentage()) : null;
            subFileBoxPack[12] = subFile.getSeller().getStoreName();
            subFileBoxPack[13] = subFile.getFile().getInfoText();
            subFileBoxPack[14] = subFile.getSeller().getUsername();
            subFileBoxPack[15] = "SubFile";
            subFileBoxPack[16] = auction != null ? Double.toString(auction.getHighestBid()) : null;
            subFileBoxPack[17] = auction != null ? auction.getHighestBidder().getUsername() : null;
            subFileBoxPack[18] = auction != null ? dateFormat.format(auction.getEndDate()) : null;
            subFileBoxPack[19] = auction != null ? auction.getId() : null;
            return subFileBoxPack;
        }

        public static String[] subSellable(SubSellable subSellable){
            if(subSellable.getClass().getSimpleName().equals("SubProduct"))
                return subProduct((SubProduct) subSellable);
            else
                return subFile((SubFile) subSellable);
        }

        public static String[] auction(Auction auction){
            String[] auctionPack = new String[7];
            auctionPack[0] = auction.getId();
            auctionPack[1] = auction.getSubSellable().getId();
            auctionPack[2] = auction.getChat().getId();
            auctionPack[3] = Double.toString(auction.getHighestBid());
            auctionPack[4] = auction.getHighestBidder().getUsername();
            auctionPack[5] = dateFormat.format(auction.getStartDate());
            auctionPack[6] = dateFormat.format(auction.getEndDate());
            return auctionPack;
        }

        public static String[] subProductInProduct(SubProduct subProduct){
            String[] subProductPack = new String[8];
            Product product = subProduct.getProduct();
            Sale sale = subProduct.getSale();
            subProductPack[0] = product.getId();
            subProductPack[1] = subProduct.getId();
            subProductPack[2] = Double.toString(subProduct.getRawPrice());
            subProductPack[3] = Double.toString(subProduct.getPriceWithSale());
            subProductPack[4] = Integer.toString(subProduct.getRemainingCount());
            subProductPack[5] = sale != null ? dateFormat.format(sale.getEndDate()) : null;
            subProductPack[6] = sale != null ? Double.toString(sale.getPercentage()) : null;
            subProductPack[7] = subProduct.getSeller().getStoreName();
            return subProductPack;
        }

        public static String[] sellerSubProduct(SubProduct subProduct){
            String[] subProductPack = new String[7];
            Product product = subProduct.getProduct();
            Sale sale = subProduct.getSale();
            subProductPack[0] = product.getId();
            subProductPack[1] = subProduct.getId();
            subProductPack[2] = product.getName();
            subProductPack[3] = product.getBrand();
            subProductPack[4] = Double.toString(subProduct.getRawPrice());
            subProductPack[5] = Integer.toString(subProduct.getRemainingCount());
            subProductPack[6] = sale != null ? sale.getId() : null;
            return subProductPack;
        }

        public static String[] addProductRequest(SubProduct subProduct, Product product){
            String[] productPack = new String[7];
            productPack[0] = product.getName();
            productPack[1] = product.getBrand();
            productPack[2] = product.getImagePath();
            productPack[3] = product.getCategory().getName();
            productPack[4] = product.getInfoText();
            productPack[5] = Integer.toString(subProduct.getRemainingCount());
            productPack[6] = Double.toString(subProduct.getRawPrice());
            return productPack;
        }

        public static String[] addFileRequest(SubFile subFile, File file){
            String[] filePack = new String[7];
            filePack[0] = file.getName();
            filePack[1] = file.getExtension();
            filePack[2] = file.getImagePath();
            filePack[3] = file.getCategory().getName();
            filePack[4] = file.getInfoText();
            filePack[5] = Double.toString(subFile.getRawPrice());
            filePack[6] = subFile.getDownloadPath();
            return filePack;
        }

        public static String[] addAuctionRequest(Auction auction) {
            String[] auctionPack = new String[5];
            auctionPack[0] = auction.getId();
            auctionPack[1] = auction.getSeller().getStoreName();
            auctionPack[2] = auction.getSubSellable().getId();
            auctionPack[3] = dateFormat.format(auction.getStartDate());
            auctionPack[4] = dateFormat.format(auction.getEndDate());
            return auctionPack;
        }

        public static String[] review(Review review) {
            String[] reviewPack = new String[4];
            reviewPack[0] = review.getReviewer().getUsername();
            reviewPack[1] = review.getTitle();
            reviewPack[2] = review.getText();
            reviewPack[3] = review.hasBought() ? "yes" : "no";
            return reviewPack;
        }

        public static String[] getReviewInfo(Review review) {
            String[] reviewInfo = new String[7];
            reviewInfo[0] = review.getReviewer().getUsername();
            reviewInfo[1] = review.getSellable().getId();
            reviewInfo[2] = review.getSellable().getName();
            if(review.getSellable().getClass().getSimpleName().equals("Product"))
                reviewInfo[3] = ((Product)review.getSellable()).getBrand();
            else
                reviewInfo[3] = ((File)review.getSellable()).getExtension();
            reviewInfo[4] = review.getTitle();
            reviewInfo[5] = review.getText();
            reviewInfo[6] = review.hasBought() ? "yes" : "no";
            return reviewInfo;
        }

        public static String[] productInCart(SubProduct subProduct, int count) {
            String[] productPack = new String[9];
            productPack[0] = subProduct.getId();
            productPack[1] = subProduct.getProduct().getId();
            productPack[2] = subProduct.getProduct().getName();
            productPack[3] = subProduct.getProduct().getBrand();
            productPack[4] = subProduct.getSeller().getUsername();
            productPack[5] = subProduct.getSeller().getStoreName();
            productPack[6] = Integer.toString(count);
            productPack[7] = Double.toString(subProduct.getPriceWithSale());
            productPack[8] = subProduct.getProduct().getImagePath();
            return productPack;
        }

        public static String[] productInSale(SubProduct subProduct) {
            String[] productPack = new String[5];
            productPack[0] = subProduct.getProduct().getId();
            productPack[1] = subProduct.getProduct().getName();
            productPack[2] = subProduct.getProduct().getBrand();
            productPack[3] = Double.toString(subProduct.getRawPrice());
            productPack[4] = Double.toString(subProduct.getPriceWithSale());
            return productPack;
        }

        public static String[] sellLog(SellLog sellLog) {
            String[] sellPack = new String[9];
            sellPack[0] = sellLog.getId();
            sellPack[1] = dateFormat.format(sellLog.getDate());
            sellPack[2] = sellLog.getCustomer().getUsername();
            sellPack[3] = Double.toString(sellLog.getReceivedMoney());
            sellPack[4] = Double.toString(sellLog.getTotalSaleAmount());
            sellPack[5] = sellLog.getReceiverName();
            sellPack[6] = sellLog.getReceiverPhone();
            sellPack[7] = sellLog.getReceiverAddress();
            sellPack[8] = sellLog.getShippingStatus().toString();
            return sellPack;
        }

        public static String[] sellLogItem(LogItem item) {
            String[] productPack = new String[8];
            Product product = item.getSubProduct().getProduct();
            productPack[0] = product.getId();
            productPack[1] = product.getName();
            productPack[2] = product.getBrand();
            productPack[3] = Integer.toString(item.getCount());
            productPack[4] = Double.toString(item.getPrice());
            productPack[5] = Double.toString(item.getSaleAmount());
            return productPack;
        }

        public static String[] discountInfo(Discount discount) {
            String[] discountInfo = new String[6];
            discountInfo[0] = discount.getId();
            discountInfo[1] = discount.getDiscountCode();
            discountInfo[2] = dateFormat.format(discount.getStartDate());
            discountInfo[3] = dateFormat.format(discount.getEndDate());
            discountInfo[4] = Double.toString(discount.getMaximumAmount());
            discountInfo[5] = Double.toString(discount.getPercentage());
            return discountInfo;
        }

        public static String[] personalInfo(Account account) {
            String[] info;
            if (account instanceof Customer) {
                info = new String[10];
                info[8] = Double.toString(((Customer) account).getWallet().getBalance());
                info[9] = account.getClass().getSimpleName();
            } else if (account instanceof Seller) {
                info = new String[11];
                info[8] = Double.toString(((Seller) account).getWallet().getBalance());
                info[9] = ((Seller) account).getStoreName();
                info[10] = account.getClass().getSimpleName();
            } else if (account instanceof Admin){
                info = new String[9];
                info[8] = account.getClass().getSimpleName();
            } else {
                info = new String[9];
                info[8] = account.getClass().getSimpleName();
            }
            info[0] = account.getUsername();
            info[1] = account.getPassword();
            info[2] = account.getId();
            info[3] = account.getFirstName();
            info[4] = account.getLastName();
            info[5] = account.getEmail();
            info[6] = account.getPhone();
            info[7] = account.getImagePath();
            return info;
        }

        public static String[] sellerInRequest(Seller seller){
            String[] info = new String[8];
            info[0] = seller.getUsername();
            info[1] = seller.getFirstName();
            info[2] = seller.getLastName();
            info[3] = seller.getEmail();
            info[4] = seller.getPhone();
            info[5] = Double.toString(seller.getWallet().getBalance());
            info[6] = seller.getStoreName();
            info[7] = seller.getImagePath();
            return info;
        }

        public static String[] digest(Product product) {
            String[] productInfo = new String[11];
            productInfo[0] = product.getId();
            productInfo[1] = product.getName();
            productInfo[2] = product.getBrand();
            productInfo[3] = product.getInfoText();
            productInfo[4] = Double.toString(product.getAverageRatingScore());
            productInfo[5] = Integer.toString(product.getRatingsCount());
            productInfo[6] = product.getDefaultSubProduct().getId();
            productInfo[7] = product.getCategory().getName();
            productInfo[8] = product.getImagePath();
            productInfo[9] = product.getMinPrice() + "";
            productInfo[10] = product.getMaxPrice() + "";
            return productInfo;
        }

        public static String[] category(Category category) {
            String[] categoryPack = new String[3];
            categoryPack[0] = category.getId();
            categoryPack[1] = category.getName();
            categoryPack[2] = category.getParent().getName();
            return categoryPack;
        }

        public static String[] buyLog(BuyLog buyLog) {
            String[] orderPack = new String[9];
            orderPack[0] = buyLog.getId();
            orderPack[1] = buyLog.getCustomer().getUsername();
            orderPack[2] = buyLog.getReceiverName();
            orderPack[3] = buyLog.getReceiverPhone();
            orderPack[4] = buyLog.getReceiverAddress();
            orderPack[5] = dateFormat.format(buyLog.getDate());
            orderPack[6] = buyLog.getShippingStatus().toString();
            orderPack[7] = Double.toString(buyLog.getPaidMoney());
            orderPack[8] = Double.toString(buyLog.getTotalDiscountAmount());
            return orderPack;
        }

        public static String[] buyLogItem(LogItem item) {
            String[] productPack = new String[9];
            Product product = item.getSubProduct().getProduct();
            productPack[0] = product.getId();
            productPack[1] = product.getName();
            productPack[2] = product.getBrand();
            productPack[3] = item.getSeller().getUsername();
            productPack[4] = item.getSeller().getStoreName();
            productPack[5] = Integer.toString(item.getCount());
            productPack[6] = Double.toString(item.getPrice());
            productPack[7] = Double.toString(item.getSaleAmount());
            productPack[8] = product.getAverageRatingScore() + "";
            return productPack;
        }

        public static String[] request(Request request) {
            String[] requestPack = new String[4];
            requestPack[0] = request.getId();
            requestPack[1] = request.getClass().getSimpleName();
            requestPack[2] = dateFormat.format(request.getDate());
            requestPack[3] = request.getStatus().toString();
            return requestPack;
        }

        public static String[] saleChange(EditSaleRequest request) {
            String[] saleChange = new String[3];
            saleChange[0] = request.getField().toString();
            saleChange[1] = request.getNewValue();
            saleChange[2] = request.getOldValue();
            return saleChange;
        }

        public static String[] productChange(EditProductRequest request) {
            String[] productChange = new String[3];
            productChange[0] = request.getField().toString();
            productChange[1] = request.getNewValue();
            productChange[2] = request.getOldValue();
            return productChange;
        }

        public static String[] fileChange(EditFileRequest request){
            String[] fileChange = new String[3];
            fileChange[0] = request.getField().toString();
            fileChange[1] = request.getNewValue();
            fileChange[2] = request.getOldValue();
            return fileChange;
        }

        public static String[] customerDiscountRemainingCount(Customer customer, int count) {
            String[] personPack = new String[3];
            personPack[0] = customer.getUsername();
            personPack[1] = Integer.toString(count);
            personPack[2] = customer.getId();
            return personPack;
        }

        public static String invalidProductIds(ArrayList<String> subProductIds) {
            StringBuilder invalidSubProductIds = new StringBuilder();
            String falseSubProduct;
            for (String subProductId : subProductIds) {
                falseSubProduct = "\n" + subProductId;
                invalidSubProductIds.append(falseSubProduct);
            }
            return invalidSubProductIds.toString();
        }

        public static String invalidAccountIds(ArrayList<String> accountIds) {
            StringBuilder invalidAccountIds = new StringBuilder();
            String falseAccount;
            for (String accountId : accountIds) {
                falseAccount = "\n" + accountId;
                invalidAccountIds.append(falseAccount);
            }
            return invalidAccountIds.toString();
        }

        public static ArrayList<String[]> subSellableBoxes(ArrayList<SubSellable> subSellables){
            ArrayList<String[]> subProductBoxes = new ArrayList<>();
            for (SubSellable subSellable : subSellables) {
                if( subSellable.getClass().getSimpleName().equals("SubProduct"))
                    subProductBoxes.add(subProduct((SubProduct) subSellable));
                else
                    subProductBoxes.add(subFile((SubFile)subSellable));
            }
            return subProductBoxes;
        }

        public static String[] supportChat(SupportChat chat){
            String[] chatPack = new String[2];
            chatPack[0] = chat.getId();
            chatPack[1] = chat.getCustomer().getUsername();
            return chatPack;
        }

        public static String[] message(Message message, String viewerUsername){
            String[] messagePack = new String[5];
            messagePack[0] = message.getId();
            messagePack[1] = message.getSender().getUsername();
            messagePack[2] = dateFormat.format(message.getSendDate());
            messagePack[3] = message.getText();
            messagePack[4] = viewerUsername.equals(message.getSender().getUsername()) ? "sender" : "not sender";
            return messagePack;
        }
    }

    static class Field {
        public static String[] customerPersonalInfoEditableFields() {
            String[] editableFields = new String[5];
            editableFields[0] = "firstName";
            editableFields[1] = "lastName";
            editableFields[2] = "phone";
            editableFields[3] = "email";
            editableFields[4] = "password";
            return editableFields;
        }

        public static String[] sellerPersonalInfoEditableFields() {
            String[] editableFields = new String[6];
            editableFields[0] = "firstName";
            editableFields[1] = "lastName";
            editableFields[2] = "phone";
            editableFields[3] = "email";
            editableFields[4] = "password";
            editableFields[5] = "storeName";
            return editableFields;
        }

        public static String[] adminPersonalInfoEditableFields() {
            String[] editableFields = new String[5];
            editableFields[0] = "firstName";
            editableFields[1] = "lastName";
            editableFields[2] = "phone";
            editableFields[3] = "email";
            editableFields[4] = "password";
            return editableFields;
        }

        public static String[] productEditableFields() {
            String[] editableFields = new String[5];
            editableFields[0] = "name";
            editableFields[1] = "brand";
            editableFields[2] = "info text";
            editableFields[3] = "price";
            editableFields[4] = "count";
            return editableFields;
        }

        public static String[] saleEditableFields() {
            String[] saleEditableFields = new String[4];
            saleEditableFields[0] = "start date";
            saleEditableFields[1] = "end date";
            saleEditableFields[2] = "percentage";
            saleEditableFields[3] = "maximum";
            return saleEditableFields;
        }

        public static String[] discountEditableFields() {
            String[] editableFields = new String[4];
            editableFields[0] = "start date";
            editableFields[1] = "end date";
            editableFields[2] = "maximum amount";
            editableFields[3] = "percentage";
            return editableFields;
        }

        public static String[] getCategoryEditableFields() {
            String[] editableFields = new String[2];
            editableFields[0] = "name";
            editableFields[1] = "parent";
            return editableFields;
        }

    }

    static class Filter {
        public static String[] productAvailableFilters() {
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

        public static ArrayList<String> getAvailableValuesOfAPropertyOfACategory(String categoryName, String property) throws Exceptions.InvalidCategoryException {
            Category category = Category.getCategoryByName(categoryName);
            if(category == null)
                throw new Exceptions.InvalidCategoryException(categoryName);
            ArrayList<Product> products = new ArrayList<>(category.getProducts(false));
            ArrayList<String> values = new ArrayList<>();
            for (Product product : products) {
                values.add(product.getValue(property));
            }
            Set<String> valuesSet = new HashSet<>(values);
            values.clear();
            values.addAll(valuesSet);
            return values;
        }

        private static boolean doesMatchTheProperty(Sellable product, String property, String value){
            ArrayList<String> categoryProperties = new ArrayList<>(product.getCategory().getProperties(false));
            if(!categoryProperties.contains(property))
                return true;
            else {
                return value.equals(product.getValue(property));
            }
        }

        static class SellableFilter {

            public static void available(ArrayList<Product> products, boolean available) {
                if (available)
                    products.removeIf(product -> (product.getTotalRemainingCount() == 0));
            }

            public static void minPrice(ArrayList<Product> products, double minPrice) {
                if (minPrice != 0)
                    products.removeIf(product -> product.getMaxPrice() < minPrice);
            }

            public static void maxPrice(ArrayList<Product> products, double maxPrice) {
                if (maxPrice != 0)
                    products.removeIf(product -> product.getMinPrice() > maxPrice);
            }

            public static void name(ArrayList<Product> products, String contains) {
                if (contains == null) {
                    return;
                }
                if (!contains.equals(""))
                if (!contains.isEmpty())
                    products.removeIf(product -> !(product.getName().toLowerCase().contains(contains.toLowerCase())));
            }

            public static void brand(ArrayList<Product> products, String brand) {
                if (brand == null) {
                    return;
                }
                if (!brand.equals(""))
                if (!brand.isEmpty())
                    products.removeIf(product -> !(product.getBrand().toLowerCase().contains(brand.toLowerCase())));
            }

            public static void storeName(ArrayList<Product> products, String storeName) {
                if (storeName == null) {
                    return;
                }

                if (!storeName.isEmpty()) {
                    products.removeIf(product -> !product.isSoldInStoreWithName(storeName.toLowerCase()));
                }
            }

            public static void ratingScore(ArrayList<Product> products, double minRatingScore) {
                products.removeIf(product -> product.getAverageRatingScore() < minRatingScore);
            }

            public static void property(ArrayList<Sellable> products, String property, String value){
                if(property == null || property.equals(""))
                    return;
                products.removeIf(product ->  ! Filter.doesMatchTheProperty(product, property, value));
            }
        }

        static class SubProductFilter {
            public static void available(ArrayList<SubSellable> subSellables, boolean available) {
                if (available)
                    subSellables.removeIf(subProduct -> (subProduct.getClass().getSimpleName().equals("SubProduct") && ((SubProduct)subProduct).getRemainingCount() == 0));
            }
            public static void minPrice(ArrayList<SubSellable> subSellables, double minPrice) {
                if (minPrice != 0)
                    subSellables.removeIf(subSellable -> subSellable.getPriceWithSale() < minPrice);
            }
            public static void maxPrice(ArrayList<SubSellable> subSellables, double maxPrice) {
                if (maxPrice != 0)
                    subSellables.removeIf(subSellable -> subSellable.getPriceWithSale() > maxPrice);
            }
            public static void name(ArrayList<SubSellable> subSellables, String contains) {
                if (!contains.isEmpty())
                    subSellables.removeIf(subSellable -> !(subSellable.getSellable().getName().toLowerCase().contains(contains.toLowerCase())));
            }
            public static void brand(ArrayList<SubSellable> subProducts, String brand) {
                if (!brand.isEmpty())
                    subProducts.removeIf(subProduct -> subProduct.getClass().getSimpleName().equals("SubProduct") && !(((SubProduct)subProduct).getProduct().getBrand().toLowerCase().contains(brand.toLowerCase())));
            }
            public static void storeName(ArrayList<SubSellable> subSellables, String storeName) {
                if (!storeName.isEmpty())
                    subSellables.removeIf(subSellable -> !subSellable.getSeller().getStoreName().contains(storeName.toLowerCase()));
            }
            public static void ratingScore(ArrayList<SubSellable> subSellables, double minRatingScore){
                subSellables.removeIf(subSellable -> subSellable.getSellable().getAverageRatingScore() < minRatingScore);
            }
            public static void extension(ArrayList<SubSellable> subProducts, String extension) {
                if (!extension.isEmpty())
                    subProducts.removeIf(subProduct -> subProduct.getClass().getSimpleName().equals("SubFile") && !(((SubFile)subProduct).getFile().getExtension().toLowerCase().contains(extension.toLowerCase())));
            }
        }
    }
    static class Sort {
        public static String[] productAvailableSorts() {
            String[] availableSorts = new String[6];
            availableSorts[0] = "price";
            availableSorts[1] = "rating score";
            availableSorts[2] = "name";
            availableSorts[3] = "category name";
            availableSorts[4] = "total remaining count";
            availableSorts[5] = "view count";
            return availableSorts;
        }

        public static class ProductPriceComparator implements Comparator<Product> {
            private int direction;

            public ProductPriceComparator(boolean isIncreasing) {
                direction = isIncreasing ? 1 : -1;
            }

            @Override
            public int compare(Product o1, Product o2) {
                return direction * Double.compare(o1.getMinPrice(), o2.getMinPrice());
            }
        }

        public static class ProductRatingScoreComparator implements Comparator<Product> {
            private int direction;

            public ProductRatingScoreComparator(boolean isIncreasing) {
                direction = isIncreasing ? 1 : -1;
            }

            @Override
            public int compare(Product o1, Product o2) {
                return direction * Double.compare(o1.getAverageRatingScore(), o2.getAverageRatingScore());
            }
        }

        public static class ProductNameComparator implements Comparator<Product> {
            private int direction;

            public ProductNameComparator(boolean isIncreasing) {
                direction = isIncreasing ? 1 : -1;
            }

            @Override
            public int compare(Product o1, Product o2) {
                return direction * o1.getName().compareTo(o2.getName());
            }
        }

        public static class ProductCategoryNameComparator implements Comparator<Product> {
            private int direction;

            public ProductCategoryNameComparator(boolean isIncreasing) {
                direction = isIncreasing ? 1 : -1;
            }

            @Override
            public int compare(Product o1, Product o2) {
                return direction * o1.getCategory().getName().compareTo(o2.getCategory().getName());
            }
        }

        public static class ProductRemainingCountComparator implements Comparator<Product> {
            private int direction;

            public ProductRemainingCountComparator(boolean isIncreasing) {
                direction = isIncreasing ? 1 : -1;
            }

            @Override
            public int compare(Product o1, Product o2) {
                return direction * Integer.compare(o1.getTotalRemainingCount(), o2.getTotalRemainingCount());
            }
        }

        public static class ProductViewCountComparator implements Comparator<Product> {
            private int direction;

            public ProductViewCountComparator(boolean isIncreasing) {
                direction = isIncreasing ? 1 : -1;
            }

            @Override
            public int compare(Product o1, Product o2) {
                return direction * Integer.compare(o1.getViewCount(), o2.getViewCount());
            }
        }

        public static class SubSellablePriceComparator implements Comparator<SubSellable> {
            private int direction;

            public SubSellablePriceComparator(boolean isIncreasing) {
                direction = isIncreasing ? 1 : -1;
            }

            @Override
            public int compare(SubSellable o1, SubSellable o2) {
                return direction * Double.compare(o1.getPriceWithSale(), o2.getPriceWithSale());
            }
        }

        public static class SubSellableRatingScoreComparator implements Comparator<SubSellable> {
            private int direction;

            public SubSellableRatingScoreComparator(boolean isIncreasing) {
                direction = isIncreasing ? 1 : -1;
            }

            @Override
            public int compare(SubSellable o1, SubSellable o2) {
                return direction * Double.compare(o1.getSellable().getAverageRatingScore(), o2.getSellable().getAverageRatingScore());
            }
        }

        public static class SubSellableNameComparator implements Comparator<SubSellable> {
            private int direction;

            public SubSellableNameComparator(boolean isIncreasing) {
                direction = isIncreasing ? 1 : -1;
            }

            @Override
            public int compare(SubSellable o1, SubSellable o2) {
                return direction * o1.getSellable().getName().compareTo(o2.getSellable().getName());
            }
        }

        public static class SubSellableCategoryNameComparator implements Comparator<SubSellable> {
            private int direction;

            public SubSellableCategoryNameComparator(boolean isIncreasing) {
                direction = isIncreasing ? 1 : -1;
            }

            @Override
            public int compare(SubSellable o1, SubSellable o2) {
                return direction * o1.getSellable().getCategory().getName().compareTo(o2.getSellable().getCategory().getName());
            }
        }

        public static class SubSellableRemainingCountComparator implements Comparator<SubSellable> {
            private int direction;

            public SubSellableRemainingCountComparator(boolean isIncreasing) {
                direction = isIncreasing ? 1 : -1;
            }

            @Override
            public int compare(SubSellable o1, SubSellable o2) {
                if( o1.getClass().getSimpleName().equals("SubProduct") && o2.getClass().getSimpleName().equals("SubProduct"))
                return direction * Integer.compare(((SubProduct)o1).getRemainingCount(), ((SubProduct)o2).getRemainingCount());
                else if(o1.getClass().getSimpleName().equals("SubProduct")){
                    return 1;
                }else {
                    if(o2.getClass().getSimpleName().equals("SubProduct")){
                        return -1;
                    }else
                        return 0;
                }
            }
        }

        public static class SubSellableViewCountComparator implements Comparator<SubSellable> {
            private int direction;

            public SubSellableViewCountComparator(boolean isIncreasing) {
                direction = isIncreasing ? 1 : -1;
            }

            @Override
            public int compare(SubSellable o1, SubSellable o2) {
                return direction * Integer.compare(o1.getSellable().getViewCount(), o2.getSellable().getViewCount());
            }
        }
    }
}