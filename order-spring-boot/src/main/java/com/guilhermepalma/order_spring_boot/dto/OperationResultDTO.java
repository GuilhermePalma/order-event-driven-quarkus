package com.guilhermepalma.order_spring_boot.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.*;
import java.util.stream.Collectors;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class OperationResultDTO<T> {
    private List<T> data;
    private Long total;
    private String errors;
    private Long totalErrors;

    /**
     * Instance class with Unique Iten
     */
    public OperationResultDTO(T data) {
        this.data = Collections.singletonList(data);
        this.total = 1L;
    }

    /**
     * Instance class with Many Items
     */
    public OperationResultDTO(Collection<T> data, long total) {
        this.data = new ArrayList<>(data);
        this.total = Objects.isNull(total) ? data.size() : total;
    }

    /**
     * Instance class with Unique Iten and Erros
     */
    public OperationResultDTO(T data, long total, Set<String> errors) {
        this.data = Collections.singletonList(data);
        this.total = Objects.isNull(total) ? 1L : total;
        if (!Objects.isNull(errors) && !errors.isEmpty()) {
            this.errors = String.join("\n", errors);
            this.totalErrors = (long) errors.size();
        }
    }

    /**
     * Instance class with Many Items
     */
    public OperationResultDTO(Collection<T> data, long total, Set<String> errors) {
        this.data = new ArrayList<>(data);
        this.total = Objects.isNull(total) ? data.size() : total;
        if (!Objects.isNull(errors) && !errors.isEmpty()) {
            this.errors = String.join("\n", errors);
            this.totalErrors = (long) errors.size();
        }
    }

    /**
     * Instance class with Exception Iten
     */
    public OperationResultDTO(Exception ex) {
        this.errors = ex.getLocalizedMessage();
        this.totalErrors = 1L;
    }

}
