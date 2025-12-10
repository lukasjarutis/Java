package org.foodapp;

public class OrderItem {

    private Long id;
    private int quantity;
    private double priceAtOrderTime;

    private MenuItem menuItem;
    private Order order;

    public OrderItem() {
    }

    public OrderItem(Long id,
                     MenuItem menuItem,
                     int quantity,
                     double priceAtOrderTime) {
        this.id = id;
        this.menuItem = menuItem;
        this.quantity = quantity;
        this.priceAtOrderTime = priceAtOrderTime;
    }

    public Long getId() {
        return id;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getPriceAtOrderTime() {
        return priceAtOrderTime;
    }

    public MenuItem getMenuItem() {
        return menuItem;
    }

    public Order getOrder() {
        return order;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setPriceAtOrderTime(double priceAtOrderTime) {
        this.priceAtOrderTime = priceAtOrderTime;
    }

    public void setMenuItem(MenuItem menuItem) {
        this.menuItem = menuItem;
    }

    public void setOrder(Order order) {
        this.order = order;
    }
}
