package com.guilhermepalma.order_quarkus.type;

import com.fasterxml.jackson.annotation.JsonValue;

public enum StatusType {
    PENDING("PENDING"), CONFIRMED("CONFIRMED"), SHIPPED("SHIPPED"), DELIVERED("DELIVERED");

    @JsonValue // Consider the string instead of the enum name
    private final String type;

    StatusType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return type;
    }
}
