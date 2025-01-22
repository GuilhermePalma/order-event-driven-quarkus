package com.guilhermepalma.order_spring_boot.factory.kafka;

import com.guilhermepalma.order_spring_boot.Util;
import com.guilhermepalma.order_spring_boot.dto.OperationResultDTO;
import com.guilhermepalma.order_spring_boot.dto.command.DeleteItemsCommand;
import com.guilhermepalma.order_spring_boot.dto.command.UpsertItemsCommand;
import com.guilhermepalma.order_spring_boot.factory.FindInterface;
import com.guilhermepalma.order_spring_boot.factory.Operations;
import com.guilhermepalma.order_spring_boot.model.Order;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;

import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

/**
 * Create an Operations by SQL Implementation
 *
 * @param <T> Entity Class
 */
@Log4j2
@RequiredArgsConstructor
public class OperationsKafka<T, Void> implements Operations<T, Void> {
    private final KafkaTemplate<String, T> producer;
    @Value("${mykafka.timeout.waiting.response}")
    private String TIMEOUT_WAITING_RESPONSE;

    @Override
    public OperationResultDTO<?> createOne(UpsertItemsCommand<T> command) {
        log.info("Started create one by Kafka Operations...");
        try {
            if (Objects.isNull(command) || Objects.isNull(command.getPayload())) {
                return new OperationResultDTO<>(new Exception("Payload can't be null"));
            }

            T data = command.getPayload().getFirstOnData();
            SendResult<String, T> kafkaResponse = producer.send(command.getIdentifier(), Util.generateIdentifierId(), data)
                    .get(getTimeout(), TimeUnit.SECONDS);

            OperationResultDTO<T> result = new OperationResultDTO<>(data);
            result.setMetadata(new HashSet<>(Arrays.asList(kafkaResponse.getRecordMetadata().toString(),
                    kafkaResponse.getProducerRecord().toString())));

            return result;
        } catch (Exception ex) {
            log.error("exception", ex);
            return new OperationResultDTO<>(ex);
        } finally {
            log.info("Finished create one by Kafka Operations...");
        }
    }

    @Override
    public OperationResultDTO<?> createMany(UpsertItemsCommand<T> command) {
        log.info("Started create many by Kafka Operations...");
        try {
            if (Objects.isNull(command) || Objects.isNull(command.getPayload())) {
                return new OperationResultDTO<>(new Exception("Payload can't be null"));
            }

            List<T> data = command.getPayload().getData();
            if (Util.isEmptyOrNull(data)) {
                return new OperationResultDTO<>(new IllegalArgumentException("Empty Payload"));
            }

            final OperationResultDTO<?> result = new OperationResultDTO<>(data, data.size());
            Set<String> metadata = Collections.synchronizedSet(new HashSet<>());
            Set<String> errors = Collections.synchronizedSet(new HashSet<>());
            data.forEach(item -> {
                SendResult<String, T> kafkaResponse = null;
                try {
                    kafkaResponse = producer.send(command.getIdentifier(), Util.generateIdentifierId(), item)
                            .get(getTimeout(), TimeUnit.SECONDS);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

                metadata.add(kafkaResponse.getRecordMetadata().toString());
                metadata.add(kafkaResponse.getProducerRecord().toString());
            });
            result.setErrors(String.join("\n", errors));
            result.setTotalErrors((long) errors.size());
            result.setMetadata(metadata);

            return result;
        } catch (Exception ex) {
            log.error("exception", ex);
            return new OperationResultDTO<>(ex);
        } finally {
            log.info("Finished create many by Kafka Operations...");
        }
    }

    @Override
    public OperationResultDTO<?> updateOne(UpsertItemsCommand<T> command) {
        log.info("Started update one by Kafka Operations...");
        try {
            return this.createOne(command);
        } catch (Exception ex) {
            log.error("exception", ex);
            return new OperationResultDTO<>(ex);
        } finally {
            log.info("Finished create one by Kafka Operations...");
        }
    }

    @Override
    public OperationResultDTO<?> updateMany(UpsertItemsCommand<T> command) {
        log.info("Started update many by Kafka Operations...");
        try {
            return this.createMany(command);
        } catch (Exception ex) {
            log.error("exception", ex);
            return new OperationResultDTO<>(ex);
        } finally {
            log.info("Finished update many by Kafka Operations...");
        }
    }

    @Override
    public OperationResultDTO<?> deleteOne(DeleteItemsCommand command) {
        return deleteMany(command);
    }

    @Override
    public OperationResultDTO<?> deleteMany(DeleteItemsCommand command) {
        log.info("Started delete many by Kafka Operations...");
        try {
            return new OperationResultDTO<>(new NotImplementedException("Not Implemented"));
        } catch (Exception ex) {
            log.error("exception", ex);
            return new OperationResultDTO<>(ex);
        } finally {
            log.info("Finished delete many by Kafka Operations...");
        }
    }

    @Override
    public OperationResultDTO<?> findOne(FindInterface<Void> command) {
        OperationResultDTO<?> many = findMany(command);
        return Objects.isNull(many) || Objects.isNull(many.getData()) || many.getData().isEmpty()
                ? new OperationResultDTO<>() : new OperationResultDTO<>(many.getData().get(0));
    }

    @Override
    public OperationResultDTO<?> findMany(FindInterface<Void> command) {
        try {
            return command.executeQuery();
        } catch (Exception ex) {
            log.error("exception", ex);
            return new OperationResultDTO<>(ex);
        } finally {
            log.info("Finished find many by Kafka Operations...");
        }
    }

    public long getTimeout() {
        try {
            return Long.parseLong(TIMEOUT_WAITING_RESPONSE);
        } catch (Exception ex) {
            log.error("error to convert value on timeout", ex);
            return 600;
        }
    }

}
