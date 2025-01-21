package com.guilhermepalma.order_spring_boot.dto.command;

import lombok.Builder;
import lombok.Getter;

import java.util.Set;
import java.util.UUID;

@Builder
@Getter
public class FindItemsByParametersCommand<T> {
    private final Set<UUID> id;
    private final Set<String> name;
    private final Set<String> uniqueKey;
    private final T type;
    private final Boolean isDeleted;
}
