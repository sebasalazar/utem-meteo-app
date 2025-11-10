package cl.utem.meteo.utils;

import cl.utem.meteo.exception.ValidationException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utilidades de validación para códigos de estación meteorológica.
 *
 * <p>
 * Reglas de validación aplicadas por {@link #validate(String, String)}:
 * <ul>
 * <li>El código no puede ser {@code null} ni vacío.</li>
 * <li>El largo debe ser exactamente 8 caracteres.</li>
 * <li>Debe comenzar con el prefijo {@value #STATION_CODE_PREFIX}.</li>
 * </ul>
 *
 * <p>
 * <strong>Notas de uso</strong>:
 * <ul>
 * <li>El parámetro {@code logStationCode} se utiliza solo para mensajes de log
 * (por ejemplo, una versión enmascarada).</li>
 * <li>Los detalles de validación se registran a nivel {@code DEBUG}.</li>
 * <li>La clase es estática y libre de estado; es segura para uso
 * concurrente.</li>
 * </ul>
 *
 * <p>
 * <strong>Ejemplo</strong>:
 * <pre>{@code
 * try {
 *     ValidationUtils.validate("RMCL1234", "RMCL1***");
 *     // Código válido
 * } catch (ValidationException ex) {
 *     // Manejar código inválido
 * }
 * }</pre>
 */
public final class ValidationUtils {

    /**
     * Prefijo requerido para un código de estación válido.
     * <p>
     * Ejemplo válido: {@code RMCL1234}</p>
     */
    private static final String STATION_CODE_PREFIX = "RMCL";

    private static final Logger LOGGER = LoggerFactory.getLogger(ValidationUtils.class);

    /**
     * Clase de utilidades: evita la instanciación.
     *
     * @throws IllegalStateException siempre, al intentar instanciar la clase.
     */
    private ValidationUtils() {
        throw new IllegalStateException("Clase utilitaria no instanciable");
    }

    /**
     * Valida un código de estación según las reglas de negocio definidas.
     *
     * <p>
     * Condiciones de error:
     * <ul>
     * <li>Si {@code stationCode} es {@code null} o vacío, se lanza
     * {@link ValidationException} con el mensaje:
     * {@code "Se necesita un código de estación válido"}.</li>
     * <li>Si el largo de {@code stationCode} es distinto de 8, se lanza
     * {@link ValidationException} con el mensaje:
     * {@code "El largo del código es inválido"}.</li>
     * <li>Si {@code stationCode} no inicia con {@value #STATION_CODE_PREFIX},
     * se lanza {@link ValidationException} con el mensaje:
     * {@code "Se necesita un código válido"}.</li>
     * </ul>
     *
     * <p>
     * Esta operación no modifica estado ni realiza I/O; solo puede escribir log
     * a nivel DEBUG.
     *
     * @param stationCode código de estación a validar (p. ej.,
     * {@code RMCL1234}).
     * @param logStationCode representación segura del código para registrar en
     * log (p. ej., enmascarado).
     * @throws ValidationException si el código es nulo/vacío, tiene largo
     * inválido o no cumple el prefijo requerido.
     */
    public static void validate(final String stationCode, final String logStationCode) {
        if (StringUtils.isEmpty(stationCode)) {
            throw new ValidationException("Se necesita un código de estación válido");
        }

        final int lengthStationCode = StringUtils.length(stationCode);
        if (lengthStationCode != 8) {
            LOGGER.debug("El código ({}) tiene largo {}", logStationCode, lengthStationCode);
            throw new ValidationException("El largo del código es inválido");
        }

        if (!StringUtils.startsWith(stationCode, STATION_CODE_PREFIX)) {
            LOGGER.debug("El código ({}) no comienza con el prefijo válido '{}'", logStationCode, STATION_CODE_PREFIX);
            throw new ValidationException("Se necesita un código válido");
        }
    }
}
