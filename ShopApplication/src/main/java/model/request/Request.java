package model.request;

import java.util.ArrayList;

public class Request {
    private static ArrayList<Request> allRequests = new ArrayList<Request>();
    private String requestId;

    public static ArrayList<Request> getAllRequests() {
        return allRequests;
    }

}
