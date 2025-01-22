package com.guilhermepalma.order_quarkus.dto.command;

import com.guilhermepalma.order_quarkus.dto.PayloadDTO;
import lombok.*;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Setter
public class UpsertItemsCommand<T> {
    private PayloadDTO<T> payload;
    private String identifier;

    public UpsertItemsCommand(PayloadDTO<T> payload) {
        this.payload = payload;
    }
}
