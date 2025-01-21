package com.guilhermepalma.order_spring_boot.factory;

import com.guilhermepalma.order_spring_boot.dto.OperationResultDTO;
import com.guilhermepalma.order_spring_boot.dto.command.DeleteItemsCommand;
import com.guilhermepalma.order_spring_boot.dto.command.FindItemsByParametersCommand;
import com.guilhermepalma.order_spring_boot.dto.command.UpsertItemsCommand;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.webjars.NotFoundException;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Create an Operations by SQL Implementation
 *
 * @param <T> Entity Class
 * @param <U> Entity Type (If there is)
 * @param <R> Repository Class by JPA Repository
 */
@Log4j2
public class OperationsSQL<T, U, R extends JpaRepository<T, UUID>> implements Operations<T, U> {

    private U type;
    private R repository;

    @Override
    public OperationResultDTO<?> createOne(UpsertItemsCommand<T> command) {
        log.info("Started create one by SQL Operations...");
        try {
            if (Objects.isNull(command) || Objects.isNull(command.getPayload())) {
                return new OperationResultDTO<>(new NotFoundException("Payload can't be null"));
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
                return new OperationResultDTO<>(new NotFoundException("Payload can't be null"));
            }

            List<T> data = command.getPayload().getData();
            if (Objects.isNull(data)) {
                return new OperationResultDTO<>(new IllegalArgumentException("Empty Payload"));
            }

            Set<String> errors = Collections.synchronizedSet(new HashSet<>());
            List<T> validData = data.stream().map(item -> {
                if (fetchDatabseItem(item).isPresent()) {
                    errors.add(String.format("Item there is on Database. Item: [%s]", item.toString()));
                    return null;
                }
                return item;
            }).filter(item -> !Objects.isNull(item)).toList();

            return new OperationResultDTO<>(repository.saveAll(validData), validData.size(), errors);
        } catch (Exception ex) {
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
                return new OperationResultDTO<>(new NotFoundException("Payload can't be null"));
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
                return new OperationResultDTO<>(new NotFoundException("Payload can't be null"));
            }

            List<T> data = command.getPayload().getData();
            if (Objects.isNull(data)) {
                return new OperationResultDTO<>(new IllegalArgumentException("Empty Payload"));
            }

            Set<String> errors = Collections.synchronizedSet(new HashSet<>());
            List<T> validData = data.stream().map(item -> {
                if (fetchDatabseItem(item).isEmpty()) {
                    errors.add(String.format("Item there isn't on Database. Item: [%s]", item.toString()));
                    return null;
                }
                return item;
            }).filter(item -> !Objects.isNull(item)).toList();

            return new OperationResultDTO<>(repository.saveAll(validData), validData.size(), errors);
        } catch (Exception ex) {
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
                return new OperationResultDTO<>(new NotFoundException("Payload can't be null"));
            }

            List<UUID> data = command.getIds().getData();
            if (Objects.isNull(data) || data.isEmpty()) {
                return new OperationResultDTO<>(new IllegalArgumentException("Empty Payload"));
            }

            repository.deleteAllById(data);
            return new OperationResultDTO<>(String.format("Deleted IDs:\n%s", data.stream().map(UUID::toString)
                    .collect(Collectors.joining("\n"))));
        } catch (Exception ex) {
            return new OperationResultDTO<>(ex);
        } finally {
            log.info("Finished delete many by SQL Operations...");
        }
    }

    @Override
    public OperationResultDTO<?> findOne(FindItemsByParametersCommand<T> command) {
        return findMany(command);
    }

    @Override
    public OperationResultDTO<?> findMany(FindItemsByParametersCommand<T> command) {
        log.info("Started find many by SQL Operations...");
        try {
            if (Objects.isNull(command)) {
                return new OperationResultDTO<>(new NotFoundException("Payload can't be null"));
            }

            final Pageable pageable = Objects.isNull(command.getPage())
                    ? PageRequest.of(0, 10, Sort.by("id").ascending()) : command.getPage();
            final Example<T> example = command.getExample();

            final Page<T> pageReturned = Objects.isNull(example) ? repository.findAll(pageable) : repository.findAll(example, pageable);
            return new OperationResultDTO<>(pageReturned.getContent(), pageReturned.getTotalElements());

        } catch (Exception ex) {
            return new OperationResultDTO<>(ex);
        } finally {
            log.info("Finished find many by SQL Operations...");
        }
    }

    private Optional<T> fetchDatabseItem(T data) {
        return repository.findOne(Example.of(data, ExampleMatcher.matchingAll().withMatcher("id", ExampleMatcher.GenericPropertyMatchers.exact())));
    }
}
