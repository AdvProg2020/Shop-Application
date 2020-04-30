package model.request;

import model.Category;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class Request {
    private static HashMap<String, Request> allRequests = new HashMap<>();
    private String requestId;

    public static ArrayList<Request> getAllRequests() {
        return (ArrayList<Request>) allRequests.values();
    }

    public String getRequestId() {
        return requestId;
    }

    public static Request getRequestById(String requestId) {
        return allRequests.get(requestId);
    }
}
