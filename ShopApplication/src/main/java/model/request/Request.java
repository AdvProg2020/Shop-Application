package model.request;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public abstract class Request {
    private static HashMap<String, Request> allRequests = new HashMap<>();
    private String requestId;
    private Date date;
    private RequestStatus status;

    public Request() {
        status = RequestStatus.PENDING;
        date = new Date();
        initialize();
    }

    private static String generateNewId() {
        //TODO: implement
        return null;
    }

    public static ArrayList<Request> getAllRequests() {
        return (ArrayList<Request>) allRequests.values();
    }

    public static Request getRequestById(String requestId) {
        return allRequests.get(requestId);
    }

    public void initialize() {
        if (requestId == null) {
            requestId = generateNewId();
        }
        allRequests.put(requestId, this);
    }

    public abstract String getType();

    public String getRequestId() {
        return requestId;
    }

    public Date getDate() {
        return date;
    }

    public RequestStatus getStatus() {
        return status;
    }

    public void accept() {
        status = RequestStatus.ACCEPTED;
    }

    public void decline() {
        status = RequestStatus.DECLINED;
    }

    public enum RequestStatus {
        PENDING, ACCEPTED, DECLINED
    }
}
