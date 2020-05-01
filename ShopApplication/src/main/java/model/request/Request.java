package model.request;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class Request {
    private static HashMap<String, Request> allRequests = new HashMap<>();
    private String requestId;

    private static String generateNewId() {
        //TODO: implement
        return null;
    }

    public void initialize() {
        if (requestId == null) {
            requestId = generateNewId();
        }
        allRequests.put(requestId, this);
    }

    public static ArrayList<Request> getAllRequests() {
        return (ArrayList<Request>) allRequests.values();
    }

    public abstract String getType();

    public String getRequestId() {
        return requestId;
    }

    public static Request getRequestById(String requestId) {
        return allRequests.get(requestId);
    }
}
