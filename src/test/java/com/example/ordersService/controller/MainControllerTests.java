package com.example.ordersService.controller;

import com.example.ordersService.dao.OrderDAO;
import com.example.ordersService.entity.Order;
import com.example.ordersService.service.URLService;
import com.example.ordersService.util.util;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@WebMvcTest
public class MainControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private OrderDAO orderDAO;

    @MockBean
    private RestTemplate restTemplate;

    @MockBean
    private URLService urlService;


    @Test
    @DisplayName("Test creating order and id returning functionality")
    public void createOrderTest() throws Exception {
        //given
        int id = 1;
        Order order = util.getFirstOrder(id);

        BDDMockito.given(urlService.getNumberGenerateServiceURL()).willReturn("http://localhost:8091/numbers");
        BDDMockito.given(restTemplate.getForEntity("http://localhost:8091/numbers", String.class))
                .willReturn(new ResponseEntity<String>("1111120241025", HttpStatusCode.valueOf(200)));
        BDDMockito.given(orderDAO.createOrder(any(Order.class))).willReturn(id);
        //when
        ResultActions resultActions = mockMvc.perform(post("/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(order)));
        //then
        resultActions
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(String.valueOf(id)));
    }

    @Test
    @DisplayName("Test order returning by id functionality")
    public void getOrderByIdTest() throws Exception {
        //given
        int id = 1;
        Order order = util.getFirstOrder(id);
        BDDMockito.given(orderDAO.getOrderById(anyInt())).willReturn(order);
        // when
        ResultActions resultActions = mockMvc.perform(get("/orders?id=" + id));
        //then
        resultActions
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", CoreMatchers.is(id)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.date", CoreMatchers.is(order.getDate())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.consumer", CoreMatchers.is(order.getConsumer())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.details[0].id_order", CoreMatchers.is(order.getId())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.details[1].id_order", CoreMatchers.is(order.getId())));
    }

    @Test
    @DisplayName("Test order returning by id functionality")
    public void getAllOrdersTest() throws Exception {
        //given
        int id1 = 1;
        int id2 = 2;
        Order order1 = util.getFirstOrder(id1);
        Order order2 = util.getSecondOrder(id2);
        List<Order> orders = List.of(order1, order2);

        BDDMockito.given(orderDAO.getAllOrders()).willReturn(orders);
        // when
        ResultActions resultActions = mockMvc.perform(get("/orders/all"));
        //then
        resultActions
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id", CoreMatchers.is(order1.getId())))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].date", CoreMatchers.is(order1.getDate())))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].consumer", CoreMatchers.is(order1.getConsumer())))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].details[0].id_order", CoreMatchers.is(order1.getId())))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].details[1].id_order", CoreMatchers.is(order1.getId())))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id", CoreMatchers.is(order2.getId())))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].date", CoreMatchers.is(order2.getDate())))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].consumer", CoreMatchers.is(order2.getConsumer())))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].details[0].id_order", CoreMatchers.is(order2.getId())))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].details[1].id_order", CoreMatchers.is(order2.getId())));
    }

    @Test
    @DisplayName("Test date and amount filter order returning functionality")
    public void getOrdersWithDateAndAmountFilterTest() throws Exception {
        //given
        int id1 = 1;
        int id2 = 2;
        Order order1 = util.getFirstOrder(id1);
        Order order2 = util.getSecondOrder(id2);
        List<Order> orders = List.of(order1, order2);

        BDDMockito.given(orderDAO.getOrdersWithDateAndAmountFilter(anyString(), anyDouble())).willReturn(orders);
        // when
        ResultActions resultActions = mockMvc.perform(get("/orders/filter1?date=2024-12-22&amount=29.5"));
        //then
        resultActions
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id", CoreMatchers.is(order1.getId())))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].date", CoreMatchers.is(order1.getDate())))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].consumer", CoreMatchers.is(order1.getConsumer())))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].details[0].id_order", CoreMatchers.is(order1.getId())))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].details[1].id_order", CoreMatchers.is(order1.getId())))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id", CoreMatchers.is(order2.getId())))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].date", CoreMatchers.is(order2.getDate())))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].consumer", CoreMatchers.is(order2.getConsumer())))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].details[0].id_order", CoreMatchers.is(order2.getId())))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].details[1].id_order", CoreMatchers.is(order2.getId())));
    }

    @Test
    @DisplayName("Test product and date filter order returning functionality")
    public void getOrdersWithProductAndFromToDateFilterTest() throws Exception {
        //given
        int id1 = 1;
        int id2 = 2;
        Order order1 = util.getFirstOrder(id1);
        Order order2 = util.getSecondOrder(id2);
        List<Order> orders = List.of(order1, order2);

        BDDMockito.given(orderDAO.getOrdersWithProductAndFromToDateFilter(anyInt(), anyString(), anyString())).willReturn(orders);
        // when
        ResultActions resultActions = mockMvc.perform(get("/orders/filter2?code=111&from=2024-12-20&to=2024-12-22"));
        //then
        resultActions
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id", CoreMatchers.is(order1.getId())))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].date", CoreMatchers.is(order1.getDate())))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].consumer", CoreMatchers.is(order1.getConsumer())))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].details[0].id_order", CoreMatchers.is(order1.getId())))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].details[1].id_order", CoreMatchers.is(order1.getId())))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id", CoreMatchers.is(order2.getId())))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].date", CoreMatchers.is(order2.getDate())))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].consumer", CoreMatchers.is(order2.getConsumer())))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].details[0].id_order", CoreMatchers.is(order2.getId())))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].details[1].id_order", CoreMatchers.is(order2.getId())));
    }


}
