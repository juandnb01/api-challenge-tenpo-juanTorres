package org.jptorres.api_challengeTenpo.controller;


import jakarta.validation.constraints.NotNull;
import org.jptorres.api_challengeTenpo.dto.CalculationResponse;
import org.jptorres.api_challengeTenpo.model.entiy.CallHistory;
import org.jptorres.api_challengeTenpo.service.implementation.CalculationService;
import org.jptorres.api_challengeTenpo.service.implementation.CallHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controlador REST que expone endpoints para:
 *  - Realizar el cálculo con porcentaje dinámico.
 *  - Consultar el historial de llamadas.
 *  - Obtener el porcentaje actual.
 *
 * Se agregó manejo de errores y validación de parámetros para mejorar la robustez.
 *
 * @author Juan Pablo Torres
 * @version 1.0
 */
@RestController
@RequestMapping("/api")
@Validated
public class CalculationController {

    // Servicio encargado de realizar el cálculo y gestionar la caché.
    @Autowired
    private CalculationService calculationService;

    // Servicio para registrar y obtener el historial de llamadas.
    @Autowired
    private CallHistoryService callHistoryService;

    /**
     * Endpoint para calcular la suma de num1 y num2 y aplicar un porcentaje adicional.
     * Ejemplo de uso: /api/calculate?num1=10&num2=20
     *
     * @param num1 Primer número. Requerido.
     * @param num2 Segundo número. Requerido.
     * @return CalculationResponse con la suma, porcentaje aplicado y resultado final.
     */
    @GetMapping("/calculate")
    public ResponseEntity<CalculationResponse> calculate(
            @RequestParam @NotNull(message = "num1 es requerido") Double num1,
            @RequestParam @NotNull(message = "num2 es requerido") Double num2) {
        CalculationResponse response = calculationService.calculate(num1, num2);
        // Registro asíncrono del historial de llamadas.
        callHistoryService.logCall("/api/calculate", "num1=" + num1 + "&num2=" + num2, response.toString());
        return ResponseEntity.ok(response);
    }

    /**
     * Endpoint para consultar el historial de llamadas.
     *
     * @return Lista de CallHistory con la información de cada llamada (fecha, endpoint, parámetros, respuesta o error).
     */
    @GetMapping("/history")
    public ResponseEntity<List<CallHistory>> getHistory() {
        return ResponseEntity.ok(callHistoryService.getHistory());
    }

    /**
     * Endpoint para obtener el porcentaje actual.
     * Este porcentaje proviene del servicio externo o de la caché.
     *
     * @return Valor del porcentaje.
     */
    @GetMapping("/percentage")
    public ResponseEntity<Double> getPercentage() {
        return ResponseEntity.ok(calculationService.getCachedPercentage());
    }

    /**
     * Manejador global de excepciones para RuntimeException.
     *
     * @param ex Excepción capturada.
     * @return ResponseEntity con mensaje de error y código HTTP 500.
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntimeException(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
    }
}