package ru.mts.siebel.process.bpm.delegate;

import lombok.extern.log4j.Log4j2;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import ru.mts.siebel.model.DeliveryMessage;
import ru.mts.siebel.process.bpm.constant.VariablesConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Log4j2
@Component
public class SendForDeliveryDelegate implements JavaDelegate {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private KafkaTemplate<String, DeliveryMessage> kafkaTemplate;

    @Override
    public void execute(final DelegateExecution execution) throws Exception {
        String orderId = execution.getVariable(VariablesConstant.ORDER_ID).toString();
        String status = execution.getVariable(VariablesConstant.STATUS).toString();
        String phone = execution.getVariable(VariablesConstant.PHONE).toString();
        String address = execution.getVariable(VariablesConstant.ADDRESS).toString();
        DeliveryMessage deliveryMessage = new DeliveryMessage(orderId, status, phone, address);
        logger.info("Заявка на доставку заказа Id = {} принята", orderId);
        kafkaTemplate.send("delivery_order_request", orderId, deliveryMessage);
    }

}
