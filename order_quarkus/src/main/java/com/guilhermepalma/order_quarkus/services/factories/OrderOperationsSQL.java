package com.guilhermepalma.order_quarkus.services.factories;

import com.guilhermepalma.order_quarkus.dto.command.FindOrderByParametersCommand;
import com.guilhermepalma.order_quarkus.factory.sql.OperationsSQL;
import com.guilhermepalma.order_quarkus.model.Order;
import com.guilhermepalma.order_quarkus.repository.OrderRepository;
import jakarta.inject.Singleton;
import org.springframework.stereotype.Service;

@Singleton
public class OrderOperationsSQL extends OperationsSQL<Order, FindOrderByParametersCommand, OrderRepository> {

    public OrderOperationsSQL(OrderRepository repository) {
        super(repository);
    }
}
