package com.guilhermepalma.order_quarkus.factory.sql;

import com.guilhermepalma.order_quarkus.factory.Operations;
import com.guilhermepalma.order_quarkus.factory.OperationsFactory;
import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.UUID;

@RequiredArgsConstructor
public class OperationSQLFactory<T, Q, R extends JpaRepository<T, UUID>> implements OperationsFactory<T, Q> {

    @Inject
    final R repository;

    public Operations<T, Q> createOperations() {
        return new OperationsSQL<T, Q, R>(repository);
    }
}
