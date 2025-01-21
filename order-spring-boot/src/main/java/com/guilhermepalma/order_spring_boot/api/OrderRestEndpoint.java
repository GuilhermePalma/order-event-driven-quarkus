package com.guilhermepalma.order_spring_boot.api;

import com.guilhermepalma.order_spring_boot.Util;
import com.guilhermepalma.order_spring_boot.dto.OperationResultDTO;
import com.guilhermepalma.order_spring_boot.dto.PayloadDTO;
import com.guilhermepalma.order_spring_boot.dto.command.FindOrderByParametersCommand;
import com.guilhermepalma.order_spring_boot.dto.command.UpsertItemsCommand;
import com.guilhermepalma.order_spring_boot.factory.FindInterface;
import com.guilhermepalma.order_spring_boot.model.Order;
import com.guilhermepalma.order_spring_boot.services.producer.OrderEventProducer;
import com.guilhermepalma.order_spring_boot.services.query.OrderQueryHandler;
import com.guilhermepalma.order_spring_boot.type.StatusType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Set;

@RestController
@Tag(name = "Order", description = "Endpoint to manipulate and manage orders and their data")
public class OrderRestEndpoint {

    private final OrderEventProducer orderService;
    private final OrderQueryHandler orderQueryHandler;

    public OrderRestEndpoint(OrderEventProducer orderService, OrderQueryHandler orderQueryHandler) {
        this.orderService = orderService;
        this.orderQueryHandler = orderQueryHandler;
    }

    @Operation(summary = "Create Orders values", description = "Used only for Register new Items")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "Orders Created", content = @Content(
            mediaType = "application/json", schema = @Schema(implementation = OperationResultDTO.class))))
    @PostMapping(value = "api/v1/order", produces = "application/json", consumes = "application/json")
    public ResponseEntity<OperationResultDTO<?>> insertOrder(@RequestBody PayloadDTO<Order> orderPayload) {
        UpsertItemsCommand<Order> command = UpsertItemsCommand.<Order>builder().payload(orderPayload).build();
        return new ResponseEntity<>(orderService.insertOrder(command), HttpStatus.OK);
    }

    @Operation(summary = "Update Orders values", description = "Used only for Update Items")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "Orders Updated", content = @Content(
            mediaType = "application/json", schema = @Schema(implementation = OperationResultDTO.class))))
    @PostMapping(value = "api/v1/order", produces = "application/json", consumes = "application/json")
    public ResponseEntity<OperationResultDTO<?>> updateOrder(@RequestBody PayloadDTO<Order> orderPayload) {
        UpsertItemsCommand<Order> command = UpsertItemsCommand.<Order>builder().payload(orderPayload).build();
        return new ResponseEntity<>(orderService.updateOrder(command), HttpStatus.OK);
    }

    @Operation(summary = "Get database Order Values by Many Fields Values")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "Matching  order values",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = OperationResultDTO.class))))
    @GetMapping(value = "api/v1/order", produces = "application/json")
    public ResponseEntity<OperationResultDTO<?>> getOrderManyParameters(
            @RequestParam(required = false) Set<String> id,
            @Parameter(description = "Name of Product that will be searched", name = "product") @RequestParam(required = false) Set<String> name,
            @Parameter(description = "Name of Customer Responsible for the Product", name = "customerName") @RequestParam(required = false) Set<String> customerName,
            @Parameter(description = "Status of orders that will be searched", name = "status",
                    schema = @Schema(implementation = StatusType.class), examples = {
                    @ExampleObject(name = "PENDING", value = "PENDING"),
                    @ExampleObject(name = "CONFIRMED", value = "CONFIRMED"),
                    @ExampleObject(name = "SHIPPED", value = "SHIPPED"),
                    @ExampleObject(name = "DELIVERED", value = "DELIVERED"),
            }) @RequestParam Set<StatusType> status,
            @Parameter(description = "Number of Page", name = "pageNumber") @RequestParam(required = false, defaultValue = "0") int pageNumber,
            @Parameter(description = "Items Number per Page", name = "pageSize") @RequestParam(required = false, defaultValue = "10") int pageSize,
            @RequestParam(required = false) boolean isDeleted
    ) {
        final FindOrderByParametersCommand find = FindOrderByParametersCommand.builder().isDeleted(isDeleted)
                .id(Util.convertStringToUUID((id))).type(status).name(name).customerName(customerName)
                .pageNumber(pageNumber).pageSize(pageSize)
                .build();

        return new ResponseEntity<>(orderQueryHandler.findOne(new FindInterface<>(find) {
            @Override
            public OperationResultDTO<?> executeQuery(FindOrderByParametersCommand queryParameters) {
                return null;
            }
        }), HttpStatus.OK);
    }

    @Operation(summary = "Get database Order Values by One Field Value")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "Matching  order values",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = OperationResultDTO.class))))
    @GetMapping(value = "api/v1/order/simple", produces = "application/json")
    public ResponseEntity<OperationResultDTO<?>> getOrder(
            @RequestParam(required = false) String id,
            @Parameter(description = "Name of Product that will be searched", name = "product") @RequestParam(required = false) String name,
            @Parameter(description = "Name of Customer Responsible for the Product", name = "customerName") @RequestParam(required = false) String customerName,
            @Parameter(description = "Status of orders that will be searched", name = "status",
                    schema = @Schema(implementation = StatusType.class), examples = {
                    @ExampleObject(name = "PENDING", value = "PENDING"),
                    @ExampleObject(name = "CONFIRMED", value = "CONFIRMED"),
                    @ExampleObject(name = "SHIPPED", value = "SHIPPED"),
                    @ExampleObject(name = "DELIVERED", value = "DELIVERED"),
            }) @RequestParam StatusType status,
            @Parameter(description = "Number of Page", name = "pageNumber") @RequestParam(required = false, defaultValue = "0") int pageNumber,
            @Parameter(description = "Items Number per Page", name = "pageSize") @RequestParam(required = false, defaultValue = "10") int pageSize,
            @RequestParam(required = false) boolean isDeleted
    ) {
        final FindOrderByParametersCommand find = FindOrderByParametersCommand.builder().isDeleted(isDeleted)
                .id(Collections.singleton(Util.convertStringToUUID((id)))).type(Collections.singleton(status))
                .name(Collections.singleton(name)).customerName(Collections.singleton(customerName))
                .pageNumber(pageNumber).pageSize(pageSize)
                .build();

        return new ResponseEntity<>(orderQueryHandler.findOne(new FindInterface<>(find) {
            @Override
            public OperationResultDTO<?> executeQuery(FindOrderByParametersCommand queryParameters) {
                return null;
            }
        }), HttpStatus.OK);
    }

}
