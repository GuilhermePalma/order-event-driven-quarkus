package com.guilhermepalma.order_spring_boot.services.factories;

import com.guilhermepalma.order_spring_boot.Util;
import com.guilhermepalma.order_spring_boot.dto.OperationResultDTO;
import com.guilhermepalma.order_spring_boot.dto.command.FindOrderByParametersCommand;
import com.guilhermepalma.order_spring_boot.factory.FindInterface;
import com.guilhermepalma.order_spring_boot.factory.OperationsSQL;
import com.guilhermepalma.order_spring_boot.model.Order;
import com.guilhermepalma.order_spring_boot.repository.OrderRepository;
import com.guilhermepalma.order_spring_boot.type.StatusType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Set;

@Service
public class OrderOperationsSQL extends OperationsSQL<Order, FindOrderByParametersCommand, OrderRepository> {

    private final OrderRepository repository;

    public OrderOperationsSQL(OrderRepository repository) {
        this.repository = repository;
    }

    @Override
    public OperationResultDTO<?> findOne(FindInterface<FindOrderByParametersCommand> command) {
        return findMany(command);
    }

    @Override
    public OperationResultDTO<?> findMany(FindInterface<FindOrderByParametersCommand> command) {
        Pageable page = PageRequest.of(command.getQuery().getPageNumber(), command.getQuery().getPageSize(), Sort.by("id").ascending());

        FindInterface<FindOrderByParametersCommand> overrideCommand = new FindInterface<>(command.getQuery()) {
            @Override
            public OperationResultDTO<?> executeQuery(FindOrderByParametersCommand queryParameters) {
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
                } else {
                    pageReturned = repository.findAll(page);
                }

                return new OperationResultDTO<>(pageReturned.getContent(), pageReturned.getTotalElements());
            }

        };

        return super.findMany(overrideCommand);
    }

}
