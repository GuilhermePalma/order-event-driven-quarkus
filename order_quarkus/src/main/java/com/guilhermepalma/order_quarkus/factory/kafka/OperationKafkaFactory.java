package com.guilhermepalma.order_quarkus.factory.kafka;

import com.guilhermepalma.order_quarkus.factory.Operations;
import com.guilhermepalma.order_quarkus.factory.OperationsFactory;
import jakarta.enterprise.context.RequestScoped;
import org.apache.kafka.clients.producer.Producer;

public class OperationKafkaFactory<T> implements OperationsFactory<T, Void> {
    private final Producer<String, T> producer;

    public OperationKafkaFactory(Producer<String, T> producer) {
        this.producer = producer;
    }

    @Override
    public Operations<T, Void> createOperations() {
        return new OperationsKafka<T, Void>(producer);
    }
}
