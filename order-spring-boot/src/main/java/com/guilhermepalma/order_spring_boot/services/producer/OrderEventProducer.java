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

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderEventProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${mykafka.timeout.waiting.response}")
    private String TIMEOUT_WAITING_RESPONSE;
    @Value("${mykafka.topics.order.insert}")
    private String INSERT_ORDER;
    @Value("${mykafka.topics.order.update}")
    private String UPDATE_ORDER;

    public OperationResultDTO<?> insertOrder(UpsertItemsCommand<Order> event) {
        log.info("OrderEventProducer start insertOrder");
        try {
            if (Objects.isNull(event.getPayload())) {
                return new OperationResultDTO<>(new IllegalArgumentException("Empty Payload"));
            }

            Set<String> metdata = Collections.synchronizedSet(new HashSet<>());
            Set<Exception> errors = Collections.synchronizedSet(new HashSet<>());
            OperationResultDTO<PayloadDTO<Order>> data = new OperationResultDTO<>(event.getPayload());
            event.getPayload().getData().forEach(item -> {
                try {
                    SendResult<String, Object> result = kafkaTemplate.send(INSERT_ORDER, Util.generateIdentifierId(), item)
                            .get(getTimeout(), TimeUnit.SECONDS);
                    metdata.add(result.getRecordMetadata().toString());
                    metdata.add(result.getProducerRecord().toString());
                } catch (Exception e) {
                    log.error("exception to send kafka message", e);
                    errors.add(e);
                }
            });
            data.setMetadata(metdata);
            data.setErrors(String.join("\n", errors.stream().map(Throwable::toString).toList()));
            data.setTotalErrors((long) errors.size());

            return data;
        } catch (Exception ex) {
            return new OperationResultDTO<>(ex);
        } finally {
            log.info("OrderEventProducer insertOrder finished");
        }
    }

    public OperationResultDTO<?> updateOrder(UpsertItemsCommand<Order> event) {
        log.info("OrderEventProducer start updateOrder");
        try {
            if (Objects.isNull(event.getPayload())) {
                return new OperationResultDTO<>(new IllegalArgumentException("Empty Payload"));
            }

            Set<String> metdata = Collections.synchronizedSet(new HashSet<>());
            Set<Exception> errors = Collections.synchronizedSet(new HashSet<>());
            OperationResultDTO<PayloadDTO<Order>> data = new OperationResultDTO<>(event.getPayload());
            event.getPayload().getData().forEach(item -> {
                try {
                    SendResult<String, Object> result = kafkaTemplate.send(UPDATE_ORDER, Util.generateIdentifierId(), item)
                            .get(getTimeout(), TimeUnit.SECONDS);
                    metdata.add(result.getRecordMetadata().toString());
                    metdata.add(result.getProducerRecord().toString());
                } catch (Exception e) {
                    log.error("exception to send kafka message", e);
                    errors.add(e);
                }
            });
            data.setMetadata(metdata);
            data.setErrors(String.join("\n", errors.stream().map(Throwable::toString).toList()));
            data.setTotalErrors((long) errors.size());

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
