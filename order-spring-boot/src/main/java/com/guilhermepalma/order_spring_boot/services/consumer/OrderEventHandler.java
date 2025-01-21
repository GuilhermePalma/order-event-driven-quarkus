package com.guilhermepalma.order_spring_boot.services.consumer;

import com.guilhermepalma.order_spring_boot.dto.OperationResultDTO;
import com.guilhermepalma.order_spring_boot.dto.command.FindOrderByParametersCommand;
import com.guilhermepalma.order_spring_boot.dto.command.UpsertItemsCommand;
import com.guilhermepalma.order_spring_boot.factory.FindInterface;
import com.guilhermepalma.order_spring_boot.model.Order;
import com.guilhermepalma.order_spring_boot.services.factories.OrderOperationsSQL;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderEventHandler {

    private final OrderOperationsSQL orderOperationsSQL;

    @KafkaListener(topics = "${mykafka.topics.order.insert}", groupId = "group-id", concurrency = "5",
        topicPartitions = @TopicPartition(topic = "${mykafka.topics.order.insert}", partitions = {"0", "1", "2", "3", "4"}))
    private void insertOrder(ConsumerRecord<String, UpsertItemsCommand<Order>> event){
        log.info("OrderEventHandler start insertOrder on Topic: [{}] Partition: [{}] Key: [{}]",
                event.topic(), event.partition(), event.key());

        if (Objects.isNull(event.value())){
            log.error("Empty Payload on Key: [{}]", event.key());
            return;
        }

        orderOperationsSQL.createMany(event.value());

        log.info("OrderEventHandler insertOrder finished");
    }

    @KafkaListener(topics = "${mykafka.topics.order.update}", groupId = "group-id", concurrency = "5",
            topicPartitions = @TopicPartition(topic = "${mykafka.topics.order.insert}", partitions = {"0", "1", "2", "3", "4"}))
    private void updateOrder(ConsumerRecord<String, UpsertItemsCommand<Order>> event){
        log.info("OrderEventHandler start updateOrder on Topic: [{}] Partition: [{}] Key: [{}]",
                event.topic(), event.partition(), event.key());

        if (Objects.isNull(event.value())){
            log.error("Empty Payload on Key: [{}]", event.key());
            return;
        }

        orderOperationsSQL.updateMany(event.value());

        log.info("OrderEventHandler updateOrder finished");
    }

    @KafkaListener(topics = "${mykafka.topics.order.list}", groupId = "group-id", concurrency = "5",
            topicPartitions = @TopicPartition(topic = "${mykafka.topics.order.insert}", partitions = {"0", "1", "2", "3", "4"}))
    private void findOrder(ConsumerRecord<String, FindOrderByParametersCommand> query){
        log.info("OrderEventHandler start findOrder on Topic: [{}] Partition: [{}] Key: [{}]",
                query.topic(), query.partition(), query.key());

        if (Objects.isNull(query.value())){
            log.error("Empty Payload on Key: [{}]", query.key());
            return;
        }

        orderOperationsSQL.findMany(new FindInterface<>(query.value()) {
            @Override
            public OperationResultDTO<?> executeQuery(FindOrderByParametersCommand queryParameters) {
                return null;
            }
        });

        log.info("OrderEventHandler findOrder finished");
    }


}
