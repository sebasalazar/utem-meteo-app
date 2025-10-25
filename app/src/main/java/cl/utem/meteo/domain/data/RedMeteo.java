package cl.utem.meteo.domain.data;

import cl.utem.meteo.domain.model.Seba;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.OffsetDateTime;

public class RedMeteo extends Seba {

    private String idEstacion;
    private String nombre;
    private double latitud;
    private double longitud;
    private long altitud;
    private long idObservacion;
    private OffsetDateTime fechaHora;
    private Double temperatura;
    private Double humedad;
    private Double velocidadViento;
    private Long direccionViento;
    private Double radiacionSolar;
    private Double presionAbsoluta;
    private Double precipitacion;
    private Double puntoRocio;
    private Double rachaViento;
    private Double presion;
    private Double tasalluvia;
    private Long ultravioleta;
    private Double lluviadiaria;

    @JsonProperty("id_estacion")
    public String getIDEstacion() {
        return idEstacion;
    }

    @JsonProperty("id_estacion")
    public void setIDEstacion(String value) {
        this.idEstacion = value;
    }

    @JsonProperty("nombre")
    public String getNombre() {
        return nombre;
    }

    @JsonProperty("nombre")
    public void setNombre(String value) {
        this.nombre = value;
    }

    @JsonProperty("latitud")
    public double getLatitud() {
        return latitud;
    }

    @JsonProperty("latitud")
    public void setLatitud(double value) {
        this.latitud = value;
    }

    @JsonProperty("longitud")
    public double getLongitud() {
        return longitud;
    }

    @JsonProperty("longitud")
    public void setLongitud(double value) {
        this.longitud = value;
    }

    @JsonProperty("altitud")
    public long getAltitud() {
        return altitud;
    }

    @JsonProperty("altitud")
    public void setAltitud(long value) {
        this.altitud = value;
    }

    @JsonProperty("id_observacion")
    public long getIDObservacion() {
        return idObservacion;
    }

    @JsonProperty("id_observacion")
    public void setIDObservacion(long value) {
        this.idObservacion = value;
    }

    @JsonProperty("fecha_hora")
    public OffsetDateTime getFechaHora() {
        return fechaHora;
    }

    @JsonProperty("fecha_hora")
    public void setFechaHora(OffsetDateTime value) {
        this.fechaHora = value;
    }

    @JsonProperty("temperatura")
    public Double getTemperatura() {
        return temperatura;
    }

    @JsonProperty("temperatura")
    public void setTemperatura(Double value) {
        this.temperatura = value;
    }

    @JsonProperty("humedad")
    public Double getHumedad() {
        return humedad;
    }

    @JsonProperty("humedad")
    public void setHumedad(Double value) {
        this.humedad = value;
    }

    @JsonProperty("velocidad_viento")
    public Double getVelocidadViento() {
        return velocidadViento;
    }

    @JsonProperty("velocidad_viento")
    public void setVelocidadViento(Double value) {
        this.velocidadViento = value;
    }

    @JsonProperty("direccion_viento")
    public Long getDireccionViento() {
        return direccionViento;
    }

    @JsonProperty("direccion_viento")
    public void setDireccionViento(Long value) {
        this.direccionViento = value;
    }

    @JsonProperty("radiacion_solar")
    public Double getRadiacionSolar() {
        return radiacionSolar;
    }

    @JsonProperty("radiacion_solar")
    public void setRadiacionSolar(Double value) {
        this.radiacionSolar = value;
    }

    @JsonProperty("presion_absoluta")
    public Double getPresionAbsoluta() {
        return presionAbsoluta;
    }

    @JsonProperty("presion_absoluta")
    public void setPresionAbsoluta(Double value) {
        this.presionAbsoluta = value;
    }

    @JsonProperty("precipitacion")
    public Double getPrecipitacion() {
        return precipitacion;
    }

    @JsonProperty("precipitacion")
    public void setPrecipitacion(Double value) {
        this.precipitacion = value;
    }

    @JsonProperty("punto_rocio")
    public Double getPuntoRocio() {
        return puntoRocio;
    }

    @JsonProperty("punto_rocio")
    public void setPuntoRocio(Double value) {
        this.puntoRocio = value;
    }

    @JsonProperty("racha_viento")
    public Double getRachaViento() {
        return rachaViento;
    }

    @JsonProperty("racha_viento")
    public void setRachaViento(Double value) {
        this.rachaViento = value;
    }

    @JsonProperty("presion")
    public Double getPresion() {
        return presion;
    }

    @JsonProperty("presion")
    public void setPresion(Double value) {
        this.presion = value;
    }

    @JsonProperty("tasalluvia")
    public Double getTasalluvia() {
        return tasalluvia;
    }

    @JsonProperty("tasalluvia")
    public void setTasalluvia(Double value) {
        this.tasalluvia = value;
    }

    @JsonProperty("ultravioleta")
    public Long getUltravioleta() {
        return ultravioleta;
    }

    @JsonProperty("ultravioleta")
    public void setUltravioleta(Long value) {
        this.ultravioleta = value;
    }

    @JsonProperty("lluviadiaria")
    public Double getLluviadiaria() {
        return lluviadiaria;
    }

    @JsonProperty("lluviadiaria")
    public void setLluviadiaria(Double value) {
        this.lluviadiaria = value;
    }
}
