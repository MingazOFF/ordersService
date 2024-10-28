package com.example.ordersService.dao;

import com.example.ordersService.entity.Detail;
import com.example.ordersService.entity.Order;
import com.example.ordersService.util.OrderWithDetailExtractor;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@AllArgsConstructor
public class OrderDAO {

    private final NamedParameterJdbcTemplate template;

    public int createOrder(Order order) {
        String sql = "INSERT INTO \"order\" " +
                "(number, amount, consumer, address, paymentType, deliveryType) VALUES " +
                "(:number, :amount, :consumer, :address, :paymentType, :deliveryType) " +
                "RETURNING ID";
        SqlParameterSource parameterSourceForOrder = new MapSqlParameterSource()
                .addValue("number", order.getNumber())
                .addValue("amount", order.getAmount())
                .addValue("consumer", order.getConsumer())
                .addValue("address", order.getAddress())
                .addValue("paymentType", order.getPaymentType())
                .addValue("deliveryType", order.getDeliveryType());
        int id = template.queryForObject(sql, parameterSourceForOrder, Integer.class);

        for (Detail detail : order.getDetails()) {
            String sqlDetail = "INSERT INTO detail " +
                    "(productcode, name, quantity, unitprice, id_order) VALUES " +
                    "(:productcode, :name, :quantity, :unitprice, :id_order) " +
                    "RETURNING id_detail";
            SqlParameterSource parameterSourceForDetail = new MapSqlParameterSource()
                    .addValue("productcode", detail.getProductCode())
                    .addValue("name", detail.getName())
                    .addValue("quantity", detail.getQuantity())
                    .addValue("unitprice", detail.getUnitPrice())
                    .addValue("id_order", id);
            template.queryForObject(sqlDetail, parameterSourceForDetail, Integer.class);
        }
        return id;
    }

    public Order getOrderById(int id) {
        String sql = "SELECT * FROM \"order\" " +
                "INNER JOIN \"detail\" ON \"order\".id = \"detail\".id_order " +
                "WHERE ID = :id";
        SqlParameterSource parameterSource = new MapSqlParameterSource().
                addValue("id", id);
        List<Order> orders = template.query(sql, parameterSource, new OrderWithDetailExtractor());
        return orders.get(0);
    }


    public List<Order> getAllOrders() {
        String sql = "SELECT * FROM \"order\" left join \"detail\" ON \"order\".id = detail.id_order";
        List<Order> allOrders = template.query(sql, new OrderWithDetailExtractor());
        return allOrders;
    }

    public List<Order> getOrdersWithDateAndAmountFilter(String date, double amount) {
        String sql = "SELECT * FROM \"order\" " +
                "WHERE \"date\" = to_date(:date,'YYYY-MM-DD') AND amount > :amount";
        SqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("date", date)
                .addValue("amount", amount);
        List<Order> orders = template.query(sql, parameterSource, new OrderWithDetailExtractor());
        return orders;
    }

    public List<Order> getOrdersWithProductAndFromToDateFilter(int code, String from, String to) {
//        String sql = "SELECT * FROM (SELECT \"order\".id AS id_temp FROM \"order\" EXCEPT " +
//                "(SELECT id_order FROM detail WHERE productcode = :productcode) " +
//                "ORDER BY id_temp) " +
//                "INNER JOIN \"order\" ON id_temp = \"order\".id " +
//                "INNER JOIN DETAIL ON \"order\".id = id_order " +
//                "WHERE DATE BETWEEN TO_DATE(:from,'YYYY-MM-DD') AND TO_DATE(:to,'YYYY-MM-DD')";

        String sql = "SELECT * FROM \"order\" " +
                "INNER JOIN \"detail\" ON \"order\".id = detail.id_order " +
                "WHERE date BETWEEN TO_DATE(:from,'YYYY-MM-DD') AND TO_DATE(:to,'YYYY-MM-DD') " +
                "AND NOT EXISTS ( " +
                "    SELECT 1 " +
                "    FROM \"detail\" " +
                "    WHERE \"detail\".id_order = \"order\".Id " +
                "    AND \"detail\".productcode = :productcode" +
                ")";
        SqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("productcode", code)
                .addValue("from", from)
                .addValue("to", to);
        List<Order> orders = template.query(sql, parameterSource, new OrderWithDetailExtractor());
        return orders;
    }

}