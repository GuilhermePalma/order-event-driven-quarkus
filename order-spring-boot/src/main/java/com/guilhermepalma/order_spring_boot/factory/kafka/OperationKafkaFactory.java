package com.guilhermepalma.order_spring_boot.factory.kafka;

import com.guilhermepalma.order_spring_boot.factory.Operations;
import com.guilhermepalma.order_spring_boot.factory.OperationsFactory;
import org.springframework.kafka.core.KafkaTemplate;

public class OperationKafkaFactory<T> implements OperationsFactory<T, Void> {

    private final KafkaTemplate<String, T> producer;

    public OperationKafkaFactory(KafkaTemplate<String, T> producer) {
        this.producer = producer;
    }

    @Override
    public Operations<T, Void> createOperations() {
        return new OperationsKafka<T, Void>(producer);
    }
}
