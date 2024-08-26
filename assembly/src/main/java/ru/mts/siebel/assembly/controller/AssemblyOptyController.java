package ru.mts.siebel.assembly.controller;

import lombok.extern.log4j.Log4j2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.mts.siebel.assembly.service.AssemblyOptyService;

@Log4j2
@RestController
@RequestMapping("/assembly")
public class AssemblyOptyController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private AssemblyOptyService assemblyOptyService;

    @PostMapping("/status")
    public ResponseEntity<String> orderCheck(@RequestParam String orderId, @RequestParam String status) {
        logger.info("Выполнение сборки заказа Id = {}", orderId + " ", status);
        assemblyOptyService.updateAssemblyOrderStatus(orderId, status);
        return ResponseEntity.ok("Assembly opty is completed");
    }

}
