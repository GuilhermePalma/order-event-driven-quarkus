package com.guilhermepalma.order_quarkus.factory.kafka;

import com.guilhermepalma.order_quarkus.Util;
import com.guilhermepalma.order_quarkus.dto.OperationResultDTO;
import com.guilhermepalma.order_quarkus.dto.command.DeleteItemsCommand;
import com.guilhermepalma.order_quarkus.dto.command.UpsertItemsCommand;
import com.guilhermepalma.order_quarkus.factory.FindInterface;
import com.guilhermepalma.order_quarkus.factory.Operations;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.NotImplementedException;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.jboss.logmanager.Level;
import org.springframework.beans.factory.annotation.Value;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.logging.LogManager;
import java.util.logging.Logger;

/**
 * Create an Operations by SQL Implementation
 *
 * @param <T> Entity Class
 */
@RequiredArgsConstructor
public class OperationsKafka<T, Void> implements Operations<T, Void> {
    private static final Logger log = LogManager.getLogManager().getLogger(OperationsKafka.class.getName());

    private final Producer<String, T> producer;
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
            ProducerRecord<String, T> record = new ProducerRecord<>(command.getIdentifier(), Util.generateIdentifierId(), data);
            RecordMetadata recordMetadata = producer.send(record).get(getTimeout(), TimeUnit.SECONDS);

            OperationResultDTO<T> result = new OperationResultDTO<>(data);
            result.setMetadata(new HashSet<>(Arrays.asList(recordMetadata.toString(), record.toString())));
            return result;
        } catch (Exception ex) {
            log.log(Level.ERROR, "exception", ex);
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
                RecordMetadata kafkaResponse;
                try {
                    ProducerRecord<String, T> record = new ProducerRecord<>(command.getIdentifier(), Util.generateIdentifierId(), item);
                    metadata.add(record.toString());
                    kafkaResponse = producer.send(record).get(getTimeout(), TimeUnit.SECONDS);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

                if (!Objects.isNull(kafkaResponse)) {
                    metadata.add(kafkaResponse.toString());
                }
            });
            result.setErrors(String.join("\n", errors));
            result.setTotalErrors((long) errors.size());
            result.setMetadata(metadata);

            return result;
        } catch (Exception ex) {
            log.log(Level.ERROR, "exception", ex);
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
            log.log(Level.ERROR, "exception", ex);
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
            log.log(Level.ERROR, "exception", ex);
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
            log.log(Level.ERROR, "exception", ex);
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
            log.log(Level.ERROR, "exception", ex);
            return new OperationResultDTO<>(ex);
        } finally {
            log.info("Finished find many by Kafka Operations...");
        }
    }

    public long getTimeout() {
        try {
            return Long.parseLong(TIMEOUT_WAITING_RESPONSE);
        } catch (Exception ex) {
            log.log(Level.ERROR, "error to convert value on timeout", ex);
            return 600;
        }
    }

}
