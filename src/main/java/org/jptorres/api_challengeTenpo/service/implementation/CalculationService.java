package org.jptorres.api_challengeTenpo.service.implementation;

import org.jptorres.api_challengeTenpo.dto.CalculationResponse;
import org.jptorres.api_challengeTenpo.service.PercentageProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CachePut;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.stereotype.Service;

/**
 * Servicio que contiene la lógica para realizar cálculos y gestionar el porcentaje obtenido.
 * Utiliza caché para almacenar el porcentaje obtenido del servicio externo real.
 *
 * - Al obtener el porcentaje se actualiza la caché.
 * - Si el proveedor externo falla, se utiliza el último valor cacheado.
 * - La caché tiene un tiempo de vida de 30 minutos (configurado en el CacheManager).
 *
 * @author Juan Pablo Torres
 * @version 1.0
 */
@Service
@CacheConfig(cacheNames = {"percentage"})
public class CalculationService {

    // Gestor de caché configurado con Caffeine.
    @Autowired
    private CaffeineCacheManager cacheManager;

    // Proveedor del porcentaje que consulta un servicio externo real.
    @Autowired
    private PercentageProvider percentageProvider;

    /**
     * Obtiene el porcentaje actual. Se intenta consultar el servicio externo y actualizar la caché.
     * Si ocurre algún fallo, se utiliza el último valor almacenado en la caché; si no existe,
     * se lanza una excepción.
     *
     * @return el porcentaje actual.
     */
    public double getCachedPercentage() {
        try {
            double percentage = percentageProvider.getPercentage();
            // Actualiza la caché con el nuevo valor.
            cacheManager.getCache("percentage").put("value", percentage);
            return percentage;
        } catch (Exception e) {
            // En caso de fallo del servicio externo, se intenta obtener el último valor cacheado.
            Double cachedValue = cacheManager.getCache("percentage").get("value", Double.class);
            if (cachedValue != null) {
                return cachedValue;
            } else {
                throw new RuntimeException("No cached percentage available and external service failed", e);
            }
        }
    }

    /**
     * Actualiza el porcentaje en la caché de forma programada cada 30 minutos.
     * Se utiliza @CachePut para asegurar que el valor actualizado se almacene en la caché.
     *
     * @return el nuevo porcentaje obtenido del servicio externo.
     */
    @CachePut(value = "percentage", key = "'value'")
    @Scheduled(fixedRate = 1800000) // 30 minutos en milisegundos
    public double updateCachedPercentage() {
        double newPercentage = percentageProvider.getPercentage();
        // Se actualiza la caché manualmente para garantizar la consistencia.
        cacheManager.getCache("percentage").put("value", newPercentage);
        return newPercentage;
    }

    /**
     * Realiza el cálculo sumando num1 y num2 y aplicando el porcentaje obtenido.
     *
     * @param num1 Primer número.
     * @param num2 Segundo número.
     * @return CalculationResponse que contiene la suma, el porcentaje aplicado y el resultado final.
     */
    public CalculationResponse calculate(double num1, double num2) {
        double sum = num1 + num2;
        double percentage = getCachedPercentage();
        double result = sum + (sum * (percentage / 100));
        return new CalculationResponse(sum, percentage, result);
    }
}
