package com.guilhermepalma.order_spring_boot.services.consumer;

import com.guilhermepalma.order_spring_boot.dto.PayloadDTO;
import com.guilhermepalma.order_spring_boot.dto.command.UpsertItemsCommand;
import com.guilhermepalma.order_spring_boot.model.Order;
import com.guilhermepalma.order_spring_boot.services.factories.OrderOperationsSQL;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderEventHandler {

    private final OrderOperationsSQL orderOperationsSQL;

    @KafkaListener(topics = {"${mykafka.topics.order.insert}"}, groupId = "group-id")
    private void insertOrder(ConsumerRecord<String, Order> event) {
        log.info("OrderEventHandler start insertOrder on Topic: [{}] Partition: [{}] Key: [{}]",
                event.topic(), event.partition(), event.key());

        orderOperationsSQL.createMany(new UpsertItemsCommand<>(new PayloadDTO<>(event.value())));

        log.info("OrderEventHandler insertOrder finished");
    }

    @KafkaListener(topics = "${mykafka.topics.order.update}", groupId = "group-id")
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
