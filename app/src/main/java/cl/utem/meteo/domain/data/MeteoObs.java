package cl.utem.meteo.domain.data;

import cl.utem.meteo.domain.model.Observation;
import cl.utem.meteo.domain.model.Seba;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import java.time.OffsetDateTime;

/**
 * DTO de observación meteorológica proveniente de
 * <a href="https://redmeteo.cl">redmeteo.cl</a>.
 *
 * <p>
 * Mapea directamente las claves del JSON de origen mediante Jackson. Los campos
 * desconocidos se ignoran y los atributos con valor {@code null} no se
 * serializan.
 *
 * <h2>Convenciones y unidades</h2>
 * <ul>
 * <li><b>Temperatura</b>: °C</li>
 * <li><b>Humedad relativa</b>: % (0–100)</li>
 * <li><b>Velocidad del viento</b>: m/s o km/h (según fuente)</li>
 * <li><b>Dirección del viento</b>: grados (0–360)</li>
 * <li><b>Radiación solar</b>: W/m²</li>
 * <li><b>Presión / presión absoluta</b>: hPa</li>
 * <li><b>Precipitación</b>: mm (del intervalo)</li>
 * <li><b>Tasa de lluvia</b>: mm/h</li>
 * <li><b>Lluvia diaria</b>: mm acumulados del día</li>
 * <li><b>Fecha/hora</b>: {@link OffsetDateTime} (ISO-8601 con offset)</li>
 * </ul>
 *
 * <p>
 * <b>Nulabilidad:</b> los campos numéricos pueden ser {@code null} si la fuente
 * no entrega el dato. Se omiten en la serialización (por
 * {@link JsonInclude#NON_NULL}).</p>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(
        name = "MeteoObs",
        description = "Observación meteorológica mapeada desde redmeteo.cl (unidades en la descripción de cada campo)."
)
public class MeteoObs extends Seba {

    /**
     * Identificador propio de la observación (si lo entrega el origen).
     */
    @Schema(
            description = "Identificador propio de la observación (si lo entrega el origen).",
            example = "obs-123",
            requiredMode = RequiredMode.NOT_REQUIRED
    )
    @JsonProperty("id_observacion")
    private String idObservacion;

    /**
     * Fecha y hora de la medición (ISO-8601 con zona/offset).
     */
    @Schema(
            description = "Fecha y hora de la medición (ISO-8601 con zona/offset).",
            example = "2025-11-10T14:31:00-03:00",
            format = "date-time",
            requiredMode = RequiredMode.NOT_REQUIRED
    )
    @JsonProperty("fecha_hora")
    private OffsetDateTime fechaHora;

    /**
     * Temperatura en °C.
     */
    @Schema(
            description = "Temperatura en °C.",
            example = "18.7",
            requiredMode = RequiredMode.NOT_REQUIRED
    )
    @JsonProperty("temperatura")
    private Double temperatura;

    /**
     * Humedad relativa en % (0–100).
     */
    @Schema(
            description = "Humedad relativa en % (0–100).",
            example = "63",
            minimum = "0",
            maximum = "100",
            requiredMode = RequiredMode.NOT_REQUIRED
    )
    @JsonProperty("humedad")
    private Double humedad;

    /**
     * Velocidad del viento (m/s o km/h según fuente).
     */
    @Schema(
            description = "Velocidad del viento (m/s o km/h según fuente).",
            example = "3.2",
            minimum = "0",
            requiredMode = RequiredMode.NOT_REQUIRED
    )
    @JsonProperty("velocidad_viento")
    private Double velocidadViento;

    /**
     * Dirección del viento en grados (0–360).
     */
    @Schema(
            description = "Dirección del viento en grados (0–360).",
            example = "255",
            minimum = "0",
            maximum = "360",
            requiredMode = RequiredMode.NOT_REQUIRED
    )
    @JsonProperty("direccion_viento")
    private Long direccionViento;

    /**
     * Radiación solar en W/m².
     */
    @Schema(
            description = "Radiación solar en W/m².",
            example = "420.0",
            minimum = "0",
            requiredMode = RequiredMode.NOT_REQUIRED
    )
    @JsonProperty("radiacion_solar")
    private Double radiacionSolar;

    /**
     * Presión absoluta en hPa.
     */
    @Schema(
            description = "Presión absoluta en hPa.",
            example = "1012.3",
            minimum = "0",
            requiredMode = RequiredMode.NOT_REQUIRED
    )
    @JsonProperty("presion_absoluta")
    private Double presionAbsoluta;

    /**
     * Precipitación del período/intervalo en mm.
     */
    @Schema(
            description = "Precipitación del período/intervalo en mm.",
            example = "0.0",
            minimum = "0",
            requiredMode = RequiredMode.NOT_REQUIRED
    )
    @JsonProperty("precipitacion")
    private Double precipitacion;

    /**
     * Punto de rocío en °C.
     */
    @Schema(
            description = "Punto de rocío en °C.",
            example = "11.5",
            requiredMode = RequiredMode.NOT_REQUIRED
    )
    @JsonProperty("punto_rocio")
    private Double puntoRocio;

    /**
     * Racha máxima de viento (misma unidad que velocidad del viento).
     */
    @Schema(
            description = "Racha máxima de viento (misma unidad que velocidad del viento).",
            example = "5.8",
            minimum = "0",
            requiredMode = RequiredMode.NOT_REQUIRED
    )
    @JsonProperty("racha_viento")
    private Double rachaViento;

    /**
     * Presión en hPa (según definición de la fuente).
     */
    @Schema(
            description = "Presión en hPa (según definición de la fuente).",
            example = "1011.6",
            minimum = "0",
            requiredMode = RequiredMode.NOT_REQUIRED
    )
    @JsonProperty("presion")
    private Double presion;

    /**
     * Tasa de lluvia en mm/h. Clave principal {@code tasalluvia}; acepta alias
     * {@code tasa_lluvia}.
     */
    @Schema(
            description = "Tasa de lluvia en mm/h.",
            example = "0.0",
            minimum = "0",
            requiredMode = RequiredMode.NOT_REQUIRED
    )
    @JsonProperty("tasalluvia")
    @JsonAlias("tasa_lluvia")
    private Double tasaLluvia;

    /**
     * Índice UV (entero).
     */
    @Schema(
            description = "Índice UV (entero).",
            example = "5",
            minimum = "0",
            requiredMode = RequiredMode.NOT_REQUIRED
    )
    @JsonProperty("ultravioleta")
    private Long ultravioleta;

    /**
     * Lluvia diaria acumulada en mm. Clave principal {@code lluviadiaria};
     * alias {@code lluvia_diaria}.
     */
    @Schema(
            description = "Lluvia diaria acumulada en mm.",
            example = "0.0",
            minimum = "0",
            requiredMode = RequiredMode.NOT_REQUIRED
    )
    @JsonProperty("lluviadiaria")
    @JsonAlias("lluvia_diaria")
    private Double lluviaDiaria;

    /**
     * Constructor por defecto requerido por Jackson.
     */
    public MeteoObs() {
    }

    /**
     * Crea una instancia a partir de un {@link Observation} de dominio.
     *
     * <p>
     * Copia directa de valores sin convertir unidades. Verifica que las
     * unidades de {@code Observation} correspondan a las declaradas en esta
     * clase.</p>
     *
     * @param obs fuente de datos de observación; si es {@code null}, los campos
     * permanecen en {@code null}.
     */
    public MeteoObs(Observation obs) {
        this.idObservacion = obs.getCode();
        this.fechaHora = obs.getDate();
        this.temperatura = obs.getTemperature();
        this.humedad = obs.getHumidity();
        this.velocidadViento = obs.getWindSpeed();
        this.direccionViento = obs.getWindDirection();
        this.radiacionSolar = obs.getSolarRadiation();
        this.presionAbsoluta = obs.getAbsolutePressure();
        this.precipitacion = obs.getPrecipitation();
        this.puntoRocio = obs.getDewPoint();
        this.rachaViento = obs.getWindGust();
        this.presion = obs.getPressure();
        this.tasaLluvia = obs.getRainRate();
        this.ultravioleta = obs.getUltraviolet();
        this.lluviaDiaria = obs.getDailyRainfall();
    }

    /**
     * @return identificador propio de la observación.
     */
    public String getIdObservacion() {
        return idObservacion;
    }

    /**
     * @param idObservacion identificador propio de la observación.
     */
    public void setIdObservacion(String idObservacion) {
        this.idObservacion = idObservacion;
    }

    /**
     * @return fecha y hora de la medición.
     */
    public OffsetDateTime getFechaHora() {
        return fechaHora;
    }

    /**
     * @param fechaHora fecha y hora de la medición.
     */
    public void setFechaHora(OffsetDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }

    /**
     * @return temperatura en °C.
     */
    public Double getTemperatura() {
        return temperatura;
    }

    /**
     * @param temperatura temperatura en °C.
     */
    public void setTemperatura(Double temperatura) {
        this.temperatura = temperatura;
    }

    /**
     * @return humedad relativa en % (0–100).
     */
    public Double getHumedad() {
        return humedad;
    }

    /**
     * @param humedad humedad relativa en % (0–100).
     */
    public void setHumedad(Double humedad) {
        this.humedad = humedad;
    }

    /**
     * @return velocidad del viento.
     */
    public Double getVelocidadViento() {
        return velocidadViento;
    }

    /**
     * @param velocidadViento velocidad del viento.
     */
    public void setVelocidadViento(Double velocidadViento) {
        this.velocidadViento = velocidadViento;
    }

    /**
     * @return dirección del viento en grados (0–360).
     */
    public Long getDireccionViento() {
        return direccionViento;
    }

    /**
     * @param direccionViento dirección del viento en grados (0–360).
     */
    public void setDireccionViento(Long direccionViento) {
        this.direccionViento = direccionViento;
    }

    /**
     * @return radiación solar en W/m².
     */
    public Double getRadiacionSolar() {
        return radiacionSolar;
    }

    /**
     * @param radiacionSolar radiación solar en W/m².
     */
    public void setRadiacionSolar(Double radiacionSolar) {
        this.radiacionSolar = radiacionSolar;
    }

    /**
     * @return presión absoluta en hPa.
     */
    public Double getPresionAbsoluta() {
        return presionAbsoluta;
    }

    /**
     * @param presionAbsoluta presión absoluta en hPa.
     */
    public void setPresionAbsoluta(Double presionAbsoluta) {
        this.presionAbsoluta = presionAbsoluta;
    }

    /**
     * @return precipitación en mm (del intervalo).
     */
    public Double getPrecipitacion() {
        return precipitacion;
    }

    /**
     * @param precipitacion precipitación en mm (del intervalo).
     */
    public void setPrecipitacion(Double precipitacion) {
        this.precipitacion = precipitacion;
    }

    /**
     * @return punto de rocío en °C.
     */
    public Double getPuntoRocio() {
        return puntoRocio;
    }

    /**
     * @param puntoRocio punto de rocío en °C.
     */
    public void setPuntoRocio(Double puntoRocio) {
        this.puntoRocio = puntoRocio;
    }

    /**
     * @return racha máxima de viento.
     */
    public Double getRachaViento() {
        return rachaViento;
    }

    /**
     * @param rachaViento racha máxima de viento.
     */
    public void setRachaViento(Double rachaViento) {
        this.rachaViento = rachaViento;
    }

    /**
     * @return presión en hPa.
     */
    public Double getPresion() {
        return presion;
    }

    /**
     * @param presion presión en hPa.
     */
    public void setPresion(Double presion) {
        this.presion = presion;
    }

    /**
     * @return tasa de lluvia en mm/h.
     */
    public Double getTasaLluvia() {
        return tasaLluvia;
    }

    /**
     * @param tasaLluvia tasa de lluvia en mm/h.
     */
    public void setTasaLluvia(Double tasaLluvia) {
        this.tasaLluvia = tasaLluvia;
    }

    /**
     * @return índice UV (entero).
     */
    public Long getUltravioleta() {
        return ultravioleta;
    }

    /**
     * @param ultravioleta índice UV (entero).
     */
    public void setUltravioleta(Long ultravioleta) {
        this.ultravioleta = ultravioleta;
    }

    /**
     * @return lluvia diaria acumulada en mm.
     */
    public Double getLluviaDiaria() {
        return lluviaDiaria;
    }

    /**
     * @param lluviaDiaria lluvia diaria acumulada en mm.
     */
    public void setLluviaDiaria(Double lluviaDiaria) {
        this.lluviaDiaria = lluviaDiaria;
    }
}
