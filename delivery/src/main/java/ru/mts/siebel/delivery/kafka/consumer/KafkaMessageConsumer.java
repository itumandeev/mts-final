package ru.mts.siebel.delivery.kafka.consumer;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import ru.mts.siebel.delivery.service.DeliveryOptyService;
import ru.mts.siebel.model.DeliveryMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Log4j2
@Service
public class KafkaMessageConsumer {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private DeliveryOptyService deliveryOptyService;

    @KafkaListener(topics = "delivery_order_request", containerFactory = "deliveryMessageKafkaListenerContainerFactory")
    public void listener(final DeliveryMessage deliveryMessage) {
        String orderId = deliveryMessage.getId();
        logger.info("Доставка заказа Id = {}", orderId);
        deliveryOptyService.createDeliveryOpty(orderId, deliveryMessage.getPhone(), deliveryMessage.getAddress());
    }

}
