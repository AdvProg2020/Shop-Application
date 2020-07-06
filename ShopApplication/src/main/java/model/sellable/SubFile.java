package model.sellable;

import model.database.Database;
import model.request.AddFileRequest;

public class SubFile extends SubSellable {
    private String downloadPath;

    public SubFile(String fileId, String sellerId, double price, String downloadPath, Database database) {
        super(fileId, sellerId, price, database);
        this.downloadPath = downloadPath;
        if (sellableId != null)
            new AddFileRequest(null, this).updateDatabase(database);
    }

    public static SubFile getSubFileById(String subFileId, boolean... suspense) {
        SubSellable subSellable = getSubSellableById(subFileId, suspense);
        if (subSellable instanceof SubFile)
            return (SubFile) subSellable;

        return null;
    }

    public String getDownloadPath() {
        return downloadPath;
    }

    public void setDownloadPath(String downloadPath) {
        this.downloadPath = downloadPath;
    }

    public File getFile(boolean... suspense) {
        return File.getFileById(sellableId, suspense);
    }

    @Override
    public boolean isAvailable() {
        return true;
    }
}
