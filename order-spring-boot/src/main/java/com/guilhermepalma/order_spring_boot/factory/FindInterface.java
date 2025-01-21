package com.guilhermepalma.order_spring_boot.factory;

import com.guilhermepalma.order_spring_boot.dto.OperationResultDTO;
import com.guilhermepalma.order_spring_boot.dto.command.FindItemsByParametersCommand;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.hibernate.tuple.Tuplizer;

@Getter
@AllArgsConstructor
public abstract class FindInterface<Q> {
    private Q query;

    public abstract OperationResultDTO<?> executeQuery(Q queryParameters);

}
