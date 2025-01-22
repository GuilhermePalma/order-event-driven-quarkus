package com.guilhermepalma.order_quarkus.dto.command;

import com.guilhermepalma.order_quarkus.dto.PayloadDTO;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Builder
@Getter
public class DeleteItemsCommand {
    private final PayloadDTO<UUID> ids;
}
