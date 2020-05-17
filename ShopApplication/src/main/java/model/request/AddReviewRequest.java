package model.request;

import model.Review;
import model.database.Database;

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
    protected boolean isInvalid() {
        return (review.getProduct() == null);
    }

    public Review getReview() {
        return review;
    }

    @Override
    public void updateDatabase(Database database) {
        database.addReview();
    }
}
