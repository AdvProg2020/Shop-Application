package model.request;

import model.ModelBasic;
import model.ModelUtilities;
import model.ModelUtilities.ModelOnly;
import model.database.Database;

import java.util.*;

public abstract class Request implements ModelBasic {
    private static Map<String, Request> allRequests = new HashMap<>();
    private static int lastNum = 1;
    protected String requestId;
    protected Date date;
    protected RequestStatus status;

    public Request() {
        status = RequestStatus.PENDING;
        date = new Date();
        initialize();
    }

    public static List<Request> getPendingRequests() {
        return ModelUtilities.getAllInstances(allRequests.values());
    }

    public static List<Request> getRequestArchive() {
        ArrayList<Request> archive = new ArrayList<>(allRequests.values());
        archive.removeAll(getPendingRequests());

        return archive;
    }

    public static Request getRequestById(String requestId) {
        return ModelUtilities.getInstanceById(allRequests, requestId);
    }

    @Override
    public void initialize() {
        if (requestId == null)
            requestId = ModelUtilities.generateNewId(Request.class.getSimpleName(), lastNum);
        allRequests.put(requestId, this);
        lastNum++;
    }

    @ModelOnly
    public void terminate() {
        allRequests.remove(requestId);
    }

    @Override
    public boolean isSuspended() {
        if (status != RequestStatus.PENDING) return true;
        boolean invalid = isInvalid();
        if (invalid)
            terminate();

        return invalid;
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
