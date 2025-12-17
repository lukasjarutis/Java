package org.foodapp.service;

import org.foodapp.Customer;
import org.foodapp.Driver;
import org.foodapp.Order;
import org.foodapp.OrderStatus;
import org.foodapp.Restaurant;
import org.foodapp.UserRole;
import org.foodapp.exception.BadRequestException;
import org.foodapp.repository.OrderRepository;
import org.foodapp.repository.RestaurantRepository;
import org.foodapp.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final RestaurantRepository restaurantRepository;
    private final UserRepository userRepository;

    public OrderService(OrderRepository orderRepository,
                        RestaurantRepository restaurantRepository,
                        UserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.restaurantRepository = restaurantRepository;
        this.userRepository = userRepository;
    }

    public List<Order> list() {
        return orderRepository.findAll();
    }

    public Order get(long id) {
        return orderRepository.findById(id);
    }

    public Order create(long restaurantId, long customerId, Long driverId, double totalPrice) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new org.foodapp.exception.NotFoundException("Restaurant not found: " + restaurantId));
        var customerRow = userRepository.findById(customerId)
                .orElseThrow(() -> new org.foodapp.exception.NotFoundException("Customer not found: " + customerId));

        Order order = new Order();
        order.setRestaurant(restaurant);

        Customer customer = new Customer();
        customer.setId(customerRow.getId());
        customer.setUsername(customerRow.getUsername());
        order.setCustomer(customer);

        if (driverId != null) {
            var driverRow = userRepository.findById(driverId)
                    .orElseThrow(() -> new org.foodapp.exception.NotFoundException("Driver not found: " + driverId));
            if (UserRole.valueOf(driverRow.getRole()) != UserRole.DRIVER) {
                throw new BadRequestException("User " + driverId + " is not a driver");
            }
            Driver driver = new Driver();
            driver.setId(driverRow.getId());
            driver.setUsername(driverRow.getUsername());
            order.setDriver(driver);
        }

        order.setStatus(OrderStatus.CREATED);
        order.setTotalPrice(totalPrice);
        order.setCreatedAt(LocalDateTime.now());
        return orderRepository.create(order);
    }

    public Order updateStatus(long id, OrderStatus status, Long driverId) {
        if (status == null) {
            throw new BadRequestException("Order status is required");
        }
        if (driverId != null) {
            var driverRow = userRepository.findById(driverId)
                    .orElseThrow(() -> new org.foodapp.exception.NotFoundException("Driver not found: " + driverId));
            if (UserRole.valueOf(driverRow.getRole()) != UserRole.DRIVER) {
                throw new BadRequestException("User " + driverId + " is not a driver");
            }
        }
        return orderRepository.updateStatus(id, status, driverId);
    }
}
