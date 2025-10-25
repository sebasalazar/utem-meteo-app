package cl.utem.meteo.utils;

import cl.utem.meteo.domain.data.RedMeteo;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class JsonUtils {

    public static final ObjectMapper MAPPER;

    static {
        MAPPER = new ObjectMapper();
        MAPPER.registerModule(new JavaTimeModule());
        MAPPER.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(JsonUtils.class);

    private JsonUtils() {
        throw new IllegalStateException();
    }

    public static List<RedMeteo> convertRedMeteo(final String json) {
        if (StringUtils.isBlank(json)) {
            return List.of();
        }

        try {
            TypeReference<List<RedMeteo>> typeReference = new TypeReference<List<RedMeteo>>() {
            };
            return MAPPER.readValue(json, typeReference);
        } catch (Exception e) {
            LOGGER.error("Error al convertir: {}", e.getLocalizedMessage());
            LOGGER.debug("Error al convertir: {}", e.getMessage(), e);
            return List.of();
        }
    }
}
