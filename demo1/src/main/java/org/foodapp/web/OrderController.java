package org.foodapp.web;

import org.foodapp.Order;
import org.foodapp.OrderStatus;
import org.foodapp.exception.BadRequestException;
import org.foodapp.service.OrderService;
import org.foodapp.web.dto.OrderCreateRequest;
import org.foodapp.web.dto.UpdateOrderStatusRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public ResponseEntity<List<Order>> list() {
        return ResponseEntity.ok(orderService.list());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> get(@PathVariable long id) {
        return ResponseEntity.ok(orderService.get(id));
    }

    @PostMapping
    public ResponseEntity<Order> create(@RequestBody OrderCreateRequest request) {
        if (request.getRestaurantId() == null || request.getCustomerId() == null) {
            throw new BadRequestException("restaurantId and customerId are required");
        }
        double totalPrice = request.getTotalPrice() != null ? request.getTotalPrice() : 0.0;
        return ResponseEntity.ok(orderService.create(
                request.getRestaurantId(),
                request.getCustomerId(),
                request.getDriverId(),
                totalPrice
        ));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Order> updateStatus(@PathVariable long id, @RequestBody UpdateOrderStatusRequest request) {
        if (request.getStatus() == null || request.getStatus().isBlank()) {
            throw new BadRequestException("Status is required");
        }
        OrderStatus status;
        try {
            status = OrderStatus.valueOf(request.getStatus());
        } catch (IllegalArgumentException ex) {
            throw new BadRequestException("Unknown order status: " + request.getStatus());
        }
        return ResponseEntity.ok(orderService.updateStatus(id, status));
    }
}
