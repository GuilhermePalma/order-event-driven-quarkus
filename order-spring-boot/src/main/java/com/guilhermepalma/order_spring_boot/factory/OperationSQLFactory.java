package com.guilhermepalma.order_spring_boot.factory;

import com.guilhermepalma.order_spring_boot.dto.command.FindItemsByParametersCommand;
import com.guilhermepalma.order_spring_boot.dto.command.FindItemsSQLCommand;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public class OperationSQLFactory<T, R extends JpaRepository<T, UUID>> implements OperationsFactory<T> {
    @Override
    public Operations<T> createOperations() {
        return new OperationsSQL<T, R>();
    }
}
