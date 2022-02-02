package Server.model.request;

import Server.model.account.Seller;
import Server.model.database.Database;
import Server.model.sellable.File;
import Server.model.sellable.SubFile;

public class AddFileRequest extends Request implements SellerRequest {
    private final File file;
    private final SubFile subFile;

    public AddFileRequest(File file, SubFile subFile) {
        super();
        this.file = file;
        this.subFile = subFile;
        initialize();
    }

    @Override
    public void accept() {
        if (file != null) {
            file.initialize();
            subFile.setSellableId(file.getId());
        }
        subFile.initialize();
        super.accept();
    }

    @Override
    protected boolean isInvalid() {
        if (file != null)
            return (status == RequestStatus.PENDING) && (file.getCategory() == null);

        return (status == RequestStatus.PENDING) && (subFile.getFile() == null || subFile.getSeller() == null);
    }

    public File getFile() {
        return file;
    }

    public SubFile getSubFile() {
        return subFile;
    }

    @Override
    public Seller getSeller() {
        return subFile.getSeller();
    }

    @Override
    public void updateDatabase(Database database) {
        if (file != null)
            database.createFile();
        else
            database.createSubFile();
    }
}
