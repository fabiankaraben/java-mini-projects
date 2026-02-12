package com.example.workflow.controller;

import com.example.workflow.model.Workflow;
import com.example.workflow.service.WorkflowService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/workflows")
public class WorkflowController {

    private final WorkflowService workflowService;

    public WorkflowController(WorkflowService workflowService) {
        this.workflowService = workflowService;
    }

    @PostMapping
    public ResponseEntity<Workflow> submitWorkflow(@RequestBody Workflow workflow) {
        try {
            Workflow submitted = workflowService.submitWorkflow(workflow);
            return ResponseEntity.accepted().body(submitted);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Workflow> getWorkflowStatus(@PathVariable String id) {
        Workflow workflow = workflowService.getWorkflow(id);
        if (workflow == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(workflow);
    }
}
