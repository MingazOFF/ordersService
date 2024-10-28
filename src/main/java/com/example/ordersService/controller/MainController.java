package com.example.ordersService.controller;

import com.example.ordersService.dao.OrderDAO;
import com.example.ordersService.entity.Order;
import com.example.ordersService.service.URLService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
@AllArgsConstructor
@Tag(name = "Main controller", description = "This is the main and only controller")
public class MainController {
    private final OrderDAO orderDAO;
    private final RestTemplate restTemplate;
    //private final String getNumberGenerateServiceURL;
    private URLService urlService;




    @PostMapping("/orders")
    @Operation(
            summary = "Create order and return id",
            description = "Create new order and return id"
    )
    public int createOrder(@RequestBody Order order) {

        //String numberGenerateServiceURL = getNumberGenerateServiceURL;
        String numberGenerateServiceURL = urlService.getNumberGenerateServiceURL();
        //ResponseEntity<String> orderNumber = restTemplate.getForEntity("http://localhost:8091/numbers", String.class);
        ResponseEntity<String> orderNumber = restTemplate.getForEntity(numberGenerateServiceURL, String.class);
        order.setNumber(orderNumber.getBody());
        return orderDAO.createOrder(order);
    }

    @GetMapping("/orders")
    @Operation(
            summary = "Get order by id",
            description = "Get order fromDB by id"
    )
    public Order getOrderById(@RequestParam @Parameter(description = "order id", example = "1") int id) {
        return orderDAO.getOrderById(id);

    }

    @GetMapping("/orders/all")
    @ResponseBody
    @Operation(
            summary = "Get all orders",
            description = "Get all orders fromDB"
    )
    public List<Order> getAllOrders() {
        return orderDAO.getAllOrders();
    }

    @GetMapping("/orders/filter1")
    @Operation(
            summary = "Get orders with amount and date filter",
            description = "Get orders for a given date and greater than a given total order amount"
    )
    public List<Order> getOrdersWithDateAndAmountFilter(@RequestParam @Parameter(description = "order's date", example = "2024-10-22")String date, @RequestParam @Parameter(description = "order's amount", example = "300.5")double amount) {
        return orderDAO.getOrdersWithDateAndAmountFilter(date, amount);
    }


    @GetMapping("/orders/filter2")
    @Operation(
            summary = "Get orders with productCode and date filter",
            description = "Getting a list of orders that do not contain a specified product and " +
                    "were received in a specified time period"
    )
    public List<Order> getOrdersWithProductAndFromToDateFilter
            (@RequestParam @Parameter(description = "product code", example = "111") int code,
             @RequestParam @Parameter(description = "date from", example = "2024-10-20") String from,
             @RequestParam @Parameter(description = "date to", example = "2024-10-24") String to) {
        return orderDAO.getOrdersWithProductAndFromToDateFilter(code, from, to);
    }
}




