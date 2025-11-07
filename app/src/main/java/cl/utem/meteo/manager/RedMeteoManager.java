package cl.utem.meteo.manager;

import cl.utem.meteo.domain.data.RedMeteo;
import cl.utem.meteo.domain.model.Observation;
import cl.utem.meteo.domain.model.Station;
import cl.utem.meteo.domain.repository.ObservationRepository;
import cl.utem.meteo.domain.repository.StationRepository;
import cl.utem.meteo.utils.RmUtils;
import cl.utem.meteo.utils.TextUtils;
import java.net.URI;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Servicio de integración con RedMeteo: transforma y persiste observaciones.
 * <p>
 * <ol>
 * <li><b>Station</b>: get-or-create por {@code idEstacion}
 * (case-insensitive).</li>
 * <li><b>Observation</b>: inserta solo si <b>NO</b> existe por (station,
 * code).</li>
 * </ol>
 * </p>
 */
@Service
public class RedMeteoManager {

    private final StationRepository stationRepository;
    private final ObservationRepository observationRepository;
    /**
     * Endpoint público con los últimos datos.
     */
    private static final URI RED_METEO_URI = URI.create("https://redmeteo.cl/last-data.json");
    private static final Logger LOGGER = LoggerFactory.getLogger(RedMeteoManager.class);

    @Autowired
    public RedMeteoManager(StationRepository stationRepository, ObservationRepository observationRepository) {
        this.stationRepository = stationRepository;
        this.observationRepository = observationRepository;
    }

    /**
     * URI del recurso remoto RedMeteo (últimos datos).
     *
     * @return {@link URI} del endpoint
     */
    public URI getRedMeteoUri() {
        return RED_METEO_URI;
    }

    /**
     * Persiste una observación:
     * <ul>
     * <li>Crea {@link Station} si no existe (get-or-create).</li>
     * <li>Normaliza el código de observación a mayúsculas.</li>
     * <li>Inserta {@link Observation} <b>solo si no existe</b> por (station,
     * code).</li>
     * </ul>
     *
     * @param rm DTO de RedMeteo; si es {@code null} se ignora.
     */
    @Transactional
    public void saveObs(final RedMeteo rm) {
        if (rm != null) {
            Station station = stationRepository.findByCodeIgnoreCase(rm.getIdEstacion());
            if (station == null) {
                Station st = new Station();
                st.setActive(true);
                st.setAltitude(rm.getAltitud());
                st.setCode(rm.getIdEstacion());
                st.setLatitude(rm.getLatitud());
                st.setLongitude(rm.getLongitud());
                st.setName(rm.getNombre());
                station = stationRepository.save(st);
            }

            final String code = TextUtils.upper(rm.getIdObservacion());
            if (StringUtils.isNotBlank(code)) {
                Observation obs = observationRepository.findByStationAndCodeIgnoreCase(station, code);
                if (obs == null) {
                    obs = new Observation();
                    obs.setCode(code);
                    obs.setAbsolutePressure(RmUtils.getValue(rm.getPresionAbsoluta()));
                    obs.setDailyRainfall(RmUtils.getValue(rm.getLluviaDiaria()));
                    obs.setDate(rm.getFechaHora());
                    obs.setDewPoint(RmUtils.getValue(rm.getPuntoRocio()));
                    obs.setHumidity(RmUtils.getValue(rm.getHumedad()));
                    obs.setPrecipitation(RmUtils.getValue(rm.getPrecipitacion()));
                    obs.setPressure(RmUtils.getValue(rm.getPresion()));
                    obs.setRainRate(RmUtils.getValue(rm.getTasaLluvia()));
                    obs.setSolarRadiation(RmUtils.getValue(rm.getRadiacionSolar()));
                    obs.setStation(station);
                    obs.setTemperature(RmUtils.getValue(rm.getTemperatura()));
                    obs.setUltraviolet(RmUtils.getValue(rm.getUltravioleta()));
                    obs.setWindDirection(RmUtils.getValue(rm.getDireccionViento()));
                    obs.setWindGust(RmUtils.getValue(rm.getRachaViento()));
                    obs.setWindSpeed(RmUtils.getValue(rm.getVelocidadViento()));
                    Observation savedObs = observationRepository.save(obs);
                    LOGGER.info("Observación creada: id={}, station={}, code={}, date={}",
                            savedObs.getId(), station.getCode(), code, savedObs.getDate());
                }
            } else {
                // Un solo log a ERROR; si deseas stacktrace, llévalo a DEBUG en la capa que capture la excepción.
                LOGGER.error("No hay código de validación para la observación. Payload: {}", rm);
            }
        }
    }
}
