package com.guilhermepalma.order_quarkus.dto.command;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Pageable;

@Builder
@Getter
public class FindItemsSQLCommand<T> {

    private Example<T> example;
    private Pageable page;

}
