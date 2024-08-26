package ru.mts.siebel.delivery.controller;

import lombok.extern.log4j.Log4j2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.mts.siebel.delivery.entity.DeliveryOpty;
import ru.mts.siebel.delivery.service.DeliveryOptyService;

import java.util.List;

@Log4j2
@RestController
@RequestMapping("/delivery")
public class DeliveryOptyController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private DeliveryOptyService deliveryOptyService;

    @PostMapping("/all")
    public List<DeliveryOpty> getOrders() {
        return deliveryOptyService.findAll();
    }


    @PostMapping("/new")
    public List<DeliveryOpty> getNewOrders() {
        return deliveryOptyService.findNew();
    }

    @PostMapping("/status")
    public ResponseEntity<String> orderCheck(@RequestParam String orderId, @RequestParam String status) {

        logger.info("Выполнение доставки заказа Id = {}", orderId);

        deliveryOptyService.updateDeliveryOrderStatus(orderId, status);
        return ResponseEntity.ok("Delivery opty is completed");
    }

}
