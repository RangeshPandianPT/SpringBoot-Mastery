package dev.rangesh.HelloWorld.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PerformanceLoggingAspectTest {

    @Mock
    private ProceedingJoinPoint joinPoint;

    @Mock
    private Signature signature;

    @Test
    void testLogExecutionTime() throws Throwable {
        PerformanceLoggingAspect aspect = new PerformanceLoggingAspect();
        
        when(joinPoint.getSignature()).thenReturn(signature);
        when(signature.toShortString()).thenReturn("StudentService.getAllStudents(..)");
        when(joinPoint.proceed()).thenReturn("Success");

        Object result = aspect.logExecutionTime(joinPoint);

        assertEquals("Success", result);
        verify(joinPoint, times(1)).proceed();
    }
}
