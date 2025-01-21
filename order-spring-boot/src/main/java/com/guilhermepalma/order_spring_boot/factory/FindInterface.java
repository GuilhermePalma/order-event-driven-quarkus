package com.guilhermepalma.order_spring_boot.factory;

import com.guilhermepalma.order_spring_boot.dto.OperationResultDTO;
import com.guilhermepalma.order_spring_boot.dto.command.FindItemsByParametersCommand;
import lombok.Getter;
import org.hibernate.tuple.Tuplizer;

@Getter
public abstract class FindInterface<Q> {
    private Q query;

    public abstract OperationResultDTO<?> executeQuery(Q queryParameters);

}
