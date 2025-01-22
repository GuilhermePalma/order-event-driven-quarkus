package com.guilhermepalma.order_spring_boot.factory.sql;

import com.guilhermepalma.order_spring_boot.factory.Operations;
import com.guilhermepalma.order_spring_boot.factory.OperationsFactory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public class OperationSQLFactory<T, Q, R extends JpaRepository<T, UUID>> implements OperationsFactory<T, Q> {

    private final R repository;

    public OperationSQLFactory(R repository) {
        this.repository = repository;
    }

    @Override
    public Operations<T, Q> createOperations() {
        return new OperationsSQL<T, Q, R>(repository);
    }
}
