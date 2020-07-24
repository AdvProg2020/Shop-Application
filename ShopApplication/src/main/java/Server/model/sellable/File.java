package Server.model.sellable;

import Server.model.ModelUtilities;
import Server.model.database.Database;
import Server.model.request.AddFileRequest;
import Server.model.request.Request;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class File extends Sellable {
    private static final String DEFAULT_IMAGE_PATH = "/src/main/resources/img/default-file-pic.png";
    private static Map<String, File> allFiles = new HashMap<>();
    private static int lastNum = 1;
    private String extension;

    public File(String name, String extension, String infoText, String imagePath, String categoryId, Map<String, String> propertyValues, SubSellable subSellable, Database database) {
        super(name, infoText, imagePath, categoryId, propertyValues, subSellable, database);
        this.extension = extension;
        new AddFileRequest(this, (SubFile) subSellable).updateDatabase(database);
    }

    public static List<File> getAllFiles(boolean... suspense) {
        return ModelUtilities.getAllInstances(allFiles.values(), suspense);
    }

    public static File getFileById(String fileId, boolean... suspense) {
        return ModelUtilities.getInstanceById(allFiles, fileId, suspense);
    }

    public static List<File> getFilesByName(String name) {
        List<File> files = new ArrayList<>();
        for (File file : allFiles.values()) {
            if (!file.suspended && file.getName().equals(name))
                files.add(file);
        }

        return files;
    }

    public static File getFileByNameAndExtension(String name, String extension) {
        for (File file : getFilesByName(name)) {
            if (file.getExtension().equals(extension))
                return file;
        }

        return null;
    }

    public static boolean isFileNameAndExtensionUsed(String name, String extension) {
        if (getFileByNameAndExtension(name, extension) != null) return true;

        for (Request request : Request.getPendingRequests()) {
            if (request instanceof AddFileRequest) {
                File file = ((AddFileRequest) request).getFile();
                if (file.getName().equals(name) && file.getExtension().equals(extension))
                    return true;
            }
        }

        return false;
    }

    @Override
    public void initialize() {
        if (sellableId == null)
            sellableId = ModelUtilities.generateNewId(getClass().getSimpleName(), lastNum);
        allFiles.put(sellableId, this);
        lastNum++;
        super.initialize();
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


    public List<SubFile> getSubFiles() { // TODO: check if it works properly
        return (List<SubFile>) (List<?>) getSubSellables();
    }

    public SubFile getSubFileOfSeller(String sellerId) {
        return (SubFile) getSubSellableOfSeller(sellerId);
    }

    public List<SubFile> getSubFilesInSale() { // TODO: delete?!
        return (List<SubFile>) (List<?>) getSubSellablesInSale();
    }

    public SubFile getDefaultSubFile() {
        return (SubFile) getDefaultSubSellable();
    }
}
