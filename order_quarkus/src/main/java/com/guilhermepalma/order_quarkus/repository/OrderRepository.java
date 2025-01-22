package com.guilhermepalma.order_quarkus.repository;

import com.guilhermepalma.order_quarkus.model.Order;
import com.guilhermepalma.order_quarkus.type.StatusType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {
    Page<Order> findByisDeletedIsAndIdIn(boolean isDeleted, Collection<UUID> id, Pageable pageable);

    Page<Order> findByisDeletedIsAndCustomerNameInAndProductInAndStatusIn(boolean isDeleted, Collection<String> customerName,
                                                                          Collection<String> product, Collection<StatusType> status, Pageable pageable);

    Page<Order> findByisDeletedIsAndProductInAndStatusIn(boolean isDeleted, Collection<String> product, Collection<StatusType> status, Pageable pageable);

    Page<Order> findByisDeletedIsAndCustomerNameInAndStatusIn(boolean isDeleted, Collection<String> product, Collection<StatusType> status, Pageable pageable);

    Page<Order> findByisDeletedIsAndCustomerNameIn(boolean isDeleted, Collection<String> customerName, Pageable pageable);

    Page<Order> findByisDeletedIsAndProductIn(boolean isDeleted, Collection<String> product, Pageable pageable);

    Page<Order> findByisDeletedIsAndStatusIn(boolean isDeleted, Collection<StatusType> status, Pageable pageable);

    Page<Order> findByisDeletedIs(boolean isDeleted, Pageable pageable);
}
