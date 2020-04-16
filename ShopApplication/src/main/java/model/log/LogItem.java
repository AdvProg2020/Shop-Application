package model.log;

import model.SubProduct;

public class LogItem {
    private SubProduct subProduct;
    private int count;
    private int Price;
    private int salePercentage;

    public LogItem(SubProduct subProduct, int count, int price, int salePercentage) {
        this.subProduct = subProduct;
        this.count = count;
        Price = price;
        this.salePercentage = salePercentage;
    }

    public SubProduct getSubProduct() {
        return subProduct;
    }

    public int getCount() {
        return count;
    }

    public int getPrice() {
        return Price;
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
