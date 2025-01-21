package com.guilhermepalma.order_spring_boot.dto;

import lombok.Data;

import java.util.*;

@Data
public class PayloadDTO<T> {
    private Set<T> data;

    public PayloadDTO(T item) {
        this.data = new HashSet<>(Collections.singleton(item));
    }
}
