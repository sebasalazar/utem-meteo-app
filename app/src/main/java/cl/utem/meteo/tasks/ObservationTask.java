package cl.utem.meteo.tasks;

import cl.utem.meteo.domain.data.RedMeteo;
import cl.utem.meteo.domain.model.Station;
import cl.utem.meteo.domain.repository.ObservationRepository;
import cl.utem.meteo.domain.repository.StationRepository;
import cl.utem.meteo.utils.JsonUtils;
import java.net.URI;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class ObservationTask {

    private final StationRepository stationRepository;
    private final ObservationRepository observationRepository;

    private static final Logger LOGGER = LoggerFactory.getLogger(ObservationTask.class);

    @Autowired
    public ObservationTask(StationRepository stationRepository, ObservationRepository observationRepository) {
        this.stationRepository = stationRepository;
        this.observationRepository = observationRepository;
    }

    private void saveObs(final RedMeteo rm) {
        if (rm != null) {
            Station station = stationRepository.findByCodeIgnoreCase(rm.getIDEstacion());
            if (station == null) {
                Station st = new Station();
                st.setActive(true);
                st.setAltitude((int) rm.getAltitud());
                st.setCode(rm.getIDEstacion());
                st.setLatitude(rm.getLatitud());
                st.setLongitude(rm.getLongitud());
                st.setName(rm.getNombre());
                station = stationRepository.save(st);
                LOGGER.info("Se guardó la estación {}", station.getId());
            }
        }
    }

    private void query() {

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity(URI.create("https://redmeteo.cl/last-data.json"), String.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            String json = response.getBody();
            List<RedMeteo> list = JsonUtils.convertRedMeteo(json);
            list.parallelStream().forEach(this::saveObs);
        }
    }

    @Scheduled(fixedDelayString = "PT1M")
    public void runScheduled() {
        LOGGER.info("Ejecutando actualización");
        query();
    }
}
