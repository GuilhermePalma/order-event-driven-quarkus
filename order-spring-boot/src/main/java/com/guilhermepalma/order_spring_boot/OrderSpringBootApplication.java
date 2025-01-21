package com.guilhermepalma.order_spring_boot;

import com.guilhermepalma.order_spring_boot.factory.OperationSQLFactory;
import com.guilhermepalma.order_spring_boot.factory.Operations;
import com.guilhermepalma.order_spring_boot.factory.OperationsFactory;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@OpenAPIDefinition(info = @Info(
		title = "API Spring Boot - Producer, Consumer, Query of Orders",
		version = "0.0.1",
		description = "API usada para as Operações Server-Side, usando EventDriven e segregando a Logica do Kafka do Banco de Dados"
))
@SpringBootApplication
public class OrderSpringBootApplication {

	public static void main(String[] args) {
		SpringApplication.run(OrderSpringBootApplication.class, args);

		OperationsFactory<Void, Void> s = new OperationSQLFactory<>();
		Operations<Void, Void> s1 = s.createOperations();

	}

}
