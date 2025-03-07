package org.jptorres.api_challengeTenpo.service;

import org.jptorres.api_challengeTenpo.service.implementation.ExternalPercentageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

public class ExternalPercentageServiceTest {

    // Inyecta un mock en la instancia de ExternalPercentageService.
    @InjectMocks
    private ExternalPercentageService externalPercentageService;

    // Creamos un mock de RestTemplate para simular la llamada al API externo.
    @Mock
    private RestTemplate restTemplate;

    // La URL que utiliza el servicio.
    private static final String EXTERNAL_API_URL = "https://www.randomnumberapi.com/api/v1.0/random?min=1&max=100&count=1";

    @BeforeEach
    public void setUp() {
        // Inicializa los mocks.
        MockitoAnnotations.openMocks(this);
        // Inyecta el mock de RestTemplate en el campo privado "restTemplate" de ExternalPercentageService.
        ReflectionTestUtils.setField(externalPercentageService, "restTemplate", restTemplate);
    }

    /**
     * Test: Verifica que, cuando el API externo retorna una respuesta válida (un array con al menos un valor),
     * se retorne el porcentaje correctamente.
     */
    @Test
    public void testGetPercentage_Success() {
        // Configuramos una respuesta exitosa con un array que contiene, por ejemplo, el número 42.
        Integer[] responseArray = new Integer[]{42};
        ResponseEntity<Integer[]> responseEntity = new ResponseEntity<>(responseArray, HttpStatus.OK);
        when(restTemplate.getForEntity(eq(EXTERNAL_API_URL), eq(Integer[].class)))
                .thenReturn(responseEntity);

        double percentage = externalPercentageService.getPercentage();
        assertEquals(42, percentage);
    }

    /**
     * Test: Verifica que, cuando el API externo retorna una respuesta válida pero con un array vacío,
     * se lance una RuntimeException indicando respuesta inválida.
     */
    @Test
    public void testGetPercentage_InvalidResponse() {
        // Configuramos una respuesta exitosa con un array vacío.
        Integer[] responseArray = new Integer[]{};
        ResponseEntity<Integer[]> responseEntity = new ResponseEntity<>(responseArray, HttpStatus.OK);
        when(restTemplate.getForEntity(eq(EXTERNAL_API_URL), eq(Integer[].class)))
                .thenReturn(responseEntity);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> externalPercentageService.getPercentage());
        assertTrue(exception.getMessage().contains("Respuesta inválida"));
    }

    /**
     * Test: Verifica que, cuando ocurre una excepción (por ejemplo, de red) al consultar el API externo,
     * se lance una RuntimeException con el mensaje correspondiente.
     */
    @Test
    public void testGetPercentage_RestClientException() {
        // Configuramos el mock para que lance una excepción de RestClientException.
        when(restTemplate.getForEntity(eq(EXTERNAL_API_URL), eq(Integer[].class)))
                .thenThrow(new RestClientException("Network error"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> externalPercentageService.getPercentage());
        assertTrue(exception.getMessage().contains("Error al consultar el servicio externo"));
    }
}
