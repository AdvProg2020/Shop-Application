package Server.model.request;

import Server.model.Review;
import Server.model.database.Database;

public class AddReviewRequest extends Request {
    private Review review;

    public AddReviewRequest(Review review) {
        super();
        this.review = review;
        initialize();
    }

    @Override
    public void accept() {
        review.initialize();
        super.accept();
    }

    @Override
    protected boolean isInvalid() {
        return (status == RequestStatus.PENDING) && (review.getSellable() == null);
    }

    public Review getReview() {
        return review;
    }

    @Override
    public void updateDatabase(Database database) {
        database.addReview();
    }
}
