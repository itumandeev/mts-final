package ru.mts.siebel.process.bpm.delegate;

import lombok.extern.log4j.Log4j2;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import ru.mts.siebel.model.OrderMessage;
import ru.mts.siebel.process.bpm.constant.VariablesConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Log4j2
@Component
public class CheckAvailableDelegate implements JavaDelegate {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private KafkaTemplate<String, OrderMessage> kafkaTemplate;

    @Override
    public void execute(final DelegateExecution execution) throws Exception {
        logger.info("Проверка наличия товара на складе ");

        String orderId = execution.getVariable(VariablesConstant.ORDER_ID).toString();
        String productCode = execution.getVariable(VariablesConstant.PRODUCT_CODE).toString();
        Integer quantity = (Integer) execution.getVariable(VariablesConstant.QUANTITY);
        String status = execution.getVariable(VariablesConstant.STATUS).toString();
        String phone = execution.getVariable(VariablesConstant.PHONE).toString();
        String address = execution.getVariable(VariablesConstant.ADDRESS).toString();
        OrderMessage orderMessage = new OrderMessage(orderId, productCode, quantity, status, phone, address);
        logger.info("Проверка наличия товара {} на складе по заказу Id = {}", productCode, orderId);
        kafkaTemplate.send("check_available_request", orderId, orderMessage);
    }

}
