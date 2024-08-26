package ru.mts.siebel.process.bpm.delegate;

import lombok.extern.log4j.Log4j2;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;
import ru.mts.siebel.process.bpm.constant.StatusConstant;
import ru.mts.siebel.process.bpm.constant.VariablesConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Log4j2
@Component
public class SendForPackagingDelegate implements JavaDelegate {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Override
    public void execute(final DelegateExecution execution) throws Exception {
        String orderId = execution.getVariable(VariablesConstant.ORDER_ID).toString();
        logger.info("Заявка на упаковку заказа Id = {} принята", orderId);

        execution.setVariable(VariablesConstant.STATUS, StatusConstant.PACKED);
        logger.info("Упаковка заказа Id = {} выполнена успешно", orderId);

    }

}
