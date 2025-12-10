package org.foodapp;

public class Admin extends User {
    private int accessLevel;

    public Admin() {
        setRole(UserRole.ADMIN);
    }

    public Admin(Long id,
                 String username,
                 String passwordHash,
                 String fullName,
                 String email,
                 String phone,
                 int accessLevel) {
        super(id, username, passwordHash, fullName, email, phone, UserRole.ADMIN);
        this.accessLevel = accessLevel;
    }

    public int getAccessLevel() {
        return accessLevel;
    }

    public void setAccessLevel(int accessLevel) {
        this.accessLevel = accessLevel;
    }
}
