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
    protected boolean suspended;

    public Request() {
        status = RequestStatus.PENDING;
        date = new Date();
        suspended = false;
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

        if (!suspended && this instanceof SellerRequest)
            ((SellerRequest) this).getSeller().addRequest(requestId);
    }

    @ModelOnly
    public void terminate() {
        suspend();
        allRequests.remove(requestId);
    }

    @Override
    public boolean isSuspended() {
        if (suspended) return true;
        if (isInvalid()) {
            terminate();
            return true;
        }

        return false;
    }

    public void suspend() {
        if (this instanceof SellerRequest)
            ((SellerRequest) this).getSeller().removeRequest(requestId);

        suspended = true;
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
        suspend();
    }

    public void decline() {
        status = RequestStatus.DECLINED;
        suspend();
    }

    public abstract void updateDatabase(Database database);

}
