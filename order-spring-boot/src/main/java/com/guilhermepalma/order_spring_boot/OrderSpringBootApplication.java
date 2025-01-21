package com.guilhermepalma.order_spring_boot;

import com.guilhermepalma.order_spring_boot.factory.OperationSQLFactory;
import com.guilhermepalma.order_spring_boot.factory.Operations;
import com.guilhermepalma.order_spring_boot.factory.OperationsFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class OrderSpringBootApplication {

	public static void main(String[] args) {
		SpringApplication.run(OrderSpringBootApplication.class, args);

		OperationsFactory<Void> s = new OperationSQLFactory<>();
		Operations<Void> s1 = s.createOperations();

	}

}
