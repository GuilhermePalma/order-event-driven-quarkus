package com.guilhermepalma.order_spring_boot.factory;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public class OperationSQLFactory<T, U, R extends JpaRepository<T, UUID>> implements OperationsFactory<T, U> {
    @Override
    public Operations<T, U> createOperations() {
        return new OperationsSQL<T, U, R>();
    }
}
