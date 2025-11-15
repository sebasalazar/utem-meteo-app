package cl.utem.meteo.api;

import cl.utem.meteo.exception.AuthException;
import cl.utem.meteo.exception.NoDataException;
import cl.utem.meteo.exception.ValidationException;
import jakarta.servlet.http.HttpServletRequest;
import java.net.URI;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import jakarta.validation.ConstraintViolationException;
import java.util.Objects;

/**
 * Manejador global de excepciones para la API Meteo.
 * <p>
 * Esta clase transforma excepciones lanzadas por los controladores/rest
 * controllers en respuestas HTTP estandarizadas utilizando el formato
 * <a href="https://datatracker.ietf.org/doc/html/rfc7807">Problem Details (RFC
 * 7807)</a>.
 * </p>
 *
 * <h2>Características</h2>
 * <ul>
 * <li>Unifica el manejo de errores y códigos de estado HTTP.</li>
 * <li>Incluye detalles adicionales en el {@link ProblemDetail}:
 * <code>timestamp</code>, <code>errorCode</code>, <code>method</code>,
 * <code>query</code> y <code>traceId</code> (si está presente en
 * {@link MDC}).</li>
 * <li>Registra el error a dos niveles: {@code error} (mensaje corto) y
 * {@code debug} (stack trace).</li>
 * <li>Establece el {@code type} del problema como una URL descriptiva por
 * código de estado (MDN).</li>
 * </ul>
 *
 * <h2>Diseño</h2>
 * <p>
 * La clase es <em>stateless</em> y segura para concurrencia en el contexto
 * habitual de Spring, ya que no mantiene estado mutable entre llamadas.
 * </p>
 */
@RestControllerAdvice
public class ApiExceptionHandler {

    private static final String MSG = "message";
    private static final Logger LOGGER = LoggerFactory.getLogger(ApiExceptionHandler.class);

    /**
     * Enumeración de códigos de error internos para consumo por clientes y
     * trazabilidad.
     * <ul>
     * <li>{@link #SA} — Sin autorización (HTTP 401).</li>
     * <li>{@link #SD} — Sin datos (HTTP 404).</li>
     * <li>{@link #VF} — Validación fallida (propia del dominio) (HTTP
     * 400).</li>
     * <li>{@link #MI} — {@link MethodArgumentNotValidException} (HTTP
     * 400).</li>
     * <li>{@link #MP} — Mala petición genérica (HTTP 400).</li>
     * <li>{@link #MT} — Media Type no soportado (HTTP 415).</li>
     * <li>{@link #MS} — Método HTTP no permitido (HTTP 405).</li>
     * <li>{@link #DC} — Desconocido / no manejado (HTTP 500).</li>
     * </ul>
     */
    public enum ErrorCode {
        SA, // Sin autorización
        SD, // Sin datos
        VF, // Validación fallida (custom)
        MI, // MethodArgumentInvalid
        MP, // Mala petición (genérico)
        MT, // Media Type
        MS, // Method not supported
        DC  // Desconocido
    }

    /**
     * Construye un {@link ProblemDetail} enriquecido con metadatos de la
     * solicitud y trazabilidad.
     *
     * <p>
     * Propiedades añadidas:</p>
     * <ul>
     * <li><b>type</b>: URL a documentación del código de estado (MDN, en
     * español).</li>
     * <li><b>title</b>: motivo estándar del estado HTTP.</li>
     * <li><b>timestamp</b>: instante del servidor en {@link Instant}.</li>
     * <li><b>errorCode</b>: uno de {@link ErrorCode}.</li>
     * <li><b>instance</b>: URI del recurso solicitado (si disponible).</li>
     * <li><b>method</b>: método HTTP.</li>
     * <li><b>query</b>: query string (si existe).</li>
     * <li><b>traceId</b>: identificador de trazas desde {@link MDC}
     * (opcional).</li>
     * </ul>
     *
     * @param request la solicitud HTTP que originó el error (puede ser
     * {@code null} en escenarios no web)
     * @param status código de estado HTTP de la respuesta
     * @param detail descripción legible para humanos; se aplica
     * {@link StringUtils#trimToEmpty(String)}
     * @param errorCode código de error interno para clasificación
     * @return el objeto {@link ProblemDetail} listo para ser enviado en la
     * respuesta
     */
    private ProblemDetail makeProblemDetail(HttpServletRequest request, HttpStatus status, String detail, ErrorCode errorCode) {

        final URI type = URI.create("https://developer.mozilla.org/es/docs/Web/HTTP/Reference/Status/" + status.value());
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(status, StringUtils.trimToEmpty(detail));
        pd.setType(type);
        pd.setTitle(status.getReasonPhrase());
        pd.setProperty("timestamp", Instant.now());
        pd.setProperty("errorCode", errorCode.name());

        if (request != null) {
            if (StringUtils.isNotBlank(request.getRequestURI())) {
                pd.setInstance(URI.create(request.getRequestURI()));
            }
            pd.setProperty("method", request.getMethod());
            String qs = request.getQueryString();
            if (StringUtils.isNotBlank(qs)) {
                pd.setProperty("query", qs);
            }
        }

        String traceId = MDC.get("traceId");
        if (StringUtils.isNotBlank(traceId)) {
            pd.setProperty("traceId", traceId);
        }

        return pd;
    }

