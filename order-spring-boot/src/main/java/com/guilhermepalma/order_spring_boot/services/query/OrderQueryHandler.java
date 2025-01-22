package com.guilhermepalma.order_spring_boot.services.query;

import com.guilhermepalma.order_spring_boot.Util;
import com.guilhermepalma.order_spring_boot.dto.OperationResultDTO;
import com.guilhermepalma.order_spring_boot.dto.command.DeleteItemsCommand;
import com.guilhermepalma.order_spring_boot.dto.command.FindOrderByParametersCommand;
import com.guilhermepalma.order_spring_boot.dto.command.UpsertItemsCommand;
import com.guilhermepalma.order_spring_boot.factory.FindInterface;
import com.guilhermepalma.order_spring_boot.factory.OperationsSQL;
import com.guilhermepalma.order_spring_boot.model.Order;
import com.guilhermepalma.order_spring_boot.repository.OrderRepository;
import com.guilhermepalma.order_spring_boot.services.factories.OrderOperationsSQL;
import com.guilhermepalma.order_spring_boot.type.StatusType;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Set;

@Service
public class OrderQueryHandler extends OperationsSQL<Order, FindOrderByParametersCommand, OrderRepository> {

    private final OrderRepository repository;

    public OrderQueryHandler(OrderRepository repository) {
        super(repository);
        this.repository = repository;
    }

    @Override
    public OperationResultDTO<?> createOne(UpsertItemsCommand<Order> command) {
        throw new NotImplementedException(String.format("Use %s to Handle with Events", OrderOperationsSQL.class.getSimpleName()));
    }

    @Override
    public OperationResultDTO<?> createMany(UpsertItemsCommand<Order> command) {
        throw new NotImplementedException(String.format("Use %s to Handle with Events", OrderOperationsSQL.class.getSimpleName()));
    }

    @Override
    public OperationResultDTO<?> updateOne(UpsertItemsCommand<Order> command) {
        throw new NotImplementedException(String.format("Use %s to Handle with Events", OrderOperationsSQL.class.getSimpleName()));
    }

    @Override
    public OperationResultDTO<?> updateMany(UpsertItemsCommand<Order> command) {
        throw new NotImplementedException(String.format("Use %s to Handle with Events", OrderOperationsSQL.class.getSimpleName()));
    }

    @Override
    public OperationResultDTO<?> deleteOne(DeleteItemsCommand command) {
        throw new NotImplementedException(String.format("Use %s to Handle with Events", OrderOperationsSQL.class.getSimpleName()));
    }

    @Override
    public OperationResultDTO<?> deleteMany(DeleteItemsCommand command) {
        throw new NotImplementedException(String.format("Use %s to Handle with Events", OrderOperationsSQL.class.getSimpleName()));
    }

    @Override
    public OperationResultDTO<?> findOne(FindInterface<FindOrderByParametersCommand> command) {
        return super.findOne(getOverrideFind(command));
    }

    @Override
    public OperationResultDTO<?> findMany(FindInterface<FindOrderByParametersCommand> command) {
        return super.findMany(getOverrideFind(command));
    }

    private FindInterface<FindOrderByParametersCommand> getOverrideFind(FindInterface<FindOrderByParametersCommand> command) {
        FindOrderByParametersCommand query = command.getQuery();
        Pageable page = PageRequest.of(query.getPageNumber(), query.getPageSize(), Sort.by("id").ascending());

        return new FindInterface<>() {
            @Override
            public FindOrderByParametersCommand getQuery() {
                return query;
            }

            @Override
            public OperationResultDTO<?> executeQuery() {
                FindOrderByParametersCommand queryParameters = getQuery();
                final boolean isDeleted = !Objects.isNull(queryParameters.getIsDeleted()) && queryParameters.getIsDeleted();

                if (!Util.isEmptyOrNull(queryParameters.getId())) {
                    final Page<Order> pageReturned = repository.findAllByIsDeletedIsAndIdIn(isDeleted, queryParameters.getId(), page);
                    return new OperationResultDTO<>(pageReturned.getContent(), pageReturned.getTotalElements());
                }

                final Set<String> productName = queryParameters.getName(), customerName = queryParameters.getCustomerName();
                final Set<StatusType> type = queryParameters.getType();
                if (!Util.isEmptyOrNull(type) && !Util.isEmptyOrNull(customerName) && !Util.isEmptyOrNull(productName)) {
                    final Page<Order> pageReturned = repository.findAllByIsDeletedIsAndCustomerNameInAndProductInAndStatusIn(isDeleted,
                            customerName, productName, type, page);
                    return new OperationResultDTO<>(pageReturned.getContent(), pageReturned.getTotalElements());
                }

                if (!Util.isEmptyOrNull(productName) && !Util.isEmptyOrNull(type)) {
                    final Page<Order> pageReturned = repository.findAllByIsDeletedIsAndProductInAndStatusIn(isDeleted,
                            productName, type, page);
                    return new OperationResultDTO<>(pageReturned.getContent(), pageReturned.getTotalElements());
                }

                if (!Util.isEmptyOrNull(customerName) && !Util.isEmptyOrNull(type)) {
                    final Page<Order> pageReturned = repository.findAllByIsDeletedIsAndCustomerNameInAndStatusIn(isDeleted,
                            customerName, type, page);
                    return new OperationResultDTO<>(pageReturned.getContent(), pageReturned.getTotalElements());
                }

                final Page<Order> pageReturned;
                if (!Util.isEmptyOrNull(productName)) {
                    pageReturned = repository.findAllByIsDeletedIsAndProductIn(isDeleted, productName, page);
                } else if (!Util.isEmptyOrNull(customerName)) {
                    pageReturned = repository.findAllByIsDeletedIsAndCustomerNameIn(isDeleted, customerName, page);
                } else if (!Util.isEmptyOrNull(type)) {
                    pageReturned = repository.findAllByIsDeletedIsAndStatusIn(isDeleted, type, page);
                } else if (!Objects.isNull(queryParameters.getIsDeleted())) {
                    pageReturned = repository.findAllByIsDeletedIs(isDeleted, page);
                } else {
                    pageReturned = repository.findAll(page);
                }

                return new OperationResultDTO<>(pageReturned.getContent(), pageReturned.getTotalElements());
            }
        };
    }
}
