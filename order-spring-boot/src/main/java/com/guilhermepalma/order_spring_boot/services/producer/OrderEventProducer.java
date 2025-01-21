package com.guilhermepalma.order_spring_boot.services.producer;

import com.guilhermepalma.order_spring_boot.Util;
import com.guilhermepalma.order_spring_boot.dto.OperationResultDTO;
import com.guilhermepalma.order_spring_boot.dto.PayloadDTO;
import com.guilhermepalma.order_spring_boot.dto.command.UpsertItemsCommand;
import com.guilhermepalma.order_spring_boot.model.Order;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderEventProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${mykafka.timeout.waiting.response}")
    private String TIMEOUT_WAITING_RESPONSE;
    @Value("${mykafka.topics.order.insert}")
    private String INSERT_ODER_MANY;
    @Value("${mykafka.topics.order.update}")
    private String UPDATE_ODER_MANY;

    private OperationResultDTO<?> insertOrder(UpsertItemsCommand<Order> event) {
        log.info("OrderEventProducer start insertOrder");
        try {
            if (Objects.isNull(event.getPayload())) {
                return new OperationResultDTO<>(new IllegalArgumentException("Empty Payload"));
            }

            SendResult<String, Object> result = kafkaTemplate.send(INSERT_ODER_MANY, Util.generateIdentifierId(), event)
                    .get(getTimeout(), TimeUnit.SECONDS);

            OperationResultDTO<PayloadDTO<Order>> data = new OperationResultDTO<>(event.getPayload());
            data.setMetadata(new HashSet<>(Arrays.asList(result.getRecordMetadata().toString(), result.getProducerRecord().toString())));

            return data;
        } catch (Exception ex) {
            return new OperationResultDTO<>(ex);
        } finally {
            log.info("OrderEventProducer insertOrder finished");
        }
    }

    private OperationResultDTO<?> updateOrder(UpsertItemsCommand<Order> event) {
        log.info("OrderEventProducer start updateOrder");
        try {
            if (Objects.isNull(event.getPayload())) {
                return new OperationResultDTO<>(new IllegalArgumentException("Empty Payload"));
            }

            SendResult<String, Object> result = kafkaTemplate.send(UPDATE_ODER_MANY, Util.generateIdentifierId(), event)
                    .get(getTimeout(), TimeUnit.SECONDS);

            OperationResultDTO<PayloadDTO<Order>> data = new OperationResultDTO<>(event.getPayload());
            data.setMetadata(new HashSet<>(Arrays.asList(result.getRecordMetadata().toString(), result.getProducerRecord().toString())));

            return data;
        } catch (Exception ex) {
            return new OperationResultDTO<>(ex);
        } finally {
            log.info("OrderEventProducer updateOrder finished");
        }
    }

    private long getTimeout() {
        try {
            return Long.parseLong(TIMEOUT_WAITING_RESPONSE);
        } catch (Exception ex) {
            log.error("error to convert value on timeout", ex);
            return 600;
        }
    }

}
