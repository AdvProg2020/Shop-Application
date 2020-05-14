package model.request;

import model.Initializable;

import java.util.*;

public abstract class Request implements Initializable {
    private static Map<String, Request> allRequests = new HashMap<>();
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

    public static List<Request> getAllRequests() {
        return new ArrayList<>(allRequests.values());
    }

    public static Request getRequestById(String requestId) {
        return allRequests.get(requestId);
    }

    @Override
    public void initialize() {
        if (requestId == null) {
            requestId = generateNewId();
        }
        allRequests.put(requestId, this);
    }

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

}
