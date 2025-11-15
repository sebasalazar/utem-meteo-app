package cl.utem.meteo.tasks;

import cl.utem.meteo.domain.data.out.RedMeteo;
import cl.utem.meteo.manager.RedMeteoManager;
import cl.utem.meteo.utils.JsonUtils;
import java.net.URI;
import java.util.List;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * Tarea programada que obtiene las últimas observaciones desde RedMeteo y
 * delega la persistencia a {@link RedMeteoManager}.
 * <p>
 * Flujo:
 * <ol>
 * <li>Consulta HTTP al endpoint provisto por
 * {@code RedMeteoManager#getRedMeteoUri()}.</li>
 * <li>Parseo del JSON a {@code List<RedMeteo>} con
 * {@link JsonUtils#convertRedMeteo(String)}.</li>
 * <li>Persistencia por elemento (en paralelo) mediante
 * {@link RedMeteoManager#saveObs(RedMeteo)}.</li>
 * </ol>
 * <b>Nota:</b> se asume que el {@link RestTemplate} inyectado está configurado
 * con timeouts.
 */
@Component
public class ObservationTask {

    private final RestTemplate restTemplate;
    private final RedMeteoManager redMeteoManager;

    private static final Logger LOGGER = LoggerFactory.getLogger(ObservationTask.class);

    @Autowired
    public ObservationTask(RestTemplate restTemplate, RedMeteoManager redMeteoManager) {
        this.restTemplate = restTemplate;
        this.redMeteoManager = redMeteoManager;
    }

    /**
     * Intenta persistir una observación individual, aislando fallas por
     * registro.
     *
     * @param redMeteo DTO recibido desde RedMeteo; si es {@code null} se omite
     */
    private void saveObs(final RedMeteo redMeteo) {
        try {
            if (redMeteo != null) {
                redMeteoManager.saveObs(redMeteo);
            }
        } catch (Exception e) {
            LOGGER.error("Error al guardar observación: {}", e.getLocalizedMessage());
            LOGGER.debug("Error al guardar observación: {}", e.getMessage(), e);
        }
    }

    /**
     * Realiza la consulta HTTP al endpoint de RedMeteo y procesa el resultado.
     * Registra WARN si el status HTTP no es 2xx y maneja cuerpo nulo/vacío.
     */
    private void query() {
        try {
            final URI redMeteoUri = redMeteoManager.getRedMeteoUri();
            ResponseEntity<String> response = restTemplate.getForEntity(redMeteoUri, String.class);

            if (!response.getStatusCode().is2xxSuccessful()) {
                LOGGER.warn("Consulta RedMeteo no exitosa: status={}", response.getStatusCode());
                return;
            }

            final String body = response.getBody();
            if (body == null || body.isBlank()) {
                LOGGER.info("Respuesta vacía desde RedMeteo (body nulo o en blanco).");
                return;
            }

            List<RedMeteo> list = JsonUtils.convertRedMeteo(body);
            if (CollectionUtils.isNotEmpty(list)) {
                list.parallelStream().forEach(this::saveObs);
            } else {
                LOGGER.warn("Sin observaciones para procesar.");
            }
        } catch (Exception e) {
            LOGGER.error("Error al consultar operación: {}", e.getLocalizedMessage());
            LOGGER.debug("Error al consultar operación: {}", e.getMessage(), e);
        }
    }

    /**
     * Ejecuta la tarea cada 10 minutos (ISO-8601: {@code PT10M}) con demora
     * fija desde el fin de la ejecución previa.
     */
    @Scheduled(fixedDelayString = "PT10M")
    public void runScheduled() {
        LOGGER.info("Ejecutando actualización");
        query();
    }
}
