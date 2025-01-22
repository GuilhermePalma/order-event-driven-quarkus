package com.guilhermepalma.order_quarkus.services.consumer;

import com.guilhermepalma.order_quarkus.dto.PayloadDTO;
import com.guilhermepalma.order_quarkus.dto.command.UpsertItemsCommand;
import com.guilhermepalma.order_quarkus.model.Order;
import com.guilhermepalma.order_quarkus.services.factories.OrderOperationsSQL;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Singleton
@Slf4j
@RequiredArgsConstructor
public class OrderEventHandler {

    private final OrderOperationsSQL orderOperationsSQL;

    @Incoming("${mykafka.topics.order.insert}")
    private void insertOrder(ConsumerRecord<String, Order> event) {
        log.info("OrderEventHandler start insertOrder on Topic: [{}] Partition: [{}] Key: [{}]",
                event.topic(), event.partition(), event.key());

        orderOperationsSQL.createMany(new UpsertItemsCommand<>(new PayloadDTO<>(event.value())));

        log.info("OrderEventHandler insertOrder finished");
    }

    @Incoming("${mykafka.topics.order.update}")
    private void updateOrder(ConsumerRecord<String, Order> event) {
        log.info("OrderEventHandler start updateOrder on Topic: [{}] Partition: [{}] Key: [{}]",
                event.topic(), event.partition(), event.key());

        if (Objects.isNull(event.value())) {
            log.error("Empty Payload on Key: [{}]", event.key());
            return;
        }

        orderOperationsSQL.updateMany(new UpsertItemsCommand<>(new PayloadDTO<>(event.value())));

        log.info("OrderEventHandler updateOrder finished");
    }

}
