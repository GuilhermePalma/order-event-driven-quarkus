package com.guilhermepalma.order_spring_boot.factory;

import com.guilhermepalma.order_spring_boot.dto.OperationResultDTO;

public interface FindInterface {
    OperationResultDTO<?> findMany();
}
