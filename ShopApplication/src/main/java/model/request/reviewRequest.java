package model.request;

import model.Review;

public class reviewRequest extends Request {
    private Review review;
    private ReviewStatus status;

    public reviewRequest(Review review) {
        this.review = review;
    }

    public Review getReview() {
        return review;
    }

    public ReviewStatus getStatus() {
        return status;
    }

    public void setStatus(ReviewStatus status) {
        this.status = status;
    }

    public enum ReviewStatus {
        pending, accepted, declined;
    }

}
