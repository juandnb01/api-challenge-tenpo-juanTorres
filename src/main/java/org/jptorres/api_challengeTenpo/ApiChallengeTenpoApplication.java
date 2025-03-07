package org.jptorres.api_challengeTenpo;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.concurrent.TimeUnit;


/**
 * Aplicación Spring Boot con soporte para caché y tareas programadas.
 *
 * @author Juan Pablo Torres
 * @version 1.0
 */
@SpringBootApplication
@EnableCaching
@EnableScheduling  // Habilita la ejecución de tareas programadas (@Scheduled)
public class ApiChallengeTenpoApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiChallengeTenpoApplication.class, args);
	}

	/**
	 * Configuración del CacheManager utilizando Caffeine.
	 * El caché "percentage" expirará 30 minutos después de haber sido escrito.
	 *
	 * @return CacheManager configurado.
	 */
	@Bean
	public CaffeineCacheManager cacheManager() {
		CaffeineCacheManager manager = new CaffeineCacheManager("percentage");
		manager.setCaffeine(Caffeine.newBuilder().expireAfterWrite(30, TimeUnit.MINUTES));
		return manager;
	}
}
