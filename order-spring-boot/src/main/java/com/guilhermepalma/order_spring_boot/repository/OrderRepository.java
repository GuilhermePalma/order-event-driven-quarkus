package com.guilhermepalma.order_spring_boot.repository;

import com.guilhermepalma.order_spring_boot.model.Order;
import com.guilhermepalma.order_spring_boot.type.StatusType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.*;

public interface OrderRepository extends JpaRepository<Order, UUID> {
    Page<Order> findAllByIsDeletedIsAndIdIn(boolean isDeleted, Collection<UUID> id, Pageable pageable);

    Page<Order> findAllByIsDeletedIsAndCustomerNameInAndProductInAndStatusIn(boolean isDeleted, Collection<String> customerName,
          Collection<String> product,Collection<StatusType> status, Pageable pageable);

    Page<Order> findAllByIsDeletedIsAndProductInAndStatusIn(boolean isDeleted, Collection<String> product, Collection<StatusType> status, Pageable pageable);

    Page<Order> findAllByIsDeletedIsAndCustomerNameInAndStatusIn(boolean isDeleted, Collection<String> product, Collection<StatusType> status, Pageable pageable);

    Page<Order> findAllByIsDeletedIsAndCustomerNameIn(boolean isDeleted, Collection<String> customerName, Pageable pageable);
    Page<Order> findAllByIsDeletedIsAndProductIn(boolean isDeleted, Collection<String> product, Pageable pageable);
    Page<Order> findAllByIsDeletedIsAndStatusIn(boolean isDeleted, Collection<StatusType> status, Pageable pageable);
}
