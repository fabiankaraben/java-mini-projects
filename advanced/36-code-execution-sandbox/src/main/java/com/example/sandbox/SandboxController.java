package com.example.sandbox;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/sandbox")
public class SandboxController {

    private final CodeExecutionService executionService;

    public SandboxController(CodeExecutionService executionService) {
        this.executionService = executionService;
    }

    @PostMapping("/execute")
    public CodeExecutionResponse execute(@RequestBody CodeExecutionRequest request) {
        return executionService.execute(request.getCode());
    }
}
