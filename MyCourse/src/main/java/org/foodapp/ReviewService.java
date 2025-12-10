package org.foodapp;

/**
 * Service class responsible for creating and attaching reviews to orders.
 */
public class ReviewService {

    /**
     * Adds a restaurant review for the provided order.
     *
     * @param order     order that was fulfilled by the restaurant
     * @param author    user leaving the review
     * @param rating    score between 1 and 5
     * @param comment   free-form feedback
     * @return created review linked to the order and restaurant
     */
    public Review leaveRestaurantReview(Order order,
                                        User author,
                                        int rating,
                                        String comment) {
        enforceOrder(order);
        if (order.getRestaurant() == null) {
            throw new IllegalStateException("Restoranas nenustatytas užsakymui");
        }

        Review review = createBaseReview(order, author, rating, comment, ReviewTargetType.RESTAURANT);
        review.setRestaurantTarget(order.getRestaurant());
        order.addReview(review);
        return review;
    }

    /**
     * Adds a driver review for the provided order.
     *
     * @param order     delivered order
     * @param author    user leaving the review
     * @param rating    score between 1 and 5
     * @param comment   free-form feedback
     * @return created review linked to the order and driver
     */
    public Review leaveDriverReview(Order order,
                                    User author,
                                    int rating,
                                    String comment) {
        enforceOrder(order);
        if (order.getDriver() == null) {
            throw new IllegalStateException("Vairuotojas nenustatytas užsakymui");
        }

        Review review = createBaseReview(order, author, rating, comment, ReviewTargetType.DRIVER);
        review.setDriverTarget(order.getDriver());
        order.addReview(review);
        return review;
    }

    private void enforceOrder(Order order) {
        if (order == null) {
            throw new IllegalArgumentException("Užsakymas privalo būti nurodytas");
        }
    }

    private Review createBaseReview(Order order,
                                    User author,
                                    int rating,
                                    String comment,
                                    ReviewTargetType targetType) {
        if (author == null) {
            throw new IllegalArgumentException("Atsiliepimo autorius privalo būti nurodytas");
        }

        if (rating < 1 || rating > 5) {
            throw new IllegalArgumentException("Įvertinimas turi būti tarp 1 ir 5");
        }

        Review review = new Review();
        review.setOrder(order);
        review.setFromUser(author);
        review.setRating(rating);
        review.setComment(comment != null ? comment.trim() : "");
        review.setTargetType(targetType);
        return review;
    }
}
