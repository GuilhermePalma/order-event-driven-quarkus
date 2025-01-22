package com.guilhermepalma.order_spring_boot.factory.kafka;

import com.guilhermepalma.order_spring_boot.factory.Operations;
import com.guilhermepalma.order_spring_boot.factory.OperationsFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OperationKafkaFactory<T> implements OperationsFactory<T, Void> {

    private final KafkaTemplate<String, T> producer;

    @Override
    public Operations<T, Void> createOperations() {
        return new OperationsKafka<T, Void>(producer);
    }
}
