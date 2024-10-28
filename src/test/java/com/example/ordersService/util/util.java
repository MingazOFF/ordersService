package com.example.ordersService.util;

import com.example.ordersService.entity.Detail;
import com.example.ordersService.entity.Order;

import java.util.HashSet;

public class util {
    public static Order getFirstOrder(int id) {
        Order order = new Order();
        order.setId(id);
        order.setNumber("1000120241212");
        order.setAmount(45.78);
        order.setAddress("Tokio");
        order.setConsumer("Pasha");
        order.setDeliveryType("pickup");
        order.setPaymentType("cash");

        Detail detail1 = new Detail();
        detail1.setProductCode(555);
        detail1.setName("carrot");
        detail1.setQuantity(7.9);
        detail1.setUnitPrice(8);
        detail1.setId_order(id);

        Detail detail2 = new Detail();
        detail2.setProductCode(444);
        detail2.setName("potato");
        detail2.setQuantity(0.9);
        detail2.setUnitPrice(7);
        detail2.setId_order(id);

        order.setDetails(new HashSet<Detail>());
        order.getDetails().add(detail1);
        order.getDetails().add(detail2);
        return order;

    }


    public static Order getSecondOrder(int id) {
        Order order = new Order();
        order.setId(id);
        order.setNumber("2000220241212");
        order.setAmount(45.78);
        order.setAddress("Deli");
        order.setConsumer("Radzha");
        order.setDeliveryType("pickup");
        order.setPaymentType("card");

        Detail detail1 = new Detail();
        detail1.setProductCode(555);
        detail1.setName("carrot");
        detail1.setQuantity(7.9);
        detail1.setUnitPrice(8);
        detail1.setId_order(id);

        Detail detail2 = new Detail();
        detail2.setProductCode(111);
        detail2.setName("banana");
        detail2.setQuantity(0.9);
        detail2.setUnitPrice(7);
        detail2.setId_order(id);

        order.setDetails(new HashSet<Detail>());
        order.getDetails().add(detail1);
        order.getDetails().add(detail2);
        return order;
    }
}
