package org.foodapp;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ReviewServiceTest {

    private final ReviewService service = new ReviewService();

    @Test
    void leavesRestaurantReviewWhenRestaurantPresent() {
        Restaurant restaurant = new Restaurant();
        restaurant.setName("Bistro");

        Customer author = new Customer();
        author.setUsername("client");

        Order order = new Order();
        order.setRestaurant(restaurant);

        Review review = service.leaveRestaurantReview(order, author, 5, "Puiku");

        assertEquals(ReviewTargetType.RESTAURANT, review.getTargetType());
        assertEquals(restaurant, review.getRestaurantTarget());
        assertEquals(author, review.getFromUser());
        assertTrue(order.getReviews().contains(review));
    }

    @Test
    void preventsReviewWithInvalidRating() {
        Order order = new Order();
        order.setRestaurant(new Restaurant());
        Customer author = new Customer();

        assertThrows(IllegalArgumentException.class,
                () -> service.leaveRestaurantReview(order, author, 0, "Blogai"));
    }

    @Test
    void preventsDriverReviewWithoutAssignedDriver() {
        Order order = new Order();
        order.setRestaurant(new Restaurant());
        Customer author = new Customer();

        assertThrows(IllegalStateException.class,
                () -> service.leaveDriverReview(order, author, 4, "Viskas tvarkoje"));
    }
}
