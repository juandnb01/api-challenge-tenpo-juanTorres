package org.jptorres.api_challengeTenpo.model.entiy;

import jakarta.persistence.*;

import java.time.LocalDateTime;

/**
 * Entidad JPA que representa un registro del historial de llamadas a la API.
 *
 * @author Juan Pablo Torres
 * @version 1.0
 */
@Entity
@Table(name = "call_history")
public class CallHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String endpoint;
    private String parameters;
    private String response;
    private LocalDateTime timestamp;

    public CallHistory() {}

    public CallHistory(String endpoint, String parameters, String response) {
        this.endpoint = endpoint;
        this.parameters = parameters;
        this.response = response;
        this.timestamp = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public String getEndpoint() { return endpoint; }
    public String getParameters() { return parameters; }
    public String getResponse() { return response; }
    public LocalDateTime getTimestamp() { return timestamp; }
}
