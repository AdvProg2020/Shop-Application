package model.sellable;

import model.database.Database;
import model.request.AddFileRequest;
import model.request.Request;

import java.util.Map;

public class File extends Sellable {
    private static final String DEFAULT_IMAGE_PATH = "/src/main/resources/img/default-file-pic.png";
    private String extension;

    public File(String name, String extension, String infoText, String imagePath, String categoryId, Map<String, String> propertyValues, SubSellable subSellable, Database database) {
        super(name, infoText, imagePath, categoryId, propertyValues, subSellable, database);
        this.extension = extension;
        new AddFileRequest(this, (SubFile) subSellable).updateDatabase(database);
    }

    public static File getFileById(String fileId, boolean... suspense) {
        Sellable sellable = getSellableById(fileId, suspense);
        if (sellable instanceof File)
            return (File) sellable;

        return null;
    }

    public static File getProductByNameAndExtension(String name, String extension) {
        for (Sellable sellable : getSellablesByName(name)) {
            if (sellable instanceof File && ((File) sellable).getExtension().equals(extension))
                return (File) sellable;
        }

        return null;
    }

    public static boolean isFileNameAndExtensionUsed(String name, String extension) {
        if (getProductByNameAndExtension(name, extension) != null) return true;

        for (Request request : Request.getPendingRequests()) {
            if (request instanceof AddFileRequest)
                if (((AddFileRequest) request).getFile().getName().equals(name) && ((AddFileRequest) request).getFile().getExtension().equals(extension))
                    return true;
        }

        return false;
    }

    @Override
    protected String getDefaultImagePath() {
        return DEFAULT_IMAGE_PATH;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }
}
