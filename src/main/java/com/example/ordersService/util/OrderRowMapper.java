package com.example.ordersService.util;

import com.example.ordersService.entity.Order;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;


public class OrderRowMapper implements RowMapper<Order> {

    @Override
    public Order mapRow(ResultSet rs, int rowNum) throws SQLException {
        Order order = new Order();
        order.setId(rs.getInt("id"));
        order.setNumber(rs.getString("number"));
        order.setAmount(rs.getDouble("amount"));
        order.setDate(rs.getDate("date").toLocalDate());
        order.setConsumer(rs.getString("consumer"));
        order.setAddress(rs.getString("address"));
        order.setPaymentType(rs.getString("paymentType"));
        order.setDeliveryType(rs.getString("deliveryType"));

        return order;
    }
}
