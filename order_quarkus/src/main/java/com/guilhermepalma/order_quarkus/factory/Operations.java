package com.guilhermepalma.order_quarkus.factory;

import com.guilhermepalma.order_quarkus.dto.OperationResultDTO;
import com.guilhermepalma.order_quarkus.dto.command.DeleteItemsCommand;
import com.guilhermepalma.order_quarkus.dto.command.FindItemsByParametersCommand;
import com.guilhermepalma.order_quarkus.dto.command.UpsertItemsCommand;

/**
 * Interface with basic Operations (CRUD)
 * @param <T> Entity Class
 */
public interface Operations<T, Q> {

    OperationResultDTO<?> createOne(UpsertItemsCommand<T> command);
    OperationResultDTO<?> createMany(UpsertItemsCommand<T> command);

    OperationResultDTO<?> updateOne(UpsertItemsCommand<T> command);
    OperationResultDTO<?> updateMany(UpsertItemsCommand<T> command);

    OperationResultDTO<?> deleteOne(DeleteItemsCommand command);
    OperationResultDTO<?> deleteMany(DeleteItemsCommand command);

    OperationResultDTO<?> findOne(FindInterface<Q> command);
    OperationResultDTO<?> findMany(FindInterface<Q> command);
}
