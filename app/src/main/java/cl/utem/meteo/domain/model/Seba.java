package cl.utem.meteo.domain.model;

import java.io.Serializable;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 *
 * Objeto BaseBean para las entidades del sistema
 */
public class Seba implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     *
     * @return Una versión que genera una string en formato json.
     */
    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.JSON_STYLE);
    }
}
