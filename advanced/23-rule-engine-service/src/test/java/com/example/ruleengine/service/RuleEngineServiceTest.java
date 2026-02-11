package com.example.ruleengine.service;

import com.example.ruleengine.config.DroolsConfig;
import com.example.ruleengine.model.Order;
import org.junit.jupiter.api.Test;
import org.kie.api.runtime.KieContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class RuleEngineServiceTest {

    @Autowired
    private RuleEngineService ruleEngineService;

    @Test
    public void testHighValueOrder() {
        Order order = new Order();
        order.setAmount(15000);
        order.setCustomerType("REGULAR");

        ruleEngineService.applyRules(order);

        assertEquals(0.15, order.getDiscount());
        assertEquals("High Value Order", order.getRuleApplied());
    }

    @Test
    public void testLoyalCustomerOrder() {
        Order order = new Order();
        order.setAmount(5000);
        order.setCustomerType("LOYAL");

        ruleEngineService.applyRules(order);

        assertEquals(0.10, order.getDiscount());
        assertEquals("Loyal Customer Order", order.getRuleApplied());
    }

    @Test
    public void testStandardOrder() {
        Order order = new Order();
        order.setAmount(500);
        order.setCustomerType("REGULAR");

        ruleEngineService.applyRules(order);

        assertEquals(0.0, order.getDiscount());
        assertEquals("Standard Order", order.getRuleApplied());
    }

    @Test
    public void testVipCustomerOrder() {
        Order order = new Order();
        order.setAmount(500); // Amount doesn't matter for VIP flat discount in our rules, unless conflicting
        order.setCustomerType("VIP");

        ruleEngineService.applyRules(order);

        assertEquals(0.20, order.getDiscount());
        assertEquals("VIP Customer Flat Discount", order.getRuleApplied());
    }
}
