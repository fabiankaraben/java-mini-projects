package com.example.ruleengine.service;

import com.example.ruleengine.model.Order;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RuleEngineService {

    private final KieContainer kieContainer;

    @Autowired
    public RuleEngineService(KieContainer kieContainer) {
        this.kieContainer = kieContainer;
    }

    public Order applyRules(Order order) {
        KieSession kieSession = kieContainer.newKieSession();
        try {
            kieSession.insert(order);
            kieSession.fireAllRules();
        } finally {
            kieSession.dispose();
        }
        return order;
    }
}
