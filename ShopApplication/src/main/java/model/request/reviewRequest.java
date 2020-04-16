package model.request;

import model.Review;

public class reviewRequest  extends Request{
    private Review review;
    private ReviewStatus status;

    public enum ReviewStatus {
        pending, accepted, declined;
    }
}
