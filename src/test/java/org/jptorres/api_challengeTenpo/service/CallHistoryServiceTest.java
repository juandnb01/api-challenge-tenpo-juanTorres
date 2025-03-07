package org.jptorres.api_challengeTenpo.service;

import org.jptorres.api_challengeTenpo.model.entiy.CallHistory;
import org.jptorres.api_challengeTenpo.repository.CallHistoryRepository;
import org.jptorres.api_challengeTenpo.service.implementation.CallHistoryService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatcher;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CallHistoryServiceTest {

    @Mock
    private CallHistoryRepository callHistoryRepository;

    @InjectMocks
    private CallHistoryService callHistoryService;

    /**
     * Test: Verifica que el método logCall guarde correctamente el historial.
     * Dado que el método es asíncrono, se utiliza verify con timeout para esperar la invocación.
     */
    @Test
    public void testLogCall() {
        String endpoint = "/api/calculate";
        String parameters = "num1=10&num2=20";
        String response = "CalculationResponse{sum=30.0, percentage=10.0, result=33.0}";

        // Invocamos el método asíncrono.
        callHistoryService.logCall(endpoint, parameters, response);

        // Verificamos que callHistoryRepository.save() sea llamado, usando un timeout para esperar su ejecución.
        verify(callHistoryRepository, timeout(1000)).save(argThat(new ArgumentMatcher<CallHistory>() {
            @Override
            public boolean matches(CallHistory history) {
                return endpoint.equals(history.getEndpoint()) &&
                        parameters.equals(history.getParameters()) &&
                        response.equals(history.getResponse());
            }
        }));
    }

    /**
     * Test: Verifica que el método getHistory retorne la lista completa del historial.
     */
    @Test
    public void testGetHistory() {
        // Preparamos una lista de historial de ejemplo.
        List<CallHistory> expectedHistory = new ArrayList<>();
        CallHistory historyEntry = new CallHistory("/api/calculate", "num1=10&num2=20", "CalculationResponse{sum=30.0, percentage=10.0, result=33.0}");
        expectedHistory.add(historyEntry);

        // Configuramos el mock para que retorne la lista de historial.
        when(callHistoryRepository.findAll()).thenReturn(expectedHistory);

        // Invocamos el método.
        List<CallHistory> result = callHistoryService.getHistory();

        // Verificamos el resultado.
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("/api/calculate", result.get(0).getEndpoint());
        assertEquals("num1=10&num2=20", result.get(0).getParameters());
        assertEquals("CalculationResponse{sum=30.0, percentage=10.0, result=33.0}", result.get(0).getResponse());
    }
}
