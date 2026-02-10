package com.fabiankaraben.miniprojects.serviceb;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ServiceBController {

    private static final Logger log = LoggerFactory.getLogger(ServiceBController.class);

    @GetMapping("/greet")
    public String greet() {
        log.info("Service B: Received greet request.");
        return "Hello from Service B!";
    }
}
