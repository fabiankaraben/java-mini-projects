package com.example.audit;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "audit_logs")
@Data
@NoArgsConstructor
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String methodName;
    private String arguments;
    private String returnValue;
    private LocalDateTime timestamp;
    private long executionTimeMs;

    public AuditLog(String methodName, String arguments, String returnValue, long executionTimeMs) {
        this.methodName = methodName;
        this.arguments = arguments;
        this.returnValue = returnValue;
        this.executionTimeMs = executionTimeMs;
        this.timestamp = LocalDateTime.now();
    }
}
