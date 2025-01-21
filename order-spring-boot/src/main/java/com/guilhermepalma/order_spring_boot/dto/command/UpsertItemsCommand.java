package com.guilhermepalma.order_spring_boot.dto.command;

import com.guilhermepalma.order_spring_boot.dto.PayloadDTO;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class UpsertItemsCommand<T> {
    private final PayloadDTO<T> payload;
}
