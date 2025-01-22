package com.guilhermepalma.order_quarkus.api;

import com.guilhermepalma.order_quarkus.Util;
import com.guilhermepalma.order_quarkus.dto.OperationResultDTO;
import com.guilhermepalma.order_quarkus.dto.PayloadDTO;
import com.guilhermepalma.order_quarkus.dto.command.FindOrderByParametersCommand;
import com.guilhermepalma.order_quarkus.dto.command.UpsertItemsCommand;
import com.guilhermepalma.order_quarkus.factory.FindInterface;
import com.guilhermepalma.order_quarkus.factory.Operations;
import com.guilhermepalma.order_quarkus.model.Order;
import com.guilhermepalma.order_quarkus.services.producer.OrderEventProducer;
import com.guilhermepalma.order_quarkus.services.query.OrderQueryHandler;
import com.guilhermepalma.order_quarkus.type.StatusType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collections;
import java.util.Set;

@Path("/api/v1/order")
@Tag(name = "Order", description = "Endpoint to manipulate and manage orders and their data")
public class OrderRestEndpoint {

    @Inject
    OrderEventProducer producer;
    @Inject
    OrderQueryHandler queryHandler;

//    public OrderRestEndpoint(OrderEventProducer producer, OrderQueryHandler query) {
//        this.queryHandler = query;
//        this.producer = producer;
//    }

    @Operation(summary = "Create Orders values", description = "Used only for Register new Items",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    examples = {
                            @ExampleObject(name = "Insert Example - 01", description = "Create Value on Database", value = StaticJson.EXAMPLE_1_REGISTER_ORDER),
                            @ExampleObject(name = "Insert Example - 02", description = "Create Value on Database", value = StaticJson.EXAMPLE_2_REGISTER_ORDER),
                            @ExampleObject(name = "Insert Example - 03", description = "Create Value on Database", value = StaticJson.EXAMPLE_3_REGISTER_ORDER),
                            @ExampleObject(name = "Insert Example - 04", description = "Create Value on Database", value = StaticJson.EXAMPLE_4_REGISTER_ORDER)
                    }
            ))
    )
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "Orders Created", content = @Content(
            mediaType = "application/json", schema = @Schema(implementation = OperationResultDTO.class))))
    @Produces(MediaType.APPLICATION_JSON_VALUE)
    @Consumes(MediaType.APPLICATION_JSON_VALUE)
    @POST
    public ResponseEntity<OperationResultDTO<?>> insertOrder(@RequestBody PayloadDTO<Order> orderPayload) {
        UpsertItemsCommand<Order> command = UpsertItemsCommand.<Order>builder().payload(orderPayload).build();
        return new ResponseEntity<>(producer.createMany(command), HttpStatus.OK);
    }

    @Operation(summary = "Update Orders values", description = "Used only for Update Items. WARMING: It's necessary there is ID on Items (Exemples isn't).  Recommendation: Copy Values of GET API and Change Parameters",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    examples = {
                            @ExampleObject(name = "Update Example - 01", description = "Update Value on Database (If there is ID)", value = StaticJson.EXAMPLE_1_REGISTER_ORDER),
                            @ExampleObject(name = "Update Example - 02", description = "Update Value on Database (If there is ID)", value = StaticJson.EXAMPLE_2_REGISTER_ORDER),
                            @ExampleObject(name = "Update Example - 03", description = "Update Value on Database (If there is ID)", value = StaticJson.EXAMPLE_3_REGISTER_ORDER),
                            @ExampleObject(name = "Update Example - 04", description = "Update Value on Database (If there is ID)", value = StaticJson.EXAMPLE_4_REGISTER_ORDER)
                    }
            ))
    )
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "Orders Updated", content = @Content(
            mediaType = "application/json", schema = @Schema(implementation = OperationResultDTO.class))))
    @Produces(MediaType.APPLICATION_JSON_VALUE)
    @Consumes(MediaType.APPLICATION_JSON_VALUE)
    @PUT
    public ResponseEntity<OperationResultDTO<?>> updateOrder(@RequestBody PayloadDTO<Order> orderPayload) {
        UpsertItemsCommand<Order> command = UpsertItemsCommand.<Order>builder().payload(orderPayload).build();
        return new ResponseEntity<>(producer.updateMany(command), HttpStatus.OK);
    }

    @Operation(summary = "Get database Order Values by Many Fields Values")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "Matching  order values",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = OperationResultDTO.class))))
    @Produces(MediaType.APPLICATION_JSON_VALUE)
    @GET
    public ResponseEntity<OperationResultDTO<?>> getOrderManyParameters(
            @RequestParam(required = false) Set<String> id,
            @Parameter(description = "Name of Product that will be searched", name = "product") @RequestParam(required = false) Set<String> product,
            @Parameter(description = "Name of Customer Responsible for the Product", name = "customerName") @RequestParam(required = false) Set<String> customerName,
            @Parameter(description = "Status of orders that will be searched", name = "status") @RequestParam(required = false) Set<StatusType> status,
            @Parameter(description = "Number of Page", name = "pageNumber") @RequestParam(required = false, defaultValue = "0") int pageNumber,
            @Parameter(description = "Items Number per Page", name = "pageSize") @RequestParam(required = false, defaultValue = "10") int pageSize,
            @RequestParam(required = false) boolean isDeleted
    ) {
        final FindOrderByParametersCommand find = FindOrderByParametersCommand.builder().isDeleted(isDeleted)
                .id(Util.convertStringToUUID((id))).type(status).name(product).customerName(customerName)
                .pageNumber(pageNumber).pageSize(pageSize)
                .build();

        return new ResponseEntity<>(queryHandler.findMany(new FindInterface<>() {
            @Override
            public FindOrderByParametersCommand getQuery() {
                return find;
            }

            @Override
            public OperationResultDTO<?> executeQuery() {
                return null;
            }
        }), HttpStatus.OK);
    }

    @Operation(summary = "Get database Order Values by One Field Value")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "Matching  order values",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = OperationResultDTO.class))))
    @Produces(MediaType.APPLICATION_JSON_VALUE)
    @Path(value = "/simple")
    @GET
    public ResponseEntity<OperationResultDTO<?>> getOrder(
            @RequestParam(required = false) String id,
            @Parameter(description = "Name of Product that will be searched", name = "product") @RequestParam(required = false) String product,
            @Parameter(description = "Name of Customer Responsible for the Product", name = "customerName") @RequestParam(required = false) String customerName,
            @Parameter(description = "Status of orders that will be searched", name = "status") @RequestParam(required = false) StatusType status,
            @Parameter(description = "Number of Page", name = "pageNumber") @RequestParam(required = false, defaultValue = "0") int pageNumber,
            @Parameter(description = "Items Number per Page", name = "pageSize") @RequestParam(required = false, defaultValue = "10") int pageSize,
            @RequestParam(required = false) boolean isDeleted
    ) {
        final FindOrderByParametersCommand find = FindOrderByParametersCommand.builder().isDeleted(isDeleted)
                .id(Collections.singleton(Util.convertStringToUUID((id)))).type(Collections.singleton(status))
                .name(Collections.singleton(product)).customerName(Collections.singleton(customerName))
                .pageNumber(pageNumber).pageSize(pageSize)
                .build();

        return new ResponseEntity<>(queryHandler.findMany(new FindInterface<>() {
            @Override
            public FindOrderByParametersCommand getQuery() {
                return find;
            }

            @Override
            public OperationResultDTO<?> executeQuery() {
                return null;
            }
        }), HttpStatus.OK);
    }

}
