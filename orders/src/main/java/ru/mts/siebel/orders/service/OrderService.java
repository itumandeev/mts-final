package ru.mts.siebel.orders.service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.mts.siebel.model.StartMessage;
import ru.mts.siebel.orders.entity.Order;
import ru.mts.siebel.orders.repository.OrderRepository;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;


@Service
public class OrderService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private OrderRepository repository;
    @Autowired
    private KafkaTemplate<String, StartMessage> kafkaTemplate;

    @Transactional
    public void addOrder(final Order order) {
        String orderId = UUID.randomUUID().toString();
        order.setId(orderId);
        order.setCreated(LocalDate.now());
        order.setStatus("NEW");
        repository.save(order);
        logger.info("заказ сохранен {}", orderId);
        StartMessage startMessage = new StartMessage(orderId, order.getProductCode(), order.getQuantity(), order.getStatus(), order.getPhone(), order.getAddress());
        kafkaTemplate.send("start_process", orderId, startMessage);
        logger.info("сообщение отправлено");
    }

    public void updateOrderStatus(final String id, final String status) {
        Optional<Order> orderOptional = repository.findById(id);
        if (orderOptional.isPresent()) {
            Order order = orderOptional.get();
            String oldStatus = order.getStatus();
            order.setStatus(status);
            repository.save(order);

            logger.info("Статус заказа Id = {} обновлен. Было: {}, стало: {}", order.getId(), oldStatus, status);
        } else {
            logger.info("Заказ Id = {} не найден", id);
        }
    }
}
