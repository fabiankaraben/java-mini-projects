package com.example.workflow.service;

import com.example.workflow.model.Task;
import com.example.workflow.model.TaskStatus;
import com.example.workflow.model.Workflow;
import com.example.workflow.model.WorkflowStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.awaitility.Awaitility.await;

class WorkflowServiceTest {

    private WorkflowService workflowService;

    @BeforeEach
    void setUp() {
        workflowService = new WorkflowService();
    }

    @Test
    void testLinearWorkflow() {
        Task t1 = new Task("t1", "LOG", "Step 1");
        Task t2 = new Task("t2", "LOG", "Step 2");
        t2.setDependencies(new HashSet<>(Collections.singletonList("t1")));
        Task t3 = new Task("t3", "LOG", "Step 3");
        t3.setDependencies(new HashSet<>(Collections.singletonList("t2")));

        Workflow workflow = new Workflow("linear-flow", Arrays.asList(t1, t2, t3));

        workflowService.submitWorkflow(workflow);

        await().atMost(5, TimeUnit.SECONDS).until(() -> 
            workflow.getStatus() == WorkflowStatus.COMPLETED
        );

        assertEquals(TaskStatus.COMPLETED, t1.getStatus());
        assertEquals(TaskStatus.COMPLETED, t2.getStatus());
        assertEquals(TaskStatus.COMPLETED, t3.getStatus());
    }

    @Test
    void testParallelWorkflow() {
        // t1 -> t2, t1 -> t3, (t2, t3) -> t4
        Task t1 = new Task("t1", "LOG", "Start");
        
        Task t2 = new Task("t2", "LOG", "Parallel A");
        t2.setDependencies(Collections.singleton("t1"));
        
        Task t3 = new Task("t3", "LOG", "Parallel B");
        t3.setDependencies(Collections.singleton("t1"));
        
        Task t4 = new Task("t4", "LOG", "End");
        t4.setDependencies(new HashSet<>(Arrays.asList("t2", "t3")));

        Workflow workflow = new Workflow("parallel-flow", Arrays.asList(t1, t2, t3, t4));

        workflowService.submitWorkflow(workflow);

        await().atMost(5, TimeUnit.SECONDS).until(() -> 
            workflow.getStatus() == WorkflowStatus.COMPLETED
        );

        assertEquals(TaskStatus.COMPLETED, t1.getStatus());
        assertEquals(TaskStatus.COMPLETED, t2.getStatus());
        assertEquals(TaskStatus.COMPLETED, t3.getStatus());
        assertEquals(TaskStatus.COMPLETED, t4.getStatus());
    }

    @Test
    void testCircularDependencyDetection() {
        Task t1 = new Task("t1", "LOG", "A");
        Task t2 = new Task("t2", "LOG", "B");
        t2.setDependencies(Collections.singleton("t1"));
        t1.setDependencies(Collections.singleton("t2")); // Cycle

        Workflow workflow = new Workflow("cycle-flow", Arrays.asList(t1, t2));

        assertThrows(IllegalArgumentException.class, () -> workflowService.submitWorkflow(workflow));
    }
    
    @Test
    void testTaskFailure() {
        Task t1 = new Task("t1", "FAIL", "Explode"); // Type FAIL triggers exception
        Task t2 = new Task("t2", "LOG", "Should not run");
        t2.setDependencies(Collections.singleton("t1"));

        Workflow workflow = new Workflow("fail-flow", Arrays.asList(t1, t2));

        workflowService.submitWorkflow(workflow);

        await().atMost(5, TimeUnit.SECONDS).until(() -> 
            workflow.getStatus() == WorkflowStatus.FAILED
        );
        
        assertEquals(TaskStatus.FAILED, t1.getStatus());
        assertEquals(TaskStatus.PENDING, t2.getStatus());
    }
}