    /**
     * Genera una {@link ResponseEntity} con {@link ProblemDetail} y registra el
     * error.
     *
     * <p>
     * Registra: </p>
     * <ul>
     * <li>Nivel <b>error</b>: mensaje corto (clase de excepción +
     * localizedMessage).</li>
     * <li>Nivel <b>debug</b>: stack trace completo.</li>
     * </ul>
     *
     * @param <T> tipo de excepción manejada
     * @param request solicitud HTTP asociada (puede ser {@code null})
     * @param status estado HTTP que se devolverá al cliente
     * @param e excepción original
     * @param logMessage mensaje de contexto usado en el log
     * @param errorCode código de error interno informativo en el
     * {@link ProblemDetail}
     * @return respuesta HTTP con cuerpo Problem Details y el estado indicado
     */
    private <T extends Exception> ResponseEntity<ProblemDetail> buildErrorResponse(
            HttpServletRequest request, HttpStatus status, T e, String logMessage, ErrorCode errorCode) {

        String shortMsg = StringUtils.defaultIfBlank(e.getLocalizedMessage(), e.getClass().getSimpleName());
        LOGGER.error("{}: {}", logMessage, shortMsg);
        LOGGER.debug("{}: {}", logMessage, e.getMessage(), e);

        ProblemDetail body = makeProblemDetail(request, status, shortMsg, errorCode);
        return new ResponseEntity<>(body, status);
    }

    /**
     * Maneja {@link AuthException} devolviendo <b>401 Unauthorized</b>.
     *
     * @param request la solicitud HTTP
     * @param e la excepción de autorización
     * @return Problem Details con {@code errorCode=SA}
     */
    @ExceptionHandler(AuthException.class)
    public ResponseEntity<ProblemDetail> handleAuthException(HttpServletRequest request, AuthException e) {
        return buildErrorResponse(request, HttpStatus.UNAUTHORIZED, e, "Sin autorización", ErrorCode.SA);
    }

    /**
     * Maneja {@link NoDataException} devolviendo <b>404 Not Found</b>.
     *
     * @param request la solicitud HTTP
     * @param e excepción indicando ausencia de datos
     * @return Problem Details con {@code errorCode=SD}
     */
    @ExceptionHandler(NoDataException.class)
    public ResponseEntity<ProblemDetail> handleNoDataException(HttpServletRequest request, NoDataException e) {
        return buildErrorResponse(request, HttpStatus.NOT_FOUND, e, "Sin datos", ErrorCode.SD);
    }

    /**
     * Maneja {@link ValidationException} de dominio devolviendo <b>400 Bad
     * Request</b>.
     *
     * @param request la solicitud HTTP
     * @param e excepción de validación del dominio
     * @return Problem Details con {@code errorCode=VF}
     */
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ProblemDetail> handleValidationException(HttpServletRequest request, ValidationException e) {
        return buildErrorResponse(request, HttpStatus.BAD_REQUEST, e, "Error de validación", ErrorCode.VF);
    }

