package model.request;

import model.Review;

public class ReviewRequest extends Request {
    private Review review;
    private ReviewStatus status;

    public ReviewRequest(Review review) {
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
        pending, accepted, declined
    }

}
