package com.example.audit;

import org.springframework.stereotype.Service;

@Service
public class BusinessService {

    @Auditable
    public String performCriticalAction(String user, String action) {
        // Simulate some business logic
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return "Action '" + action + "' performed by " + user;
    }

    @Auditable
    public String updateSettings(String key, String value) {
        return "Settings updated: " + key + "=" + value;
    }
    
    public String nonAuditedMethod() {
        return "This method is not audited";
    }
}
