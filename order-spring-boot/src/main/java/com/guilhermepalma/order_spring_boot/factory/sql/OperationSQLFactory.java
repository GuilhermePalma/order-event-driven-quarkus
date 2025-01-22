package com.guilhermepalma.order_spring_boot.factory.sql;

import com.guilhermepalma.order_spring_boot.factory.Operations;
import com.guilhermepalma.order_spring_boot.factory.OperationsFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class OperationSQLFactory<T, Q, R extends JpaRepository<T, UUID>> implements OperationsFactory<T, Q> {

    private final R repository;

    @Override
    public Operations<T, Q> createOperations() {
        return new OperationsSQL<T, Q, R>(repository);
    }
}
