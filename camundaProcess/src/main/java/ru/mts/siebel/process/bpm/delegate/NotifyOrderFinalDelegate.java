package ru.mts.siebel.process.bpm.delegate;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import ru.mts.siebel.model.StatusMessage;
import ru.mts.siebel.process.bpm.constant.StatusConstant;
import ru.mts.siebel.process.bpm.constant.VariablesConstant;

@Component
public class NotifyOrderFinalDelegate implements JavaDelegate {

    @Autowired
    private KafkaTemplate<String, StatusMessage> kafkaTemplate;

    @Override
    public void execute(final DelegateExecution execution) throws Exception {
        String orderId = execution.getVariable(VariablesConstant.ORDER_ID).toString();
        StatusMessage statusMessage = new StatusMessage(orderId, StatusConstant.COMPLETED);
        kafkaTemplate.send("notify_final_status", orderId, statusMessage);
    }

}
