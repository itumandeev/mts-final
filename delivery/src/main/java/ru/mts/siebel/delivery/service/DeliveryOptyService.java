package ru.mts.siebel.delivery.service;

import jakarta.transaction.Transactional;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.mts.siebel.delivery.constant.StatusConstant;
import ru.mts.siebel.delivery.entity.DeliveryOpty;
import ru.mts.siebel.delivery.repository.DeliveryOptyRepository;
import ru.mts.siebel.model.StatusMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Log4j2
@Service
public class DeliveryOptyService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private DeliveryOptyRepository repository;
    @Autowired
    private KafkaTemplate<String, StatusMessage> kafkaTemplate;

    public List<DeliveryOpty> findAll() {
        return repository.findAll();
    }

    public List<DeliveryOpty> findNew() {
        return repository.findByStatus(StatusConstant.NEW);
    }

    public int size() {
        return findAll().size();
    }

    @Transactional
    public void createDeliveryOpty(final String orderId, final String phone, final String address) {
        logger.info("Создание заявки на доставку начало");
        DeliveryOpty deliveryOpty = new DeliveryOpty();
        deliveryOpty.setOrderId(orderId);
        deliveryOpty.setCreated(LocalDate.now());
        deliveryOpty.setStatus(StatusConstant.NEW);
        deliveryOpty.setPhone(phone);
        deliveryOpty.setAddress(address);
        repository.save(deliveryOpty);
        logger.info("Создана заявка Id = {} на доставку", deliveryOpty.getId());

        logger.info("Ожидайте доставку заказа Id = {}", orderId);
    }

    @Transactional
    public void addDeliveryOpty(final DeliveryOpty deliveryOpty) {
        repository.save(deliveryOpty);
        logger.info("Создана заявка Id = {} на доставку", deliveryOpty.getId());
    }

    public void updateDeliveryOrderStatus(final String id, final String status) {
        Optional<DeliveryOpty> orderOptional = repository.findByOrderId(id);
        if (orderOptional.isPresent()) {
            DeliveryOpty deliveryOpty = orderOptional.get();
            String oldStatus = deliveryOpty.getStatus();
            String orderId = deliveryOpty.getOrderId();
            deliveryOpty.setStatus(status);
            repository.save(deliveryOpty);
            logger.info("Статус заявки на доставку Id = {} обновлен. Было: {}, стало: {}", deliveryOpty.getId(), oldStatus, status);

            kafkaTemplate.send("delivery_order_response", orderId, new StatusMessage(orderId, status));
        } else {
            logger.info("Заявка на доставку Id = {} не найдена", id);
        }
    }

}
