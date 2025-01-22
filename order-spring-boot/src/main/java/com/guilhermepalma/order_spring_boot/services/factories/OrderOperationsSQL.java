package com.guilhermepalma.order_spring_boot.services.factories;

import com.guilhermepalma.order_spring_boot.dto.command.FindOrderByParametersCommand;
import com.guilhermepalma.order_spring_boot.factory.OperationsSQL;
import com.guilhermepalma.order_spring_boot.model.Order;
import com.guilhermepalma.order_spring_boot.repository.OrderRepository;
import org.springframework.stereotype.Service;

@Service
public class OrderOperationsSQL extends OperationsSQL<Order, FindOrderByParametersCommand, OrderRepository> {

    public OrderOperationsSQL(OrderRepository repository) {
        super(repository);
    }
}
