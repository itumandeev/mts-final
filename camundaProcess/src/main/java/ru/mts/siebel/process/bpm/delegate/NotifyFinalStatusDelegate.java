package ru.mts.siebel.process.bpm.delegate;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import ru.mts.siebel.model.StatusMessage;
import ru.mts.siebel.process.bpm.constant.VariablesConstant;

@Component
public class NotifyFinalStatusDelegate implements JavaDelegate {

    @Autowired
    private KafkaTemplate<String, StatusMessage> kafkaTemplate;

    @Override
    public void execute(final DelegateExecution execution) throws Exception {
        String orderId = execution.getVariable(VariablesConstant.ORDER_ID).toString();
        String status = execution.getVariable(VariablesConstant.STATUS).toString();
        StatusMessage statusMessage = new StatusMessage(orderId, status);
        kafkaTemplate.send("notify_final_status", orderId, statusMessage);
    }

}
