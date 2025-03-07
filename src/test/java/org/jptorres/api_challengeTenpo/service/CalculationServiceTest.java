package org.jptorres.api_challengeTenpo.service;

import org.jptorres.api_challengeTenpo.dto.CalculationResponse;
import org.jptorres.api_challengeTenpo.service.implementation.CalculationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cache.Cache;
import org.springframework.cache.caffeine.CaffeineCacheManager;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CalculationServiceTest {

    @Mock
    private CaffeineCacheManager cacheManager;

    @Mock
    private PercentageProvider percentageProvider;

    @Mock
    private Cache cache; // Mock de la caché para el nombre "percentage"

    @InjectMocks
    private CalculationService calculationService;

    @BeforeEach
    public void setUp() {
        // Configura el cacheManager para que retorne nuestro mock cache cuando se solicite la caché "percentage"
        when(cacheManager.getCache("percentage")).thenReturn(cache);
    }

    /**
     * Test: Escenario normal, el proveedor retorna un valor sin excepción.
     */
    @Test
    public void testGetCachedPercentage_Success() {
        double providerValue = 10.0;
        when(percentageProvider.getPercentage()).thenReturn(providerValue);

        double result = calculationService.getCachedPercentage();

        // Se espera que se actualice la caché con el valor retornado.
        verify(cache).put("value", providerValue);
        assertEquals(providerValue, result);
    }

    /**
     * Test: En caso de fallo del proveedor, pero existe un valor almacenado en la caché.
     */
    @Test
    public void testGetCachedPercentage_Fallback() {
        double cachedValue = 15.0;
        // Configuramos el proveedor para que lance excepción.
        when(percentageProvider.getPercentage()).thenThrow(new RuntimeException("Fallo externo"));
        // Simulamos que en la caché se encuentra el valor.
        when(cache.get("value", Double.class)).thenReturn(cachedValue);

        double result = calculationService.getCachedPercentage();

        // Se debe retornar el valor cacheado.
        assertEquals(cachedValue, result);
    }

    /**
     * Test: En caso de fallo del proveedor y no hay valor en la caché, se lanza una excepción.
     */
    @Test
    public void testGetCachedPercentage_NoCachedValue() {
        // Configuramos el proveedor para que lance excepción.
        when(percentageProvider.getPercentage()).thenThrow(new RuntimeException("Fallo externo"));
        // Simulamos que la caché no tiene valor (retorna null).
        when(cache.get("value", Double.class)).thenReturn(null);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            calculationService.getCachedPercentage();
        });

        assertTrue(exception.getMessage().contains("No cached percentage available"));
    }

    /**
     * Test: Verifica que el método updateCachedPercentage actualiza la caché y retorna el nuevo valor.
     */
    @Test
    public void testUpdateCachedPercentage() {
        double newPercentage = 20.0;
        when(percentageProvider.getPercentage()).thenReturn(newPercentage);

        double result = calculationService.updateCachedPercentage();

        // Verifica que se actualice la caché.
        verify(cache).put("value", newPercentage);
        assertEquals(newPercentage, result);
    }

    /**
     * Test: Verifica que el método calculate realice el cálculo correctamente.
     */
    @Test
    public void testCalculate() {
        double num1 = 10.0;
        double num2 = 20.0;
        // Para este test, supongamos que el porcentaje obtenido es 10.0.
        when(percentageProvider.getPercentage()).thenReturn(10.0);

        // Como calculate llama internamente a getCachedPercentage (que usa percentageProvider.getPercentage() y cache.put),
        // invocamos directamente el método calculate.
        CalculationResponse response = calculationService.calculate(num1, num2);

        // Verifica que se haya llamado a cache.put con el valor 10.0
        verify(cache).put("value", 10.0);

        // Se espera:
        // sum = num1 + num2 = 30.0
        // percentage = 10.0
        // result = 30.0 + (30.0 * (10.0 / 100)) = 33.0
        assertNotNull(response);
        assertEquals(30.0, response.getSum());
        assertEquals(10.0, response.getPercentage());
        assertEquals(33.0, response.getResult());
    }
}
