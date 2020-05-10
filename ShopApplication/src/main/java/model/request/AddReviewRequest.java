package model.request;

import model.Review;

public class AddReviewRequest extends Request {
    private Review review;

    public AddReviewRequest(Review review) {
        super();
        this.review = review;
    }

    @Override
    public void accept() {
        super.accept();
        review.initialize();
    }

    @Override
    public String getType() {
        return "AddReview";
    }

    public Review getReview() {
        return review;
    }
}
