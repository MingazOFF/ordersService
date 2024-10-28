package com.example.ordersService.util;

import com.example.ordersService.entity.Detail;
import com.example.ordersService.entity.Order;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class OrderWithDetailExtractor implements ResultSetExtractor<List<Order>> {
    @Override
    public List<Order> extractData(ResultSet rs) throws SQLException, DataAccessException {
        List<Order> orders = new ArrayList<>();
        while (rs.next()) {
            Order order = new Order();
            order.setId(rs.getInt("id"));
            order.setNumber(rs.getString("number"));
            order.setAmount(rs.getDouble("amount"));
            order.setDate(rs.getDate("date").toLocalDate());
            order.setConsumer(rs.getString("consumer"));
            order.setAddress(rs.getString("address"));
            order.setPaymentType(rs.getString("paymentType"));
            order.setDeliveryType(rs.getString("deliveryType"));
            order.setDetails(new HashSet<Detail>());

            if (!orders.contains(order)) {
                orders.add(order);
            }

            Detail detail = new Detail();
            detail.setId(rs.getInt("id_detail"));
            detail.setProductCode(rs.getInt("productcode"));
            detail.setName(rs.getString("name"));
            detail.setQuantity(rs.getDouble("quantity"));
            detail.setUnitPrice(rs.getDouble("unitPrice"));
            detail.setId_order(rs.getInt("id_order"));

            if (detail.getId() != 0) {
                orders.get(orders.indexOf(order)).getDetails().add(detail);
            }
        }
        return orders;
    }
}
