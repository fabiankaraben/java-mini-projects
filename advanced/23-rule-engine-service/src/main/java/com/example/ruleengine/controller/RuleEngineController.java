package com.example.ruleengine.controller;

import com.example.ruleengine.model.Order;
import com.example.ruleengine.service.RuleEngineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/rules")
public class RuleEngineController {

    private final RuleEngineService ruleEngineService;

    @Autowired
    public RuleEngineController(RuleEngineService ruleEngineService) {
        this.ruleEngineService = ruleEngineService;
    }

    @PostMapping("/order-discount")
    public Order getDiscount(@RequestBody Order order) {
        return ruleEngineService.applyRules(order);
    }
}
