package com.guilhermepalma.order_spring_boot.services.factories;

import com.guilhermepalma.order_spring_boot.Util;
import com.guilhermepalma.order_spring_boot.dto.OperationResultDTO;
import com.guilhermepalma.order_spring_boot.dto.command.FindOrderByParametersCommand;
import com.guilhermepalma.order_spring_boot.factory.FindInterface;
import com.guilhermepalma.order_spring_boot.factory.OperationsSQL;
import com.guilhermepalma.order_spring_boot.model.Order;
import com.guilhermepalma.order_spring_boot.repository.OrderRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Objects;

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

        FindInterface<FindOrderByParametersCommand> overrideCommand = new FindInterface<>() {
            @Override
            public OperationResultDTO<?> executeQuery(FindOrderByParametersCommand queryParameters) {
                Boolean isDeleted = !Objects.isNull(queryParameters.getIsDeleted()) && queryParameters.getIsDeleted();

                if (!Util.isEmptyOrNull(queryParameters.getId())) {
                    final Page<Order> pageReturned = repository.findAllByIsDeletedIsAndIdIn(isDeleted, queryParameters.getId(), page);
                    return new OperationResultDTO<>(pageReturned.getContent(), pageReturned.getTotalElements());
                }

                if (!Util.isEmptyOrNull(queryParameters.getType()) && !Util.isEmptyOrNull(queryParameters.getCustomerName()) && !Util.isEmptyOrNull(queryParameters.getName())) {
                    final Page<Order> pageReturned = repository.findAllByIsDeletedIsAndCustomerNameInAndProductInAndStatusIn(isDeleted,
                            queryParameters.getCustomerName(), queryParameters.getName(), queryParameters.getType(), page);
                    return new OperationResultDTO<>(pageReturned.getContent(), pageReturned.getTotalElements());
                }

                if (!Util.isEmptyOrNull(queryParameters.getName()) && !Util.isEmptyOrNull(queryParameters.getType())) {
                    final Page<Order> pageReturned = repository.findAllByIsDeletedIsAndProductInAndStatusIn(isDeleted,
                            queryParameters.getName(), queryParameters.getType(), page);
                    return new OperationResultDTO<>(pageReturned.getContent(), pageReturned.getTotalElements());
                }

                if (!Util.isEmptyOrNull(queryParameters.getCustomerName()) && !Util.isEmptyOrNull(queryParameters.getType())) {
                    final Page<Order> pageReturned = repository.findAllByIsDeletedIsAndCustomerNameInAndStatusIn(isDeleted,
                            queryParameters.getCustomerName(), queryParameters.getType(), page);
                    return new OperationResultDTO<>(pageReturned.getContent(), pageReturned.getTotalElements());
                }

                final Page<Order> pageReturned;
                if (!Util.isEmptyOrNull(queryParameters.getName())) {
                    pageReturned = repository.findAllByIsDeletedIsAndProductIn(isDeleted, queryParameters.getName(), page);
                } else if (!Util.isEmptyOrNull(queryParameters.getCustomerName())) {
                    pageReturned = repository.findAllByIsDeletedIsAndCustomerNameIn(isDeleted, queryParameters.getCustomerName(), page);
                } else if (!Util.isEmptyOrNull(queryParameters.getType())) {
                    pageReturned = repository.findAllByIsDeletedIsAndStatusIn(isDeleted, queryParameters.getType(), page);
                } else {
                    pageReturned = repository.findAll(page);
                }

                return new OperationResultDTO<>(pageReturned.getContent(), pageReturned.getTotalElements());
            }

        };

        return super.findMany(overrideCommand);
    }

}
