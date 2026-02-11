package com.example.audit;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/audit")
public class AuditController {

    private final BusinessService businessService;
    private final AuditLogRepository auditLogRepository;

    public AuditController(BusinessService businessService, AuditLogRepository auditLogRepository) {
        this.businessService = businessService;
        this.auditLogRepository = auditLogRepository;
    }

    @PostMapping("/action")
    public String triggerAction(@RequestParam String user, @RequestParam String action) {
        return businessService.performCriticalAction(user, action);
    }

    @PostMapping("/settings")
    public String updateSettings(@RequestParam String key, @RequestParam String value) {
        return businessService.updateSettings(key, value);
    }

    @GetMapping("/logs")
    public List<AuditLog> getLogs() {
        return auditLogRepository.findAll();
    }
}
