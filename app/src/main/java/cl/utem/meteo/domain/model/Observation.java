package cl.utem.meteo.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.OffsetDateTime;

@Entity
@Table(name = "observations")
public class Observation extends PkEntityBase {

    @ManyToOne(fetch = FetchType.EAGER, targetEntity = Station.class)
    @JoinColumn(name = "station_fk", nullable = false)
    private Station station = null;

    @Column(name = "code", nullable = false)
    private String code = null;

    @Column(name = "date_time", nullable = false)
    private OffsetDateTime date = null;

    @Column(name = "temperature", nullable = false)
    private Double temperature = null;

    @Column(name = "humidity", nullable = false)
    private Double humidity = null;

    @Column(name = "wind_speed", nullable = false)
    private Double windSpeed = null;

    @Column(name = "wind_direction", nullable = false)
    private Double windDirection = null;

    @Column(name = "solar_radiation", nullable = false)
    private Double solarRadiation = null;

    @Column(name = "absolute_pressure", nullable = false)
    private Double absolutePressure = null;

    @Column(name = "precipitation", nullable = false)
    private Double precipitation = null;

    @Column(name = "dew_point", nullable = false)
    private Double dewPoint = null;

    @Column(name = "wind_gust", nullable = false)
    private Double windGust = null;

    @Column(name = "pressure", nullable = false)
    private Double pressure = null;

    @Column(name = "rain_rate", nullable = false)
    private Double rainRate = null;

    @Column(name = "ultraviolet", nullable = false)
    private Double ultraviolet = null;

    @Column(name = "daily_rainfall", nullable = false)
    private Double dailyRainfall = null;

    public Station getStation() {
        return station;
    }

    public void setStation(Station station) {
        this.station = station;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public OffsetDateTime getDate() {
        return date;
    }

    public void setDate(OffsetDateTime date) {
        this.date = date;
    }

    public Double getTemperature() {
        return temperature;
    }

    public void setTemperature(Double temperature) {
        this.temperature = temperature;
    }

    public Double getHumidity() {
        return humidity;
    }

    public void setHumidity(Double humidity) {
        this.humidity = humidity;
    }

    public Double getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(Double windSpeed) {
        this.windSpeed = windSpeed;
    }

    public Double getWindDirection() {
        return windDirection;
    }

    public void setWindDirection(Double windDirection) {
        this.windDirection = windDirection;
    }

    public Double getSolarRadiation() {
        return solarRadiation;
    }

    public void setSolarRadiation(Double solarRadiation) {
        this.solarRadiation = solarRadiation;
    }

    public Double getAbsolutePressure() {
        return absolutePressure;
    }

    public void setAbsolutePressure(Double absolutePressure) {
        this.absolutePressure = absolutePressure;
    }

    public Double getPrecipitation() {
        return precipitation;
    }

    public void setPrecipitation(Double precipitation) {
        this.precipitation = precipitation;
    }

    public Double getDewPoint() {
        return dewPoint;
    }

    public void setDewPoint(Double dewPoint) {
        this.dewPoint = dewPoint;
    }

    public Double getWindGust() {
        return windGust;
    }

    public void setWindGust(Double windGust) {
        this.windGust = windGust;
    }

    public Double getPressure() {
        return pressure;
    }

    public void setPressure(Double pressure) {
        this.pressure = pressure;
    }

    public Double getRainRate() {
        return rainRate;
    }

    public void setRainRate(Double rainRate) {
        this.rainRate = rainRate;
    }

    public Double getUltraviolet() {
        return ultraviolet;
    }

    public void setUltraviolet(Double ultraviolet) {
        this.ultraviolet = ultraviolet;
    }

    public Double getDailyRainfall() {
        return dailyRainfall;
    }

    public void setDailyRainfall(Double dailyRainfall) {
        this.dailyRainfall = dailyRainfall;
    }

}
