package com.guilhermepalma.order_spring_boot.dto.command;

import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Builder
@Getter
public class FindOrderByParametersCommand {

    private final Set<UUID> id;
    private final Set<String> name;
    private final List<Object> type;
    private final Boolean isDeleted;

    private int pageNumber;
    private int pageSize;

}
