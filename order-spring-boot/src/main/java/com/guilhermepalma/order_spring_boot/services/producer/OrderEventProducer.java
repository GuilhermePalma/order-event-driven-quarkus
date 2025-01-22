package com.guilhermepalma.order_spring_boot.services.producer;

import com.guilhermepalma.order_spring_boot.dto.OperationResultDTO;
import com.guilhermepalma.order_spring_boot.dto.command.DeleteItemsCommand;
import com.guilhermepalma.order_spring_boot.dto.command.UpsertItemsCommand;
import com.guilhermepalma.order_spring_boot.factory.FindInterface;
import com.guilhermepalma.order_spring_boot.factory.kafka.OperationsKafka;
import com.guilhermepalma.order_spring_boot.model.Order;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class OrderEventProducer extends OperationsKafka<Order, Void> {
    @Value("${mykafka.topics.order.insert}")
    private String INSERT_ORDER;

    @Value("${mykafka.topics.order.update}")
    private String UPDATE_ORDER;

    public OrderEventProducer(KafkaTemplate<String, Order> producer) {
        super(producer);
    }

    @Override
    public OperationResultDTO<?> createOne(UpsertItemsCommand<Order> event) {
        event.setIdentifier(INSERT_ORDER);
        return super.createOne(event);
    }

    @Override
    public OperationResultDTO<?> createMany(UpsertItemsCommand<Order> command) {
        command.setIdentifier(INSERT_ORDER);
        return super.createOne(command);
    }

    @Override
    public OperationResultDTO<?> updateOne(UpsertItemsCommand<Order> command) {
        command.setIdentifier(UPDATE_ORDER);
        return super.updateOne(command);
    }

    @Override
    public OperationResultDTO<?> updateMany(UpsertItemsCommand<Order> command) {
        command.setIdentifier(UPDATE_ORDER);
        return super.updateMany(command);
    }

    @Override
    public OperationResultDTO<?> deleteOne(DeleteItemsCommand command) {
        return new OperationResultDTO<>(new NotImplementedException("not implemented delete method"));
    }

    @Override
    public OperationResultDTO<?> deleteMany(DeleteItemsCommand command) {
        return new OperationResultDTO<>(new NotImplementedException("not implemented delete method"));
    }

    @Override
    public OperationResultDTO<?> findOne(FindInterface<Void> command) {
        return new OperationResultDTO<>(new NotImplementedException("not implemented find method"));
    }

    @Override
    public OperationResultDTO<?> findMany(FindInterface<Void> command) {
        return new OperationResultDTO<>(new NotImplementedException("not implemented find method"));
    }

}
