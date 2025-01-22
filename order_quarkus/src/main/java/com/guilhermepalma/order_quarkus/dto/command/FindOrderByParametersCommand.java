package com.guilhermepalma.order_quarkus.dto.command;

import com.guilhermepalma.order_quarkus.type.StatusType;
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
    private final Set<String> customerName;
    private final Set<StatusType> type;
    private final Boolean isDeleted;

    private int pageNumber;
    private int pageSize;

}
