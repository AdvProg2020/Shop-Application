package model.request;

import model.account.Seller;
import model.database.Database;
import model.sellable.File;
import model.sellable.SubFile;

public class EditFileRequest extends Request implements SellerRequest {
    private String subFileId;
    private Field field;
    private String newValue;
    private String oldValue;

    public EditFileRequest(String subFileId, Field field, String newValue) {
        super();
        this.subFileId = subFileId;
        this.field = field;
        this.newValue = newValue;
        initialize();
    }

    @Override
    public void accept() {
        setOldValue();
        SubFile subFile = SubFile.getSubFileById(subFileId);
        File file = subFile.getFile();
        switch (field) {
            case NAME:
                file.setName(newValue);
                break;
            case EXTENSION:
                file.setExtension(newValue);
                break;
            case INFO_TEXT:
                file.setInfoText(newValue);
                break;
            case IMAGE_PATH:
                file.setImagePath(newValue);
                break;
            case SUB_PRICE:
                subFile.setPrice(Double.parseDouble(newValue));
                break;
            case SUB_PATH:
                subFile.setDownloadPath(newValue);
                break;
            case PROPERTY:
                String[] data = newValue.split(",");
                file.setProperty(data[0], data[1]);
        }
        super.accept();
    }

    @Override
    public void decline() {
        setOldValue();
        super.decline();
    }

    @Override
    protected boolean isInvalid() {
        return (status == RequestStatus.PENDING) && (getSubFile() == null);
    }

    public SubFile getSubFile() {
        return SubFile.getSubFileById(subFileId);
    }

    @Override
    public Seller getSeller() {
        return getSubFile().getSeller();
    }

    public Field getField() {
        return field;
    }

    public String getNewValue() {
        return newValue;
    }

    public String getOldValue() {
        if (!suspended)
            setOldValue();

        return oldValue;
    }

    private void setOldValue() {
        SubFile subFile = SubFile.getSubFileById(subFileId);
        File file = subFile.getFile();
        switch (field) {
            case NAME:
                oldValue = file.getName();
                break;
            case EXTENSION:
                oldValue = file.getExtension();
                break;
            case INFO_TEXT:
                oldValue = file.getInfoText();
                break;
            case IMAGE_PATH:
                oldValue = file.getImagePath();
                break;
            case SUB_PRICE:
                oldValue = String.valueOf(subFile.getRawPrice());
                break;
            case SUB_PATH:
                oldValue = subFile.getDownloadPath();
                break;
            case PROPERTY:
                String[] data = newValue.split(",");
                oldValue = data[0] + "," + file.getValue(data[0]);
        }
    }

    @Override
    public void updateDatabase(Database database) {
        if (field.toString().startsWith("SUB"))
            database.editSubSellable();
        else
            database.editSellable();
    }

    public enum Field {
        NAME,
        EXTENSION,
        INFO_TEXT,
        IMAGE_PATH,
        PROPERTY,
        SUB_PRICE,
        SUB_PATH
    }
}
