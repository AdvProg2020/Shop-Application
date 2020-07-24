package Server.model.sellable;

import Server.model.ModelUtilities;
import Server.model.database.Database;
import Server.model.request.AddFileRequest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SubFile extends SubSellable {
    private static Map<String, SubFile> allSubFiles = new HashMap<>();
    private static int lastNum = 1;
    private String downloadPath;

    public SubFile(String fileId, String sellerId, double price, String downloadPath, Database database) {
        super(fileId, sellerId, price, database);
        this.downloadPath = downloadPath;
        if (sellableId != null)
            new AddFileRequest(null, this).updateDatabase(database);
    }

    public static List<SubFile> getAllSubFiles(boolean... suspense) {
        return ModelUtilities.getAllInstances(allSubFiles.values(), suspense);
    }

    public static SubFile getSubFileById(String subFileId, boolean... suspense) {
        return ModelUtilities.getInstanceById(allSubFiles, subFileId, suspense);
    }

    @Override
    public void initialize() {
        if (subSellableId == null)
            subSellableId = ModelUtilities.generateNewId(getClass().getSimpleName(), lastNum);
        allSubFiles.put(subSellableId, this);
        lastNum++;
        super.initialize();
    }

    public File getFile(boolean... suspense) {
        return File.getFileById(sellableId, suspense);
    }

    public String getDownloadPath() {
        return downloadPath;
    }

    public void setDownloadPath(String downloadPath) {
        this.downloadPath = downloadPath;
    }

    @Override
    public boolean isAvailable() {
        return true;
    }
}
