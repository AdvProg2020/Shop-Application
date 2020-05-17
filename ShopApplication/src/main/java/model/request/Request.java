package model.request;

import model.BasicMethods;
import model.ModelBasic;
import model.database.Database;

import java.util.*;

public abstract class Request implements ModelBasic {
    private static Map<String, Request> allRequests = new HashMap<>();
    private static int lastNum = 1;
    private String requestId;
    private Date date;
    private RequestStatus status;

    public Request() {
        status = RequestStatus.PENDING;
        date = new Date();
        initialize();
    }

    private static void filterRequests() {
        allRequests.values().removeIf(request -> (request.status == RequestStatus.PENDING) && request.isInvalid());
    }

    public static List<Request> getPendingRequests() {
        filterRequests();
        return BasicMethods.getInstances(allRequests.values());
    }

    public static List<Request> getRequestArchive() {
        ArrayList<Request> archive = new ArrayList<>(allRequests.values());
        archive.removeAll(getPendingRequests());
        return archive;
    }

    public static Request getRequestById(String requestId) {
        return BasicMethods.getInstanceById(allRequests, requestId);

    }

    @Override
    public void initialize() {
        if (requestId == null)
            requestId = BasicMethods.generateNewId(getClass().getSimpleName(), lastNum);
        allRequests.put(requestId, this);
        lastNum++;
    }

    @Override
    public boolean isSuspended() {
        return (status != RequestStatus.PENDING || isInvalid());
    }

    @Override
    public String getId() {
        return requestId;
    }

    protected abstract boolean isInvalid();

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

    public abstract void updateDatabase(Database database);

}
