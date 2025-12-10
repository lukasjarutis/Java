package org.foodapp;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserAccountServiceTest {

    private final UserAccountService service = new UserAccountService();

    @Test
    void registersCustomerWithEditableProfile() {
        Customer customer = service.registerCustomer(
                "client",
                "hash123",
                "Vardenis Pavardenis",
                "client@example.com",
                "+37060000000",
                "Vilnius"
        );

        assertEquals(UserRole.CUSTOMER, customer.getRole());
        assertEquals("client", customer.getUsername());
        assertEquals("Vilnius", customer.getDefaultAddress());

        service.updateContactInfo(customer, "Naujas Vardas", "new@example.com", "+37061111111");

        assertEquals("Naujas Vardas", customer.getFullName());
        assertEquals("new@example.com", customer.getEmail());
        assertEquals("+37061111111", customer.getPhone());
    }

    @Test
    void registersDriverWithVehicleNumber() {
        Driver driver = service.registerDriver(
                "driver",
                "hash456",
                "Vairuotojas",
                "driver@example.com",
                "+37062222222",
                "ABC123"
        );

        assertEquals(UserRole.DRIVER, driver.getRole());
        assertEquals("ABC123", driver.getVehicleNumber());
        assertEquals(DriverStatus.OFFLINE, driver.getCurrentStatus());
    }

    @Test
    void rejectsBlankInputsOnUpdate() {
        Driver driver = new Driver();
        assertThrows(IllegalArgumentException.class,
                () -> service.updateContactInfo(driver, " ", "", ""));
    }
}
