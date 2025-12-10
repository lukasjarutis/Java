package org.foodapp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Order {

    private Long id;
    private LocalDateTime createdAt;
    private OrderStatus status;
    private double totalPrice;
    private double dynamicPriceFactor;
    private int loyaltyPointsUsed;
    private int loyaltyPointsEarned;

    private Customer customer;
    private Restaurant restaurant;
    private Driver driver;

    private List<OrderItem> items = new ArrayList<>();
    private List<ChatMessage> messages = new ArrayList<>();
    private List<Review> reviews = new ArrayList<>();

    public Order() {
        this.createdAt = LocalDateTime.now();
        this.status = OrderStatus.CREATED;
    }

    public Order(Long id,
                 Customer customer,
                 Restaurant restaurant) {
        this();
        this.id = id;
        this.customer = customer;
        this.restaurant = restaurant;
    }

    public void addItem(OrderItem item) {
        items.add(item);
        item.setOrder(this);
        recalculateTotal();
    }

    public void removeItem(OrderItem item) {
        items.remove(item);
        item.setOrder(null);
        recalculateTotal();
    }

    public boolean removeItemByMenuItemId(Long menuItemId) {
        if (menuItemId == null) {
            return false;
        }

        boolean removed = items.removeIf(i ->
                i.getMenuItem() != null
                        && menuItemId.equals(i.getMenuItem().getId())
        );

        if (removed) {
            recalculateTotal();
        }

        return removed;
    }

    public void addMessage(ChatMessage message) {
        messages.add(message);
        message.setOrder(this);
    }

    public void addReview(Review review) {
        reviews.add(review);
        review.setOrder(this);
    }

    private void recalculateTotal() {
        double sum = 0.0;
        for (OrderItem item : items) {
            sum += item.getPriceAtOrderTime() * item.getQuantity();
        }
        this.totalPrice = sum * (dynamicPriceFactor == 0.0 ? 1.0 : dynamicPriceFactor);
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public double getDynamicPriceFactor() {
        return dynamicPriceFactor;
    }

    public int getLoyaltyPointsUsed() {
        return loyaltyPointsUsed;
    }

    public int getLoyaltyPointsEarned() {
        return loyaltyPointsEarned;
    }

    public Customer getCustomer() {
        return customer;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public Driver getDriver() {
        return driver;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public List<ChatMessage> getMessages() {
        return messages;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public void setDynamicPriceFactor(double dynamicPriceFactor) {
        this.dynamicPriceFactor = dynamicPriceFactor;
        recalculateTotal();
    }

    public void setLoyaltyPointsUsed(int loyaltyPointsUsed) {
        this.loyaltyPointsUsed = loyaltyPointsUsed;
    }

    public void setLoyaltyPointsEarned(int loyaltyPointsEarned) {
        this.loyaltyPointsEarned = loyaltyPointsEarned;
    }

    public void setCustomer(Customer customer) {
        enforceParticipantsAreMutable(this.customer, customer);
        this.customer = customer;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public void setDriver(Driver driver) {
        enforceDriverAssignmentAllowed(driver);
        enforceParticipantsAreMutable(this.driver, driver);
        this.driver = driver;
    }

    public void setItems(List<OrderItem> items) {
        this.items = items;
        recalculateTotal();
    }

    public void setMessages(List<ChatMessage> messages) {
        this.messages = messages;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    private void enforceParticipantsAreMutable(User current, User incoming) {
        if (current == null && incoming == null) {
            return;
        }

        if (isPickedUpOrLater()
                && !isSameUser(current, incoming)) {
            throw new IllegalStateException(
                    "Negalima keisti pirkėjo ar vairuotojo po to, kai užsakymas paimtas"
            );
        }
    }

    private boolean isPickedUpOrLater() {
        return status != null && status.ordinal() >= OrderStatus.PICKED_UP.ordinal();
    }

    private boolean isSameUser(User current, User incoming) {
        if (current == incoming) {
            return true;
        }
        if (current == null || incoming == null) {
            return false;
        }
        return current.getId() != null
                && current.getId().equals(incoming.getId());
    }

    private void enforceDriverAssignmentAllowed(Driver driver) {
        if (driver == null) {
            return;
        }

        boolean isNewOrder = id == null;
        if (isNewOrder && items.isEmpty()) {
            throw new IllegalStateException(
                    "Negalima priskirti vairuotojo tuščiam užsakymui"
            );
        }
    }
}
