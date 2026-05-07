package org.tfg.grant_java;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * Entry point for the Grant Java Spring Boot application.
 *
 * <p>This class bootstraps the application using Spring Boot auto-configuration
 * and component scanning. It also enables global Cross-Origin Resource Sharing
 * (CORS) to allow requests from any origin.</p>
 *
 * <p><b>Key responsibilities:</b></p>
 * <ul>
 *   <li>Initialize and start the Spring Boot application</li>
 *   <li>Enable component scanning and auto-configuration</li>
 *   <li>Provide a centralized application startup log entry</li>
 * </ul>
 *
 * <p><b>CORS note:</b> The {@link CrossOrigin} configuration allows all origins
 * and headers. This is typically acceptable for development environments.
 * For production, consider restricting allowed origins and headers.</p>
 */
@Slf4j
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
@SpringBootApplication
public class GrantJavaApplication {

	/**
	 * Main method that launches the Spring Boot application.
	 *
	 * @param args application startup arguments
	 */
	public static void main(String[] args) {

		log.info("Starting Grant Java Application");
		SpringApplication.run(GrantJavaApplication.class, args);
	}
}