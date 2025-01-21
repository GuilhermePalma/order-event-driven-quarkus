package com.guilhermepalma.order_spring_boot.factory;


/**
 * Create an adpter to Acess Operations
 */
public interface OperationsFactory<T, Q> {
    Operations<T, Q> createOperations();
}
