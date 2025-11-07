package cl.utem.meteo.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serial;
import java.io.Serializable;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * Base simple para entidades del sistema.
 * <p>
 * Implementa {@link Serializable} y redefine {@link #toString()} usando
 * {@link ReflectionToStringBuilder} con {@link ToStringStyle#JSON_STYLE} para
 * obtener una representación legible en formato JSON.
 * </p>
 *
 * <h2>Advertencias de uso</h2>
 * <ul>
 * <li><b>Rendimiento:</b> la reflexión tiene un costo pero es cómodo.</li>
 * <li><b>Seguridad:</b> la salida puede incluir campos sensibles (tokens,
 * claves, PII). Para excluir hay que marcarlos con {@code transient}</li>
 * <li><b>Bucles/ciclos:</b> si hay referencias cíclicas entre objetos, las
 * salidas son muy largas.</li>
 * </ul>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Seba implements Serializable {

    /**
     * El tiempo de ejecución de serialización asocia con cada clase
     * serializable un número de versión, llamado serialVersionUID, que se usa
     * durante la deserialización para verificar que el remitente y el receptor
     * de un objeto serializado hayan cargado clases para ese objeto que sean
     * compatibles con respecto a la serialización. Si el receptor ha cargado
     * una clase para el objeto que tiene un serialVersionUID diferente al de la
     * clase del remitente correspondiente, entonces la deserialización
     * resultará en una InvalidClassException.
     *
     * Una clase serializable declara su propio serialVersionUID como un campo
     * serialVersionUID que DEBE ser static, final, y de tipo long. Sino se
     * declara la JVM lo calcula en tiempo de ejecución.
     */
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Representación tipo JSON de la instancia usando reflexión.
     * <p>
     * Útil para logs, trazas y depuración. No se recomienda para exposición
     * pública sin filtrar/mascarar datos sensibles.
     * </p>
     *
     * @return cadena en estilo JSON con los campos de la instancia.
     */
    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.JSON_STYLE);
    }
}
