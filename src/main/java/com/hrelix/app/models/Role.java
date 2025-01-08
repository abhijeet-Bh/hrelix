package com.hrelix.app.models;

// Role E-Num
public enum Role {
    EMPLOYEE,
    HR,
    ADMIN,
    MANAGER;

    @Override
    public String toString() {
        return "ROLE_" + this.name();
    }
}