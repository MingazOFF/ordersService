package com.example.ordersService.dao;

import com.example.ordersService.entity.Order;
import com.example.ordersService.util.util;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;


@DataJdbcTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class OrderDAOTests {

    @Mock
    private NamedParameterJdbcTemplate template;
    @InjectMocks
    private OrderDAO orderDAO;


    @Test
    @DisplayName("Test save and return id functionality")
    public void createOrderTest() {
        //given
        int id = 1;
        Order order = util.getFirstOrder(id);
        BDDMockito.given(template.queryForObject(anyString(), any(SqlParameterSource.class), eq(Integer.class)))
                .willReturn(id);
        //when
        int idFromDAO = orderDAO.createOrder(order);
        //then
        assertEquals(idFromDAO, id);
    }

    @Test
    @DisplayName("Test return order by id functionality")
    public void getOrderByIdTest() {
        //given
        int id = 1;
        Order order = util.getFirstOrder(id);
        List<Order> orders = new ArrayList<>();
        orders.add(order);

        BDDMockito.given(template.query(anyString(), any(SqlParameterSource.class), any(ResultSetExtractor.class)))
                .willReturn(orders);
        //when
        Order returnedOrder = orderDAO.getOrderById(id);
        //then
        assertEquals(returnedOrder.getId(), returnedOrder.getId());
        assertEquals(returnedOrder.getConsumer(), returnedOrder.getConsumer());
    }

    @Test
    @DisplayName("Test return all order functionality")
    public void getAllOrdersTest() {
        //given
        int id1 = 1;
        int id2 = 2;
        Order order1 = util.getFirstOrder(id1);
        Order order2 = util.getSecondOrder(id2);
        List<Order> ordersToSave;
        ordersToSave = new ArrayList<>(List.of(order1, order2));
        BDDMockito.given(template.query(anyString(), any(ResultSetExtractor.class)))
                .willReturn(ordersToSave);
        //when
        List<Order> ordersFromDB = orderDAO.getAllOrders();
        //then
        assertThat(ordersFromDB).isNotNull();
        assertThat(CollectionUtils.isEmpty(ordersFromDB)).isFalse();
        assertThat(ordersToSave.size()).isEqualTo(ordersFromDB.size());
    }


    @Test
    @DisplayName("Test return order with amount and date filter functionality")
    public void ggetOrdersWithDateAndAmountFilterTest() {
        //given
        int id1 = 1;
        int id2 = 2;
        Order order1 = util.getFirstOrder(id1);
        Order order2 = util.getSecondOrder(id2);
        List<Order> orders;
        orders = new ArrayList<>(List.of(order1, order2));
        BDDMockito.given(template.query(anyString(),any(SqlParameterSource.class), any(ResultSetExtractor.class)))
                .willReturn(orders);
        String date = "2024-10-22";
        double amount = 10;
        //when
        List<Order> ordersFromDB = orderDAO.getOrdersWithDateAndAmountFilter(date,amount);
        //then
        assertThat(ordersFromDB).isNotNull();
        assertThat(CollectionUtils.isEmpty(ordersFromDB)).isFalse();
        assertThat(orders.size()).isEqualTo(ordersFromDB.size());
    }

    @Test
    @DisplayName("Test return order with product code and date filter functionality")
    public void getOrdersWithProductAndFromToDateFilterTest() {
        //given
        int id1 = 1;
        int id2 = 2;
        Order order1 = util.getFirstOrder(id1);
        Order order2 = util.getSecondOrder(id2);
        List<Order> orders;
        orders = new ArrayList<>(List.of(order1, order2));
        BDDMockito.given(template.query(anyString(),any(SqlParameterSource.class), any(ResultSetExtractor.class)))
                .willReturn(orders);
        int productCode = 111;
        String from = "2024-10-20";
        String to = "2024-10-22";
        //when
        List<Order> ordersFromDB = orderDAO.getOrdersWithProductAndFromToDateFilter(productCode,from,to);
        //then
        assertThat(ordersFromDB).isNotNull();
        assertThat(CollectionUtils.isEmpty(ordersFromDB)).isFalse();
        assertThat(orders.size()).isEqualTo(ordersFromDB.size());
    }
}