package Server.model.request;

import Server.model.Auction;
import Server.model.account.Seller;
import Server.model.database.Database;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class EditAuctionRequest extends Request implements SellerRequest {
    private final String auctionId;
    private final Field field;
    private final String newValue;
    private String oldValue;


    public EditAuctionRequest(String auctionId, String newValue, Field field) {
        super();
        this.auctionId = auctionId;
        this.field = field;
        this.newValue = newValue;
        initialize();
    }

    @Override
    public void accept() {
        setOldValue();
        Auction auction = Auction.getAuctionById(auctionId);
        try {
            SimpleDateFormat parser = new SimpleDateFormat("yy-MM-dd");
            switch (field) {
                case START_DATE:
                    auction.setStartDate(parser.parse(newValue));
                    break;
                case END_DATE:
                    auction.setEndDate(parser.parse(newValue));
                    break;
            }
        } catch (ParseException e) {
            e.printStackTrace();
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
        return (status == RequestStatus.PENDING) && (getAuction() == null);
    }

    public Auction getAuction() {
        return Auction.getAuctionById(auctionId);
    }

    @Override
    public Seller getSeller() {
        return getAuction().getSeller();
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
        Auction auction = Auction.getAuctionById(auctionId);
        SimpleDateFormat parser = new SimpleDateFormat("yy-MM-dd");
        switch (field) {
            case START_DATE:
                oldValue = parser.format(auction.getStartDate());
                break;
            case END_DATE:
                oldValue = parser.format(auction.getEndDate());
                break;
        }
    }

    @Override
    public void updateDatabase(Database database) {
        database.editAuction();
    }

    public enum Field {
        START_DATE,
        END_DATE
    }
}
