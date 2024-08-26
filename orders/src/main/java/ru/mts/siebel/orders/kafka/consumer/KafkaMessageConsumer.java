package ru.mts.siebel.orders.kafka.consumer;

import lombok.extern.log4j.Log4j2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.mts.siebel.model.OrderMessage;
import ru.mts.siebel.model.StatusMessage;
import ru.mts.siebel.orders.constant.StatusConstant;
import ru.mts.siebel.orders.service.OrderService;
import ru.mts.siebel.orders.service.StorageService;

@Log4j2
@Service
public class KafkaMessageConsumer {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private OrderService orderService;
    @Autowired
    private StorageService storageService;
    @Autowired
    private KafkaTemplate<String, OrderMessage> kafkaTemplate;

    @KafkaListener(topics = "check_available_request", containerFactory = "orderMessageKafkaListenerContainerFactory")
    public void checkAvailableRequestListener(final OrderMessage orderMessage) {

        logger.info("Проверка наличия товара {} на складе в количестве {} шт", orderMessage.getProductCode(), orderMessage.getQuantity());

        boolean value = storageService.checkAndReserve(orderMessage.getProductCode(), orderMessage.getQuantity());
        if (value) {
            orderMessage.setStatus(StatusConstant.RESERVED);
        } else {
            orderMessage.setStatus(StatusConstant.NOT_AVAILABLE);
        }
        kafkaTemplate.send("check_available_response", orderMessage.getId(), orderMessage);
    }

    @KafkaListener(topics = "notify_intermediate_status", containerFactory = "statusMessageKafkaListenerContainerFactory")
    public void notifyIntermediateListener(final StatusMessage statusMessage) {
        String orderId = statusMessage.getOrderId();

        logger.info("Обновление статуса заказа Id = {}", orderId);
        orderService.updateOrderStatus(orderId, statusMessage.getStatus());
    }

    @KafkaListener(topics = "notify_final_status", containerFactory = "statusMessageKafkaListenerContainerFactory")
    public void notifyFinalListener(final StatusMessage statusMessage) {
        String orderId = statusMessage.getOrderId();
        String status = statusMessage.getStatus();
        logger.info("Завершение заказа Id = {}", orderId);
        orderService.updateOrderStatus(orderId, status);
        if (status.equals(StatusConstant.COMPLETED)) {

            logger.info("Заказ Id = {} выполнен", orderId);
        } else {

            logger.info("Заказ Id = {} закрыт", orderId);
        }
    }

}
