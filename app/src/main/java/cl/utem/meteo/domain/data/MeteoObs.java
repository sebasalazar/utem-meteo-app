package cl.utem.meteo.domain.data;

import cl.utem.meteo.domain.model.Observation;
import cl.utem.meteo.domain.model.Seba;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.OffsetDateTime;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 *
 * Objeto json de https://redmeteo.cl
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MeteoObs extends Seba {

    /**
     * Identificador propio de la observación (si lo entrega el origen).
     */
    @JsonProperty("id_observacion")
    private String idObservacion;

    /**
     * Fecha y hora de la medición.
     */
    @JsonProperty("fecha_hora")
    private OffsetDateTime fechaHora;

    /**
     * Temperatura (°C).
     */
    @JsonProperty("temperatura")
    private Double temperatura;

    /**
     * Humedad relativa (%).
     */
    @JsonProperty("humedad")
    private Double humedad;

    /**
     * Velocidad del viento (m/s o km/h).
     */
    @JsonProperty("velocidad_viento")
    private Double velocidadViento;

    /**
     * Dirección del viento (grados 0–360).
     */
    @JsonProperty("direccion_viento")
    private Long direccionViento;

    /**
     * Radiación solar (W/m²).
     */
    @JsonProperty("radiacion_solar")
    private Double radiacionSolar;

    /**
     * Presión absoluta (hPa).
     */
    @JsonProperty("presion_absoluta")
    private Double presionAbsoluta;

    /**
     * Precipitación del período/intervalo (mm).
     */
    @JsonProperty("precipitacion")
    private Double precipitacion;

    /**
     * Punto de rocío (°C).
     */
    @JsonProperty("punto_rocio")
    private Double puntoRocio;

    /**
     * Racha de viento (misma unidad que velocidad).
     */
    @JsonProperty("racha_viento")
    private Double rachaViento;

    /**
     * Presión (hPa) – según definición de la fuente.
     */
    @JsonProperty("presion")
    private Double presion;

    /**
     * Tasa de lluvia (mm/h). Clave principal 'tasalluvia'; acepta alias
     * 'tasa_lluvia'.
     */
    @JsonProperty("tasalluvia")
    @JsonAlias("tasa_lluvia")
    private Double tasaLluvia;

    /**
     * Índice UV (entero).
     */
    @JsonProperty("ultravioleta")
    private Long ultravioleta;

    /**
     * Lluvia diaria acumulada (mm). Clave principal 'lluviadiaria'; acepta
     * alias 'lluvia_diaria'.
     */
    @JsonProperty("lluviadiaria")
    @JsonAlias("lluvia_diaria")
    private Double lluviaDiaria;

    public MeteoObs() {
    }

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

    public String getIdObservacion() {
        return idObservacion;
    }

    public void setIdObservacion(String idObservacion) {
        this.idObservacion = idObservacion;
    }

    public OffsetDateTime getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(OffsetDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }

    public Double getTemperatura() {
        return temperatura;
    }

    public void setTemperatura(Double temperatura) {
        this.temperatura = temperatura;
    }

    public Double getHumedad() {
        return humedad;
    }

    public void setHumedad(Double humedad) {
        this.humedad = humedad;
    }

    public Double getVelocidadViento() {
        return velocidadViento;
    }

    public void setVelocidadViento(Double velocidadViento) {
        this.velocidadViento = velocidadViento;
    }

    public Long getDireccionViento() {
        return direccionViento;
    }

    public void setDireccionViento(Long direccionViento) {
        this.direccionViento = direccionViento;
    }

    public Double getRadiacionSolar() {
        return radiacionSolar;
    }

    public void setRadiacionSolar(Double radiacionSolar) {
        this.radiacionSolar = radiacionSolar;
    }

    public Double getPresionAbsoluta() {
        return presionAbsoluta;
    }

    public void setPresionAbsoluta(Double presionAbsoluta) {
        this.presionAbsoluta = presionAbsoluta;
    }

    public Double getPrecipitacion() {
        return precipitacion;
    }

    public void setPrecipitacion(Double precipitacion) {
        this.precipitacion = precipitacion;
    }

    public Double getPuntoRocio() {
        return puntoRocio;
    }

    public void setPuntoRocio(Double puntoRocio) {
        this.puntoRocio = puntoRocio;
    }

    public Double getRachaViento() {
        return rachaViento;
    }

    public void setRachaViento(Double rachaViento) {
        this.rachaViento = rachaViento;
    }

    public Double getPresion() {
        return presion;
    }

    public void setPresion(Double presion) {
        this.presion = presion;
    }

    public Double getTasaLluvia() {
        return tasaLluvia;
    }

    public void setTasaLluvia(Double tasaLluvia) {
        this.tasaLluvia = tasaLluvia;
    }

    public Long getUltravioleta() {
        return ultravioleta;
    }

    public void setUltravioleta(Long ultravioleta) {
        this.ultravioleta = ultravioleta;
    }

    public Double getLluviaDiaria() {
        return lluviaDiaria;
    }

    public void setLluviaDiaria(Double lluviaDiaria) {
        this.lluviaDiaria = lluviaDiaria;
    }
}
