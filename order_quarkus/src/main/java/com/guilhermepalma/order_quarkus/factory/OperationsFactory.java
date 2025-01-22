package com.guilhermepalma.order_quarkus.factory;


/**
 * Create an adpter to Acess Operations
 */
public interface OperationsFactory<T, Q> {
    Operations<T, Q> createOperations();
}
