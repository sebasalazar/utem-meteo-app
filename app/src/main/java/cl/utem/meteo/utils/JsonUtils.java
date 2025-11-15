package cl.utem.meteo.utils;

import cl.utem.meteo.domain.data.out.RedMeteo;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utilidades JSON para la capa meteo.
 * <p>
 * Expone un {@link ObjectMapper} configurado para fechas Java 8+ (JSR-310) y un
 * método seguro para convertir un arreglo JSON a {@code List<RedMeteo>}.
 * </p>
 *
 * <h2>Detalles de configuración</h2>
 * <ul>
 * <li>Registra {@link JavaTimeModule} para soportar {@code LocalDate},
 * {@code Instant}, etc.</li>
 * <li>Deshabilita {@link SerializationFeature#WRITE_DATES_AS_TIMESTAMPS} para
 * serializar fechas como ISO-8601.</li>
 * <li>Deshabilita {@link DeserializationFeature#FAIL_ON_UNKNOWN_PROPERTIES}
 * para tolerar campos extra en el JSON.</li>
 * </ul>
 *
 * <h2>Notas de diseño</h2>
 * <ul>
 * <li>Clase de utilidades final con constructor privado: no instanciable.</li>
 * <li>{@code MAPPER} es <em>thread-safe</em> una vez construido y sólo se
 * configura en el bloque estático.</li>
 * <li>Ante entrada vacía o error de parseo, retorna {@code List.of()} (lista
 * inmutable vacía) y registra logs.</li>
 * </ul>
 */
public final class JsonUtils {

    /**
     * Mapper global para (de)serialización JSON. Es seguro para uso
     * concurrente.
     */
    public static final ObjectMapper MAPPER;

    static {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        MAPPER = mapper;
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(JsonUtils.class);

    /**
     * Evita la instanciación.
     */
    private JsonUtils() {
        throw new IllegalStateException("JsonUtils no debe instanciarse");
    }

    /**
     * Convierte un JSON que representa un arreglo de {@link RedMeteo} en una
     * lista tipada. Si el JSON es nulo/vacío/espacios, retorna
     * {@code List.of()}. Si ocurre un error de parseo, registra el error y
     * retorna {@code List.of()}.
     *
     * @param json cadena JSON (puede ser {@code null} o en blanco)
     * @return lista inmutable de {@link RedMeteo}; vacía en caso de entrada
     * inválida o error
     */
    public static List<RedMeteo> convertRedMeteo(final String json) {
        if (StringUtils.isBlank(json)) {
            return List.of();
        }
        try {
            TypeReference<List<RedMeteo>> typeReference = new TypeReference<List<RedMeteo>>() {
            };
            return MAPPER.readValue(json, typeReference);
        } catch (Exception e) {
            // Mensaje corto a ERROR y stacktrace a DEBUG para no contaminar logs en producción.
            LOGGER.error("Error al convertir RedMeteo: {}", e.getLocalizedMessage());
            LOGGER.debug("Error al convertir RedMeteo: {}", e.getMessage(), e);
            return List.of();
        }
    }
}
