package org.jptorres.api_challengeTenpo.repository;

import org.jptorres.api_challengeTenpo.model.entiy.CallHistory;
import org.springframework.data.jpa.repository.JpaRepository;


/**
 * Repositorio JPA para la entidad CallHistory.
 * Permite realizar operaciones CRUD sobre el historial de llamadas.
 *
 * @author Juan Pablo Torres
 * @version 1.0
 */
public interface CallHistoryRepository extends JpaRepository<CallHistory, Long> {
}
