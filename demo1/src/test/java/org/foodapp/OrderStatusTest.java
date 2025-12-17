package org.foodapp;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class OrderStatusTest {

    @Test
    void fromStringHandlesDifferentCasingAndSeparators() {
        assertEquals(OrderStatus.PICKED_UP, OrderStatus.fromString("picked_up"));
        assertEquals(OrderStatus.DELIVERING, OrderStatus.fromString("Delivering"));
        assertEquals(OrderStatus.READY_FOR_PICKUP, OrderStatus.fromString("ready for pickup"));
        assertEquals(OrderStatus.READY_FOR_PICKUP, OrderStatus.fromString("ready-for-pickup"));
    }

    @Test
    void fromStringReturnsNullForUnknownValue() {
        assertNull(OrderStatus.fromString("finished"));
        assertNull(OrderStatus.fromString(""));
        assertNull(OrderStatus.fromString(null));
    }
}