    /**
     * Maneja errores de validación de argumentos anotados con {@code @Valid} en
     * el cuerpo o parámetros del request. Devuelve <b>400 Bad Request</b> y una
     * lista de errores por campo en la propiedad <code>errors</code>.
     *
     * <p>
     * Ejemplo de elemento en <code>errors</code>:</p>
     * <pre>
     * {"field":"nombre","message":"no debe estar vacío"}
     * </pre>
     *
     * @param request la solicitud HTTP
     * @param ex la excepción de Spring con los errores de binding/validación
     * @return Problem Details con {@code errorCode=MI} y propiedad
     * <code>errors</code>
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ProblemDetail> handleMethodArgumentNotValid(HttpServletRequest request, MethodArgumentNotValidException ex) {
        List<Map<String, String>> errors = ex.getBindingResult().getFieldErrors().stream()
                .map(err -> Map.of(
                "field", err.getField(),
                MSG, Objects.toString(StringUtils.trimToNull(err.getDefaultMessage()), "Invalid value")
        ))
                .toList();

        String detail = errors.stream()
                .map(m -> m.get("field") + ": " + m.get(MSG))
                .collect(Collectors.joining(", "));

        ProblemDetail pd = makeProblemDetail(request, HttpStatus.BAD_REQUEST, detail, ErrorCode.MI);
        pd.setProperty("errors", errors);
        return new ResponseEntity<>(pd, HttpStatus.BAD_REQUEST);
    }

    /**
     * Maneja validaciones declarativas realizadas mediante
     * {@link jakarta.validation.ConstraintViolation} (por ejemplo, en
     * parámetros de métodos anotados con {@code @Validated}). Devuelve <b>400
     * Bad Request</b>.
     *
     * <p>
     * Incluye una colección <code>errors</code> con pares <code>property</code>
     * y <code>message</code>.</p>
     *
     * @param request la solicitud HTTP
     * @param ex la excepción que encapsula las violaciones
     * @return Problem Details con {@code errorCode=MP} y la lista de
     * violaciones
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ProblemDetail> handleConstraintViolation(HttpServletRequest request, ConstraintViolationException ex) {
        List<Map<String, String>> errors = ex.getConstraintViolations().stream()
                .map(v -> Map.of(
                "property", String.valueOf(v.getPropertyPath()),
                MSG, Objects.toString(StringUtils.trimToNull(v.getMessage()), "Invalid value")
        )).toList();

        String detail = errors.stream()
                .map(m -> m.get("property") + ": " + m.get(MSG))
                .collect(Collectors.joining(", "));

        ProblemDetail pd = makeProblemDetail(request, HttpStatus.UNPROCESSABLE_ENTITY, detail, ErrorCode.MP);
        pd.setProperty("errors", errors);
        return new ResponseEntity<>(pd, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    /**
     * Maneja cuerpos de solicitud inválidos o mal formados, devolviendo <b>400
     * Bad Request</b>.
     *
     * @param request la solicitud HTTP
     * @param e la excepción asociada al parseo del cuerpo
     * @return Problem Details con {@code errorCode=MP}
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ProblemDetail> handleNotReadable(HttpServletRequest request, HttpMessageNotReadableException e) {
        return buildErrorResponse(request, HttpStatus.BAD_REQUEST, e, "Cuerpo de solicitud inválido o malformado", ErrorCode.MP);
    }

    /**
     * Maneja errores de conversión de tipos en parámetros (path/query),
     * devolviendo <b>400 Bad Request</b>.
     *
     * @param request la solicitud HTTP
     * @param e la excepción por incompatibilidad de tipos
     * @return Problem Details con {@code errorCode=MP}
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ProblemDetail> handleTypeMismatch(HttpServletRequest request, MethodArgumentTypeMismatchException e) {
        return buildErrorResponse(request, HttpStatus.BAD_REQUEST, e, "Tipo de argumento inválido", ErrorCode.MP);
    }

    /**
     * Maneja la ausencia de parámetros requeridos, devolviendo <b>400 Bad
     * Request</b>.
     *
     * @param request la solicitud HTTP
     * @param e excepción indicando el parámetro faltante
     * @return Problem Details con {@code errorCode=MP}
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ProblemDetail> handleMissingParam(HttpServletRequest request, MissingServletRequestParameterException e) {
        return buildErrorResponse(request, HttpStatus.BAD_REQUEST, e, "Parámetro requerido ausente", ErrorCode.MP);
    }

    /**
     * Maneja tipos de contenido no soportados, devolviendo <b>415 Unsupported
     * Media Type</b>.
     *
     * @param request la solicitud HTTP
     * @param e excepción indicando el tipo de contenido no soportado
     * @return Problem Details con {@code errorCode=MT}
     */
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ProblemDetail> handleMediaType(HttpServletRequest request, HttpMediaTypeNotSupportedException e) {
        return buildErrorResponse(request, HttpStatus.UNSUPPORTED_MEDIA_TYPE, e, "Tipo de contenido no soportado", ErrorCode.MT);
    }

    /**
     * Maneja métodos HTTP no permitidos para el recurso, devolviendo <b>405
     * Method Not Allowed</b>.
     *
     * @param request la solicitud HTTP
     * @param e excepción indicando el método no soportado
     * @return Problem Details con {@code errorCode=MS}
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ProblemDetail> handleMethodNotSupported(HttpServletRequest request, HttpRequestMethodNotSupportedException e) {
        return buildErrorResponse(request, HttpStatus.METHOD_NOT_ALLOWED, e, "Método HTTP no permitido", ErrorCode.MS);
    }

    /**
     * Captura de seguridad para cualquier excepción no manejada explícitamente.
     * Devuelve <b>500 Internal Server Error</b> y registra el detalle para
     * diagnóstico.
     *
     * @param request la solicitud HTTP
     * @param e la excepción no controlada
     * @return Problem Details con {@code errorCode=DC}
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ProblemDetail> handleGenericException(HttpServletRequest request, Exception e) {
        return buildErrorResponse(request, HttpStatus.INTERNAL_SERVER_ERROR, e, "Error NO manejado", ErrorCode.DC);
    }
}
