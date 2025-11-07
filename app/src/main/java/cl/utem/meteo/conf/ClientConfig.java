package cl.utem.meteo.conf;

import java.time.Duration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * Configuración del cliente HTTP para integraciones salientes.
 * <p>
 * Expone un {@link RestTemplate} con timeouts configurables vía propiedades:
 * </p>
 *
 * <ul>
 * <li><b>rest.connection.timeout</b> (segundos): tiempo máximo para establecer
 * la conexión.
 * <br>Por defecto: {@code 17}.</li>
 * <li><b>rest.request.timeout</b> (segundos): tiempo máximo de espera por
 * lectura de respuesta.
 * <br>Por defecto: {@code 23}.</li>
 * </ul>
 *
 */
@Configuration
public class ClientConfig {

    /**
     * Timeout de conexión en segundos (por defecto 17s).
     */
    @Value("${rest.connection.timeout:17}")
    private Integer connectTimeout;

    /**
     * Timeout de lectura en segundos (por defecto 23s).
     */
    @Value("${rest.request.timeout:23}")
    private Integer readTimeout;

    /**
     * Crea un {@link RestTemplate} con timeouts de conexión/lectura.
     *
     * @param builder {@link RestTemplateBuilder} provisto por Spring Boot
     * @return instancia configurada de {@link RestTemplate}
     */
    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder
                .connectTimeout(Duration.ofSeconds(connectTimeout))
                .readTimeout(Duration.ofSeconds(readTimeout))
                .build();
    }
}
