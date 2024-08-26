package ru.mts.siebel.orders.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.mts.siebel.orders.entity.Order;
import ru.mts.siebel.orders.service.OrderService;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/create")
    public ResponseEntity<String> orderParams(@RequestBody Order order) {
        orderService.addOrder(order);
        return ResponseEntity.ok("Order is received");
    }

}
