package com.example.collaboration;

import com.example.collaboration.model.Document;
import com.example.collaboration.model.Operation;
import com.example.collaboration.model.OperationType;
import com.example.collaboration.service.OtService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class CollaborationToolApplicationTests {

    @Autowired
    private OtService otService;

    @Test
    void testConcurrentEdits() throws InterruptedException {
        // Simulation of concurrent edits
        // Client A wants to insert "Hello" at 0
        // Client B wants to insert "World" at 0
        // Expected result depends on timing, but with OT it should be consistent (e.g. "HelloWorld" or "WorldHello")
        // But more interestingly:
        // Client A inserts "A" at 0.
        // Client B inserts "B" at 0.
        // Both start from revision 0.
        
        int numberOfThreads = 2;
        ExecutorService service = Executors.newFixedThreadPool(numberOfThreads);
        CountDownLatch latch = new CountDownLatch(numberOfThreads);

        Runnable clientA = () -> {
            try {
                Operation op = new Operation(OperationType.INSERT, 0, "A", 0, 0, "clientA");
                otService.applyOperation(op);
            } finally {
                latch.countDown();
            }
        };

        Runnable clientB = () -> {
            try {
                Operation op = new Operation(OperationType.INSERT, 0, "B", 0, 0, "clientB");
                otService.applyOperation(op);
            } finally {
                latch.countDown();
            }
        };

        service.submit(clientA);
        service.submit(clientB);

        latch.await(5, TimeUnit.SECONDS);
        
        Document doc = otService.getDocument();
        System.out.println("Final Content: " + doc.getContent());
        // Since both inserted at 0 with revision 0.
        // If A goes first: A. Rev becomes 1.
        // B comes with Rev 0.
        // Transform B against A (Insert vs Insert). 
        // B pos 0, A pos 0.
        // If B clientId > A clientId? (Comparison logic in OtService)
        // Or simple first come first served if synchronized?
        // Wait, applyOperation is synchronized.
        // So one will strictly happen after the other in the server.
        // The second one will see it is outdated (rev 0 < current rev 1) and transform.
        
        // If A is first: Doc "A", rev 1.
        // B comes: Insert "B" at 0, rev 0.
        // Transform B against A (Insert "A" at 0).
        // B.pos 0, A.pos 0.
        // If we assume "A" < "B" string comparison for tie breaking? Or clientId?
        // In OtService I put: if (op1.getPosition() > op2.getPosition() || (op1.getPosition() == op2.getPosition() && op1.getClientId().compareTo(op2.getClientId()) > 0))
        // "clientA" vs "clientB". "clientB" > "clientA".
        
        // Case 1: A executed first.
        // Doc: "A".
        // B transforms against A. B.pos(0) == A.pos(0). B.id > A.id.
        // B gets shifted?
        // Logic: if (op1.pos > op2.pos || (op1.pos == op2.pos && op1.id > op2.id))
        // op1 is the new op (B), op2 is the history op (A).
        // B.id > A.id -> True.
        // B.pos becomes 0 + len("A") = 1.
        // Apply B at 1.
        // Result: "AB".
        
        // Case 2: B executed first.
        // Doc: "B".
        // A transforms against B. A.pos(0) == B.pos(0). A.id < B.id.
        // A.id > B.id -> False.
        // A.pos remains 0.
        // Apply A at 0.
        // Result: "AB".
        
        // So in both cases, result should be "AB".
        
        assertEquals("AB", doc.getContent());
    }
}
