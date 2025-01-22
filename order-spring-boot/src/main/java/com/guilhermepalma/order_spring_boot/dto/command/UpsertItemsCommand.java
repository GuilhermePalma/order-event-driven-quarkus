package com.guilhermepalma.order_spring_boot.dto.command;

import com.guilhermepalma.order_spring_boot.dto.PayloadDTO;
import lombok.*;
import org.springframework.data.annotation.PersistenceCreator;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Setter
public class UpsertItemsCommand<T> {
    private PayloadDTO<T> payload;
    private String identifier;

    @PersistenceCreator
    public UpsertItemsCommand(PayloadDTO<T> payload) {
        this.payload = payload;
    }
}
