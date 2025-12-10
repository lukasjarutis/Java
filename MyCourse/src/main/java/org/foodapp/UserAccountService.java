package org.foodapp;

/**
 * Service class that simulates customer and driver registration via the mobile app
 * and allows updating their profile information.
 */
public class UserAccountService {

    public Customer registerCustomer(String username,
                                     String passwordHash,
                                     String fullName,
                                     String email,
                                     String phone,
                                     String defaultAddress) {
        Customer customer = new Customer();
        populateCommonFields(customer, username, passwordHash, fullName, email, phone);
        customer.setDefaultAddress(defaultAddress);
        return customer;
    }

    public Driver registerDriver(String username,
                                 String passwordHash,
                                 String fullName,
                                 String email,
                                 String phone,
                                 String vehicleNumber) {
        Driver driver = new Driver();
        populateCommonFields(driver, username, passwordHash, fullName, email, phone);
        driver.setVehicleNumber(requireNotBlank(vehicleNumber, "Transporto priemonės numeris"));
        driver.setCurrentStatus(DriverStatus.OFFLINE);
        return driver;
    }

    public void updateContactInfo(User user,
                                  String fullName,
                                  String email,
                                  String phone) {
        if (user == null) {
            throw new IllegalArgumentException("Naudotojas privalo būti nurodytas");
        }
        user.setFullName(requireNotBlank(fullName, "Vardas ir pavardė"));
        user.setEmail(requireNotBlank(email, "El. paštas"));
        user.setPhone(requireNotBlank(phone, "Telefono numeris"));
    }

    private void populateCommonFields(User user,
                                      String username,
                                      String passwordHash,
                                      String fullName,
                                      String email,
                                      String phone) {
        user.setUsername(requireNotBlank(username, "Prisijungimo vardas"));
        user.setPasswordHash(requireNotBlank(passwordHash, "Slaptažodis"));
        updateContactInfo(user, fullName, email, phone);
    }

    private String requireNotBlank(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(fieldName + " negali būti tuščias");
        }
        return value.trim();
    }
}
