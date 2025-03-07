package org.jptorres.api_challengeTenpo.service;

/**
 * Interfaz que define el contrato para obtener un porcentaje.
 * Permite abstraer la fuente del porcentaje (por ejemplo, un servicio externo o un mock).
 *
 * @author Juan Pablo Torres
 * @version 1.0
 */
public interface PercentageProvider {
    /**
     * Obtiene el porcentaje.
     *
     * @return valor del porcentaje.
     */
    double getPercentage();
}
