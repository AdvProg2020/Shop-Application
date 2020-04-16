package model.log;

import model.SubProduct;

public class LogItem {
    private SubProduct subProduct;
    private int count;
    private int price;
    private int salePercentage;

    public LogItem(SubProduct subProduct, int count, int price, int salePercentage) {
        this.subProduct = subProduct;
        this.count = count;
        this.price = price;
        this.salePercentage = salePercentage;
    }

    public SubProduct getSubProduct() {
        return subProduct;
    }

    public int getCount() {
        return count;
    }

    public int getPrice() {
        return price;
    }

    public int getSalePercentage() {
        return salePercentage;
    }

    public void addLogToDatabase() {
    }

    public void removeLogFromDatabase() {
    }

    public void loadDatabase() {
    }
}
