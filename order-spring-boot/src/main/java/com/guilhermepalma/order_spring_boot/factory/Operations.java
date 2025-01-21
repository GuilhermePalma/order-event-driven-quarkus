package com.guilhermepalma.order_spring_boot.factory;

import com.guilhermepalma.order_spring_boot.dto.OperationResultDTO;
import com.guilhermepalma.order_spring_boot.dto.command.DeleteItemsCommand;
import com.guilhermepalma.order_spring_boot.dto.command.FindItemsByParametersCommand;
import com.guilhermepalma.order_spring_boot.dto.command.UpsertItemsCommand;

/**
 * Interface with basic Operations (CRUD)
 * @param <T> Entity Class
 */
public interface Operations<T> {

    OperationResultDTO<?> createOne(UpsertItemsCommand<T> command);
    OperationResultDTO<?> createMany(UpsertItemsCommand<T> command);

    OperationResultDTO<?> updateOne(UpsertItemsCommand<T> command);
    OperationResultDTO<?> updateMany(UpsertItemsCommand<T> command);

    OperationResultDTO<?> deleteOne(DeleteItemsCommand command);
    OperationResultDTO<?> deleteMany(DeleteItemsCommand command);

    OperationResultDTO<?> findOne(FindInterface<FindItemsByParametersCommand> command);
    OperationResultDTO<?> findMany(FindInterface<FindItemsByParametersCommand> command);
}
