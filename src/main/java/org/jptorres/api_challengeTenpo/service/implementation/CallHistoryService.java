package org.jptorres.api_challengeTenpo.service.implementation;

import org.jptorres.api_challengeTenpo.model.entiy.CallHistory;
import org.jptorres.api_challengeTenpo.repository.CallHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Servicio encargado de gestionar el historial de llamadas a la API.
 * El registro se realiza de forma asíncrona para no afectar el rendimiento de la aplicación.
 *
 * @author Juan Pablo Torres
 * @version 1.0
 */
@Service
public class CallHistoryService {

    // Repositorio para interactuar con la entidad CallHistory.
    @Autowired
    private CallHistoryRepository callHistoryRepository;

    /**
     * Registra de forma asíncrona una llamada a la API.
     * La anotación @Async permite que este proceso se ejecute en un hilo separado.
     * Además, @Transactional asegura que la operación sobre la base de datos se realice correctamente.
     *
     * @param endpoint  Endpoint que fue invocado.
     * @param parameters Parámetros de la llamada.
     * @param response  Respuesta o error generado.
     */
    @Async
    @Transactional
    public void logCall(String endpoint, String parameters, String response) {
        CallHistory history = new CallHistory(endpoint, parameters, response);
        callHistoryRepository.save(history);
    }

    /**
     * Recupera el historial completo de llamadas registradas.
     *
     * @return Lista de CallHistory.
     */
    public List<CallHistory> getHistory() {
        return callHistoryRepository.findAll();
    }
}