package ru.mts.siebel.assembly.service;

import jakarta.transaction.Transactional;
import lombok.extern.log4j.Log4j2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.mts.siebel.assembly.constant.StatusConstant;
import ru.mts.siebel.assembly.entity.AssemblyOpty;
import ru.mts.siebel.assembly.repository.AssemblyOptyRepository;
import ru.mts.siebel.model.StatusMessage;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Log4j2
@Service
public class AssemblyOptyService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private AssemblyOptyRepository repository;
    @Autowired
    private KafkaTemplate<String, StatusMessage> kafkaTemplate;

    public List<AssemblyOpty> findAll() {
        return repository.findAll();
    }

    public List<AssemblyOpty> findNew() {
        return repository.findByStatus(StatusConstant.NEW);
    }

    public int size() {
        return findAll().size();
    }

    @Transactional
    public void createAssemblyOpty(final String orderId) {
        logger.info("Создание заявки начало");
        AssemblyOpty assemblyOpty = new AssemblyOpty();
        assemblyOpty.setCreated(LocalDate.now());
        assemblyOpty.setOrderId(orderId);
        assemblyOpty.setStatus(StatusConstant.NEW);
        logger.info("заявка" + assemblyOpty.toString());
        repository.save(assemblyOpty);
        logger.info("Создана заявка Id = {} на сборку", assemblyOpty.getId());
        logger.info("Ожидайте сборку заказа Id = {}", orderId);
    }

    @Transactional
    public void addAssemblyOpty(final AssemblyOpty assemblyOpty) {
        repository.save(assemblyOpty);
        logger.info("Создана заявка Id = {} на сборку", assemblyOpty.getId());
    }

    public void updateAssemblyOrderStatus(final String id, final String status) {
        Optional<AssemblyOpty> orderOptional = repository.findByOrderId(id);
        if (orderOptional.isPresent()) {
            AssemblyOpty assemblyOpty = orderOptional.get();
            String oldStatus = assemblyOpty.getStatus();
            String orderId = assemblyOpty.getOrderId();
            assemblyOpty.setStatus(status);
            repository.save(assemblyOpty);
            logger.info("Статус заявки на сборку Id = {} обновлен. Было: {}, стало: {}", assemblyOpty.getId(), oldStatus, status);
            kafkaTemplate.send("assembly_order_response", orderId, new StatusMessage(orderId, status));
        } else {
            logger.info("Заявка на сборку Id = {} не найдена", id);
        }
    }

}
