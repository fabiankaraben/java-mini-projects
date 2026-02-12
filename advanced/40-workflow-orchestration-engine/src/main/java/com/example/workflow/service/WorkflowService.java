package com.example.workflow.service;

import com.example.workflow.model.Task;
import com.example.workflow.model.TaskStatus;
import com.example.workflow.model.Workflow;
import com.example.workflow.model.WorkflowStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Service
public class WorkflowService {

    private static final Logger logger = LoggerFactory.getLogger(WorkflowService.class);
    private final Map<String, Workflow> workflowStore = new ConcurrentHashMap<>();
    private final ExecutorService executorService = Executors.newCachedThreadPool();

    public Workflow submitWorkflow(Workflow workflow) {
        if (workflow.getId() == null || workflow.getId().isEmpty()) {
            workflow.setId(UUID.randomUUID().toString());
        }
        // Validate DAG
        if (hasCycles(workflow)) {
            throw new IllegalArgumentException("Workflow contains cycles/circular dependencies");
        }
        
        workflow.setStatus(WorkflowStatus.PENDING);
        workflow.getTasks().forEach(t -> t.setStatus(TaskStatus.PENDING));
        workflowStore.put(workflow.getId(), workflow);
        
        // Start execution asynchronously
        CompletableFuture.runAsync(() -> executeWorkflow(workflow), executorService);
        
        return workflow;
    }

    public Workflow getWorkflow(String id) {
        return workflowStore.get(id);
    }

    private void executeWorkflow(Workflow workflow) {
        workflow.setStatus(WorkflowStatus.RUNNING);
        logger.info("Starting workflow: {}", workflow.getId());

        try {
            boolean workflowChanged = true;
            while (workflowChanged && workflow.getStatus() == WorkflowStatus.RUNNING) {
                workflowChanged = false;
                List<Task> pendingTasks = workflow.getTasks().stream()
                        .filter(t -> t.getStatus() == TaskStatus.PENDING)
                        .collect(Collectors.toList());

                if (pendingTasks.isEmpty()) {
                    break;
                }

                for (Task task : pendingTasks) {
                    if (areDependenciesMet(task, workflow)) {
                        task.setStatus(TaskStatus.IN_PROGRESS);
                        // Simulate execution
                        executeTask(task);
                        task.setStatus(TaskStatus.COMPLETED);
                        workflowChanged = true;
                    }
                }
                
                // If we have pending tasks but no progress was made, and dependencies aren't met, we might be stuck (should be caught by cycle check, but good for safety)
                if (!workflowChanged && !pendingTasks.isEmpty()) {
                     // Check if any dependencies failed
                     boolean anyFailed = workflow.getTasks().stream().anyMatch(t -> t.getStatus() == TaskStatus.FAILED);
                     if (anyFailed) {
                         workflow.setStatus(WorkflowStatus.FAILED);
                         return;
                     }
                }
            }
            
            boolean allCompleted = workflow.getTasks().stream().allMatch(t -> t.getStatus() == TaskStatus.COMPLETED);
            if (allCompleted) {
                workflow.setStatus(WorkflowStatus.COMPLETED);
                logger.info("Workflow completed: {}", workflow.getId());
            } else {
                workflow.setStatus(WorkflowStatus.FAILED);
                logger.error("Workflow failed or stuck: {}", workflow.getId());
            }
            
        } catch (Exception e) {
            logger.error("Error executing workflow", e);
            workflow.setStatus(WorkflowStatus.FAILED);
        }
    }

    private void executeTask(Task task) {
        logger.info("Executing task: {} (Type: {}, Payload: {})", task.getId(), task.getType(), task.getPayload());
        try {
            // Simulate processing time
            Thread.sleep(100); 
            if ("FAIL".equalsIgnoreCase(task.getType())) {
                throw new RuntimeException("Forced failure for testing");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (Exception e) {
             logger.error("Task failed: {}", task.getId(), e);
             task.setStatus(TaskStatus.FAILED);
             throw new RuntimeException(e);
        }
    }

    private boolean areDependenciesMet(Task task, Workflow workflow) {
        if (task.getDependencies() == null || task.getDependencies().isEmpty()) {
            return true;
        }
        
        for (String depId : task.getDependencies()) {
            Task depTask = workflow.getTasks().stream()
                    .filter(t -> t.getId().equals(depId))
                    .findFirst()
                    .orElse(null);
            
            if (depTask == null || depTask.getStatus() != TaskStatus.COMPLETED) {
                return false;
            }
        }
        return true;
    }

    private boolean hasCycles(Workflow workflow) {
        // Simple DFS to detect cycles
        Map<String, List<String>> adj = new HashMap<>();
        for (Task t : workflow.getTasks()) {
            adj.putIfAbsent(t.getId(), new ArrayList<>());
            if (t.getDependencies() != null) {
                for (String dep : t.getDependencies()) {
                    // Dependency means dep -> t (dep must finish before t)
                    // In graph terms for topological sort, edge is dep -> t
                    adj.putIfAbsent(dep, new ArrayList<>());
                    adj.get(dep).add(t.getId());
                }
            }
        }
        
        Set<String> visited = new HashSet<>();
        Set<String> recursionStack = new HashSet<>();
        
        for (String nodeId : adj.keySet()) {
            if (hasCycleUtil(nodeId, adj, visited, recursionStack)) {
                return true;
            }
        }
        return false;
    }

    private boolean hasCycleUtil(String nodeId, Map<String, List<String>> adj, Set<String> visited, Set<String> recursionStack) {
        if (recursionStack.contains(nodeId)) {
            return true;
        }
        if (visited.contains(nodeId)) {
            return false;
        }
        
        visited.add(nodeId);
        recursionStack.add(nodeId);
        
        List<String> children = adj.get(nodeId);
        if (children != null) {
            for (String child : children) {
                if (hasCycleUtil(child, adj, visited, recursionStack)) {
                    return true;
                }
            }
        }
        
        recursionStack.remove(nodeId);
        return false;
    }
}
