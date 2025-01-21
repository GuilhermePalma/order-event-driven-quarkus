package com.guilhermepalma.order_spring_boot.dto;

import lombok.Data;

import java.util.*;

@Data
public class PayloadDTO<T> {
    private List<T> data;

    public PayloadDTO(T item) {
        this.data = new ArrayList<>(Collections.singletonList(item));
    }

    public T getFirstOnData(){
        if (Objects.isNull(data) || data.isEmpty()) {
            return null;
        }

        return data.get(0);
    }
}
