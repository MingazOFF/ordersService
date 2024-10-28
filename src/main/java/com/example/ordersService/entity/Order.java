package com.example.ordersService.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.MappedCollection;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDate;
import java.util.Set;

@Data
@EqualsAndHashCode
@Table("order")
public class Order {

    @Id()
    private int id;

    private String number;

    private double amount;

    private LocalDate date;

    private String consumer;

    private String address;

    private String paymentType;

    private String deliveryType;

    @MappedCollection(idColumn = "id")
    @EqualsAndHashCode.Exclude
    private Set<Detail> details;

}
