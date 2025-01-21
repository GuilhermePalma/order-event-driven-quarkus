package com.guilhermepalma.order_spring_boot.repository;

import com.guilhermepalma.order_spring_boot.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {

}
