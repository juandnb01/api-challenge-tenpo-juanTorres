package org.jptorres.api_challengeTenpo.service.implementation;

import org.jptorres.api_challengeTenpo.service.PercentageProvider;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

/**
 * Implementación de PercentageProvider que consulta un servicio externo real para obtener un porcentaje.
 * En este ejemplo se utiliza la API de Random Number (https://www.randomnumberapi.com) para obtener un valor aleatorio
 * entre 1 y 100, que se usará como porcentaje.
 */
@Service
public class ExternalPercentageService implements PercentageProvider {

    // URL del servicio externo que retorna un array con un número aleatorio.
    private static final String EXTERNAL_API_URL = "https://www.randomnumberapi.com/api/v1.0/random?min=1&max=100&count=1";

    private final RestTemplate restTemplate = new RestTemplate();

    /**
     * Consulta el API externo para obtener un porcentaje.
     *
     * @return el porcentaje obtenido del servicio externo.
     * @throws RuntimeException si la respuesta es inválida o si ocurre algún error en la consulta.
     */
    @Override
    public double getPercentage() {
        try {
            // Se realiza la petición GET al API externo.
            ResponseEntity<Integer[]> responseEntity = restTemplate.getForEntity(EXTERNAL_API_URL, Integer[].class);
            // Se verifica que la respuesta sea exitosa y contenga al menos un valor.
            if (responseEntity.getStatusCode().is2xxSuccessful()
                    && responseEntity.getBody() != null
                    && responseEntity.getBody().length > 0) {
                return responseEntity.getBody()[0];
            } else {
                throw new RuntimeException("Respuesta inválida del servicio externo");
            }
        } catch (RestClientException e) {
            throw new RuntimeException("Error al consultar el servicio externo", e);
        }
    }
}
