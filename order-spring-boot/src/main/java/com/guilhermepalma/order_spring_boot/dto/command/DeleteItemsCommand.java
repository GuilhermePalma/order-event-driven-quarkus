package com.guilhermepalma.order_spring_boot.dto.command;

import com.guilhermepalma.order_spring_boot.dto.PayloadDTO;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Builder
@Getter
public class DeleteItemsCommand {
    private final PayloadDTO<UUID> ids;
}
