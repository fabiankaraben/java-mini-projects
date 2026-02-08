package com.example.cache;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CachedComputationServiceTest {

    @Mock
    private ComputationService delegateService;

    private CachedComputationService cachedService;

    @BeforeEach
    void setUp() {
        cachedService = new CachedComputationService(delegateService);
    }

    @Test
    void testCacheHit() {
        String input = "test-input";
        String expectedOutput = "Processed: test-input";

        // Mock behavior: return value when called
        when(delegateService.compute(input)).thenReturn(expectedOutput);

        // First call: Should hit the delegate
        String result1 = cachedService.compute(input);
        assertEquals(expectedOutput, result1);

        // Second call: Should hit the cache
        String result2 = cachedService.compute(input);
        assertEquals(expectedOutput, result2);

        // Verify delegate was called exactly once
        verify(delegateService, times(1)).compute(input);
    }

    @Test
    void testCacheMiss() {
        String input1 = "input-1";
        String input2 = "input-2";
        
        when(delegateService.compute(input1)).thenReturn("Result 1");
        when(delegateService.compute(input2)).thenReturn("Result 2");

        cachedService.compute(input1);
        cachedService.compute(input2);

        // Verify delegate was called once for each distinct input
        verify(delegateService, times(1)).compute(input1);
        verify(delegateService, times(1)).compute(input2);
    }
    
    @Test
    void testCacheClear() {
        String input = "input";
        when(delegateService.compute(input)).thenReturn("result");
        
        cachedService.compute(input);
        cachedService.clearCache();
        cachedService.compute(input);
        
        // Should be called twice because cache was cleared
        verify(delegateService, times(2)).compute(input);
    }
}
