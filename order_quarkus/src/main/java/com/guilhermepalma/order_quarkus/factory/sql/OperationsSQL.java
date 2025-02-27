package com.guilhermepalma.order_quarkus.factory.sql;

import com.guilhermepalma.order_quarkus.dto.OperationResultDTO;
import com.guilhermepalma.order_quarkus.dto.command.DeleteItemsCommand;
import com.guilhermepalma.order_quarkus.dto.command.UpsertItemsCommand;
import com.guilhermepalma.order_quarkus.factory.FindInterface;
import com.guilhermepalma.order_quarkus.factory.Operations;
import org.jboss.logmanager.Level;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;

import java.lang.reflect.Field;
import java.util.*;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Create an Operations by SQL Implementation
 *
 * @param <T> Entity Class
 * @param <R> Repository Class by JPA Repository
 */
public class OperationsSQL<T, Q, R extends JpaRepository<T, UUID>> implements Operations<T, Q> {
    private static final Logger log = LogManager.getLogManager().getLogger(OperationsSQL.class.getName());
    private final R repository;

    public OperationsSQL(R repository) {
        this.repository = repository;
    }

    @Override
    public OperationResultDTO<?> createOne(UpsertItemsCommand<T> command) {
        log.info("Started create one by SQL Operations...");
        try {
            if (Objects.isNull(command) || Objects.isNull(command.getPayload())) {
                return new OperationResultDTO<>(new Exception("Payload can't be null"));
            }

            T data = command.getPayload().getFirstOnData();
            if (Objects.isNull(data)) {
                return new OperationResultDTO<>(new IllegalArgumentException("Empty Payload"));
            } else if (repository.exists(Example.of(data))) {
                return new OperationResultDTO<>(new IllegalArgumentException("Item alread there is on Database"));
            }

            return new OperationResultDTO<>(repository.save(data));
        } catch (Exception ex) {
            return new OperationResultDTO<>(ex);
        } finally {
            log.info("Finished create one by SQL Operations...");
        }
    }

    @Override
    public OperationResultDTO<?> createMany(UpsertItemsCommand<T> command) {
        log.info("Started create many by SQL Operations...");
        try {
            if (Objects.isNull(command) || Objects.isNull(command.getPayload())) {
                return new OperationResultDTO<>(new Exception("Payload can't be null"));
            }

            List<T> data = command.getPayload().getData();
            if (Objects.isNull(data)) {
                return new OperationResultDTO<>(new IllegalArgumentException("Empty Payload"));
            }

            return new OperationResultDTO<>(repository.saveAll(data));
        } catch (Exception ex) {
            log.log(Level.ERROR, "exception", ex);
            return new OperationResultDTO<>(ex);
        } finally {
            log.info("Finished create many by SQL Operations...");
        }
    }

    @Override
    public OperationResultDTO<?> updateOne(UpsertItemsCommand<T> command) {
        log.info("Started update one by SQL Operations...");
        try {
            if (Objects.isNull(command) || Objects.isNull(command.getPayload())) {
                return new OperationResultDTO<>(new Exception("Payload can't be null"));
            }

            T data = command.getPayload().getFirstOnData();
            if (Objects.isNull(data)) {
                return new OperationResultDTO<>(new IllegalArgumentException("Empty Payload"));
            } else if (fetchDatabseItem(data).isEmpty()) {
                return new OperationResultDTO<>(new IllegalArgumentException("Item there isn't on Database"));
            }

            return new OperationResultDTO<>(repository.save(data));
        } catch (Exception ex) {
            return new OperationResultDTO<>(ex);
        } finally {
            log.info("Finished create one by SQL Operations...");
        }
    }

    @Override
    public OperationResultDTO<?> updateMany(UpsertItemsCommand<T> command) {
        log.info("Started update many by SQL Operations...");
        try {
            if (Objects.isNull(command) || Objects.isNull(command.getPayload())) {
                return new OperationResultDTO<>(new Exception("Payload can't be null"));
            }

            List<T> data = command.getPayload().getData();
            if (Objects.isNull(data)) {
                return new OperationResultDTO<>(new IllegalArgumentException("Empty Payload"));
            }

            Set<String> errors = Collections.synchronizedSet(new HashSet<>());
            List<T> validData = data.stream().map(item -> {
                try {
                    Optional<T> optional = fetchDatabseItem(item);
                    if (optional.isEmpty()) {
                        errors.add(String.format("Item there isn't on Database. Item: [%s]", item));
                        return null;
                    } else {
                        BeanUtils.copyProperties(item, optional.get());
                        return item;
                    }
                } catch (Exception ex) {
                    log.log(Level.ERROR, "exception", ex);
                    errors.add(ex.getLocalizedMessage());
                    return null;
                }
            }).filter(item -> !Objects.isNull(item)).toList();

            return new OperationResultDTO<>(repository.saveAll(validData), validData.size(), errors);
        } catch (Exception ex) {
            log.log(Level.ERROR, "exception", ex);
            return new OperationResultDTO<>(ex);
        } finally {
            log.info("Finished update many by SQL Operations...");
        }
    }

    @Override
    public OperationResultDTO<?> deleteOne(DeleteItemsCommand command) {
        return deleteMany(command);
    }

    @Override
    public OperationResultDTO<?> deleteMany(DeleteItemsCommand command) {
        log.info("Started delete many by SQL Operations...");
        try {
            if (Objects.isNull(command) || Objects.isNull(command.getIds())) {
                return new OperationResultDTO<>(new Exception("Payload can't be null"));
            }

            List<UUID> data = command.getIds().getData();
            if (Objects.isNull(data) || data.isEmpty()) {
                return new OperationResultDTO<>(new IllegalArgumentException("Empty Payload"));
            }

            data.forEach(repository::deleteById);
            return new OperationResultDTO<>(String.format("Deleted IDs:\n%s", data.stream().map(UUID::toString)
                    .collect(Collectors.joining("\n"))));
        } catch (Exception ex) {
            return new OperationResultDTO<>(ex);
        } finally {
            log.info("Finished delete many by SQL Operations...");
        }
    }

    @Override
    public OperationResultDTO<?> findOne(FindInterface<Q> command) {
        OperationResultDTO<?> many = findMany(command);
        return Objects.isNull(many) || Objects.isNull(many.getData()) || many.getData().isEmpty()
                ? new OperationResultDTO<>() : new OperationResultDTO<>(many.getData().get(0));
    }

    @Override
    public OperationResultDTO<?> findMany(FindInterface<Q> command) {
        try {
            return command.executeQuery();
        } catch (Exception ex) {
            return new OperationResultDTO<>(ex);
        } finally {
            log.info("Finished find many by SQL Operations...");
        }
    }

    private Optional<T> fetchDatabseItem(T data) throws NoSuchFieldException, IllegalAccessException {
        Field field = data.getClass().getDeclaredField("id");
        field.setAccessible(true);
        return repository.findById((UUID) field.get(data));
    }

    public R getRepository() {
        return repository;
    }
}
