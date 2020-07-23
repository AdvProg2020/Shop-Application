package model.log;

public enum ShippingStatus {
    PROCESSING, SENDING, RECEIVED;

    @Override
    public String toString() {
        if (this.equals(PROCESSING)) return "Processing";
        else if (this.equals(SENDING)) return "Sending";
        else return "Received";
    }
}
