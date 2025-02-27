package com.guilhermepalma.order_spring_boot;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.scheduling.annotation.EnableAsync;

@OpenAPIDefinition(info = @Info(
		title = "API Spring Boot - Producer, Consumer, Query of Orders",
		version = "0.0.1",
		description = "API usada para as Operações Server-Side, usando EventDriven e segregando a Logica do Kafka do Banco de Dados"
))
@SpringBootApplication
@EnableKafka // Set Kafka in Spring Boot Project
@EnableAsync // Enable works async
public class OrderSpringBootApplication {

	public static void main(String[] args) {
		SpringApplication.run(OrderSpringBootApplication.class, args);
	}

}
