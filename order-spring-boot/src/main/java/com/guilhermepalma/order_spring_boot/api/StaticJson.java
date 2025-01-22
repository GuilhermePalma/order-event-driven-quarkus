package com.guilhermepalma.order_spring_boot.api;

public class StaticJson {
    public static final String EXAMPLE_1_REGISTER_ORDER = """
            {
              "data": [
                {
                  "customerName": "Joana Viera",
                  "product": "Smart TV 42" AOC Full HD",
                  "quantity": 2,
                  "status": "PENDING",
                  "isDeleted": false
                }
              ]
            }""";

    public static final String EXAMPLE_2_REGISTER_ORDER = """
            {
              "data": [
                {
                  "customerName": "Ricardo Jose",
                  "product": "Coca-Cola Pet 2l",
                  "quantity": 12,
                  "status": "SHIPPED",
                  "isDeleted": false
                }
              ]
            }""";

    public static final String EXAMPLE_3_REGISTER_ORDER = """
            {
              "data": [
                {
                  "customerName": "Vinícola Irriga",
                  "product": "Kit Taça 750Ml 8 Und. Cristal",
                  "quantity": 1,
                  "status": "CONFIRMED",
                  "isDeleted": false
                }
              ]
            }""";

    public static final String EXAMPLE_4_REGISTER_ORDER = """
            {
              "data": [
                {
                  "customerName": "MicroInfo Mat",
                  "product": "SmartWatch Samsumg 6",
                  "quantity": 1,
                  "status": "DELIVERED",
                  "isDeleted": false
                }
              ]
            }""";

}
