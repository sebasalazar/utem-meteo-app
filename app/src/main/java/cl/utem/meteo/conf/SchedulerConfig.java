package cl.utem.meteo.conf;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Configuración de tareas programadas (scheduling) para la aplicación.
 * <p>
 * Al habilitar {@link EnableScheduling}, Spring detecta y ejecuta
 * automáticamente los métodos anotados con {@code @Scheduled} en los beans del
 * contexto.
 * </p>
 *
 */
@Configuration
@EnableScheduling
public class SchedulerConfig {

}
