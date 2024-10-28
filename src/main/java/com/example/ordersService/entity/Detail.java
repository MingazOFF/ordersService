package com.example.ordersService.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@EqualsAndHashCode
@Table("detail")
public class Detail {
    @Id()
    @Column("id_detail")
    private int id;

    private int productCode;
    private String name;

    private double quantity;
    private double unitPrice;

    private int id_order;



}
