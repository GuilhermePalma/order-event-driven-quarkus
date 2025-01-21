package com.guilhermepalma.order_spring_boot.factory;


/**
 * Create an adpter to Acess Operations
 * @param <T> Entity Class
 * @param <U> Entity Type (If there is)
 */
public interface OperationsFactory<T, U> {
    Operations<T, U> createOperations();
}
