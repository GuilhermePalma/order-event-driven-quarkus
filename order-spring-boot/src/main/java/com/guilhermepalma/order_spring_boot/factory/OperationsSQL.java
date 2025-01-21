package com.guilhermepalma.order_spring_boot.factory;

import com.guilhermepalma.order_spring_boot.dto.OperationResultDTO;
import com.guilhermepalma.order_spring_boot.dto.command.DeleteItemsCommand;
import com.guilhermepalma.order_spring_boot.dto.command.FindItemsByParametersCommand;
import com.guilhermepalma.order_spring_boot.dto.command.UpsertItemsCommand;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

/**
 * Create an Operations by SQL Implementation
 * @param <T> Entity Class
 * @param <U> Entity Type (If there is)
 * @param <R> Repository Class by JPA Repository
 */
public class OperationsSQL<T, U, R extends JpaRepository<T, UUID>> implements Operations<T, U> {

    private U type;
    private R repository;

    @Override
    public OperationResultDTO<?> createOne(UpsertItemsCommand<T> command) throws Exception {
        return null;
    }

    @Override
    public OperationResultDTO<?> createMany(UpsertItemsCommand<T> command) throws Exception {
        return null;
    }

    @Override
    public OperationResultDTO<?> updateOne(UpsertItemsCommand<T> command) throws Exception {
        return null;
    }

    @Override
    public OperationResultDTO<?> updateMany(UpsertItemsCommand<T> command) throws Exception {
        return null;
    }

    @Override
    public OperationResultDTO<?> deleteOne(DeleteItemsCommand command) {
        return null;
    }

    @Override
    public OperationResultDTO<?> deleteMany(DeleteItemsCommand command) {
        return null;
    }

    @Override
    public OperationResultDTO<?> findOne(FindItemsByParametersCommand<U> command) {
        return null;
    }

    @Override
    public OperationResultDTO<?> findMany(FindItemsByParametersCommand<U> command) {
        return null;
    }
}
