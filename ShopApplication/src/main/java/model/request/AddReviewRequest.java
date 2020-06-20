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
        review.initialize();
        super.accept();
    }

    @Override
    protected boolean isInvalid() {
        return (status == RequestStatus.PENDING) && (review.getProduct() == null);
    }

    public Review getReview() {
        return review;
    }

    @Override
    public void updateDatabase(Database database) {
        database.addReview();
    }
}
