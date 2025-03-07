package org.jptorres.api_challengeTenpo.controller;

import org.jptorres.api_challengeTenpo.dto.CalculationResponse;
import org.jptorres.api_challengeTenpo.model.entiy.CallHistory;
import org.jptorres.api_challengeTenpo.service.implementation.CalculationService;
import org.jptorres.api_challengeTenpo.service.implementation.CallHistoryService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CalculationControllerTest {

    @Mock
    private CalculationService calculationService;

    @Mock
    private CallHistoryService callHistoryService;

    @InjectMocks
    private CalculationController controller;

    /**
     * Prueba el endpoint /api/calculate.
     * Se verifica que, al invocar el método calculate del controlador,
     * se llame al servicio correspondiente y se retorne la respuesta esperada.
     */
    @Test
    public void testCalculate() {
        // Datos de prueba
        double num1 = 10.0;
        double num2 = 20.0;
        CalculationResponse expectedResponse = new CalculationResponse(30.0, 10.0, 33.0);

        // Configuración de mocks
        when(calculationService.calculate(num1, num2)).thenReturn(expectedResponse);

        // Invoca el endpoint
        ResponseEntity<CalculationResponse> responseEntity = controller.calculate(num1, num2);

        // Verificaciones
        assertNotNull(responseEntity);
        assertEquals(200, responseEntity.getStatusCodeValue());
        assertEquals(expectedResponse, responseEntity.getBody());

        // Verifica que se haya registrado la llamada en el historial.
        verify(callHistoryService, times(1))
                .logCall("/api/calculate", "num1=" + num1 + "&num2=" + num2, expectedResponse.toString());
    }

    /**
     * Prueba el endpoint /api/history.
     * Se verifica que se retorne la lista de historial de llamadas.
     */
    @Test
    public void testGetHistory() {
        // Datos de prueba: Crea un registro de historial.
        CallHistory historyEntry = new CallHistory("/api/calculate", "num1=10&num2=20",
                "CalculationResponse{sum=30.0, percentage=10.0, result=33.0}");
        List<CallHistory> expectedHistory = Arrays.asList(historyEntry);

        // Configura el mock
        when(callHistoryService.getHistory()).thenReturn(expectedHistory);

        // Invoca el endpoint
        ResponseEntity<List<CallHistory>> responseEntity = controller.getHistory();

        // Verificaciones
        assertNotNull(responseEntity);
        assertEquals(200, responseEntity.getStatusCodeValue());
        assertEquals(expectedHistory, responseEntity.getBody());
    }

    /**
     * Prueba el endpoint /api/percentage.
     * Se verifica que se retorne el porcentaje actual obtenido del servicio.
     */
    @Test
    public void testGetPercentage() {
        // Dato de prueba
        double expectedPercentage = 10.0;

        // Configura el mock
        when(calculationService.getCachedPercentage()).thenReturn(expectedPercentage);

        // Invoca el endpoint
        ResponseEntity<Double> responseEntity = controller.getPercentage();

        // Verificaciones
        assertNotNull(responseEntity);
        assertEquals(200, responseEntity.getStatusCodeValue());
        assertEquals(expectedPercentage, responseEntity.getBody());
    }

    /**
     * Prueba el manejador de excepciones global para RuntimeException.
     * Se verifica que, al lanzar una excepción, se retorne un error 500 con el mensaje esperado.
     */
    @Test
    public void testHandleRuntimeException() {
        // Dato de prueba: mensaje de error
        String errorMessage = "Test error message";
        RuntimeException ex = new RuntimeException(errorMessage);

        // Invoca el manejador de excepciones
        ResponseEntity<String> responseEntity = controller.handleRuntimeException(ex);

        // Verificaciones
        assertNotNull(responseEntity);
        assertEquals(500, responseEntity.getStatusCodeValue());
        assertEquals(errorMessage, responseEntity.getBody());
    }
}
