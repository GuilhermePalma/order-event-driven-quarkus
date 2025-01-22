package com.guilhermepalma.order_spring_boot.model;

import com.guilhermepalma.order_spring_boot.type.StatusType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity(name = "TB_ORDER")
@ToString
public class Order {

    // Default class values
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(unique = true, nullable = false, columnDefinition = "uuid")
    private UUID id;

    @Schema(description = "Name of Customer", example = "JOAO")
    @Column(nullable = false)
    private String customerName;

    @Schema(description = "Name of Product", example = "DESKTOP I3 - 918391")
    @Column(nullable = false)
    private String product;

    @Schema(description = "Quantity of Product", example = "3")
    @Column(nullable = false)
    private int quantity;

    @Schema(description = "Status of Product", example = "PENDING")
    @Column(nullable = false)
    private StatusType status;

    @Schema(description = "defines if the register is deleted. this tag allows you to retrieve possible excluded cases", defaultValue = "false")
    private Boolean isDeleted = Boolean.FALSE;

}

