package com.guilhermepalma.order_quarkus.dto.command;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Builder
@Getter
public class FindItemsByParametersCommand {

    private final Set<UUID> id;
    private final Set<String> name;
    private final List<Object> type;
    private final Boolean isDeleted;

    private int pageNumber;
    private int pageSize;

}
