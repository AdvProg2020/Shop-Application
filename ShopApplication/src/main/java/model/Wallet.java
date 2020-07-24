package model;

import model.ModelUtilities.ModelOnly;
import model.account.Account;
import model.account.Customer;
import model.account.Seller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Wallet implements ModelBasic {
    private static Map<String, Wallet> allWallets = new HashMap<>();
    private static int lastNum = 1;
    private static double minBalance = 0;
    private String walletId;
    private String accountId;
    private double balance;

    public Wallet(String accountId, double balance) {
        this.accountId = accountId;
        this.balance = balance;
        initialize();
    }

    public static List<Wallet> getAllWallets() {
        return ModelUtilities.getAllInstances(allWallets.values(), false);
    }

    public static Wallet getWalletById(String walletId) {
        return ModelUtilities.getInstanceById(allWallets, walletId, false);
    }

    @Override
    public void initialize() {
        if (walletId == null)
            walletId = ModelUtilities.generateNewId(getClass().getSimpleName(), lastNum);
        allWallets.put(walletId, this);
        lastNum++;

        Account account = getAccount();
        if (account instanceof Customer)
            ((Customer) account).setWallet(walletId);
        else
            ((Seller) account).setWallet(walletId);
    }

    @ModelOnly
    public void terminate() {
        allWallets.remove(walletId);
    }

    @Override
    public boolean isSuspended() {
        return false;
    }

    @Override
    public String getId() {
        return walletId;
    }

    public double getBalance() {
        return balance;
    }

    public static double getMinBalance() {
        return minBalance;
    }

    public Account getAccount() {
        return Account.getAccountById(accountId);
    }

    public static void setMinBalance(double minBalance) {
        Wallet.minBalance = minBalance;
    }

    public void changeBalance(double changeAmount) {
        balance += changeAmount;
        if (balance < minBalance)
            balance = minBalance;
    }
}
