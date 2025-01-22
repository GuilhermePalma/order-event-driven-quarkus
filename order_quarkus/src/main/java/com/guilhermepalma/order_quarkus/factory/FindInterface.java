package com.guilhermepalma.order_quarkus.factory;

import com.guilhermepalma.order_quarkus.dto.OperationResultDTO;
import com.guilhermepalma.order_quarkus.dto.command.FindItemsByParametersCommand;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.hibernate.tuple.Tuplizer;

public abstract class FindInterface<Q> {
    public abstract Q getQuery();
    public abstract OperationResultDTO<?> executeQuery();

}
