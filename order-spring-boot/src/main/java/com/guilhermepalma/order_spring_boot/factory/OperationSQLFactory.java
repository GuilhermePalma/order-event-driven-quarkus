package com.guilhermepalma.order_spring_boot.factory;

import com.guilhermepalma.order_spring_boot.dto.command.FindItemsSQLCommand;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public class OperationSQLFactory<T, Q, R extends JpaRepository<T, UUID>> implements OperationsFactory<T, Q> {
    @Override
    public Operations<T, Q> createOperations() {
        return new OperationsSQL<T, Q, R>();
    }
}
