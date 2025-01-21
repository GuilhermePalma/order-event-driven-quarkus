package com.guilhermepalma.order_spring_boot.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.Objects;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class OperationResultDTO<T> {
    private Set<T> data;
    private Long total;
    private String errors;
    private Long totalErrors;

    /**
     * Instance class with Unique Item
     */
    public OperationResultDTO(T data) {
        this.data = Collections.singleton(data);
        this.total = Objects.isNull(total) ? null : 1L;
    }

    /**
     * Instance class with Exception Item
     */
    public OperationResultDTO(Exception ex) {
        this.errors = ex.getLocalizedMessage();
        this.totalErrors = 1L;
    }

}
