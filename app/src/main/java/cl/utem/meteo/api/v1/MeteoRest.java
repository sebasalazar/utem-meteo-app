package cl.utem.meteo.api.v1;

import cl.utem.meteo.domain.data.MeteoObs;
import cl.utem.meteo.domain.model.Observation;
import cl.utem.meteo.domain.model.Station;
import cl.utem.meteo.exception.NoDataException;
import cl.utem.meteo.exception.ValidationException;
import cl.utem.meteo.manager.RedMeteoManager;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/observations")
public class MeteoRest {

    private final RedMeteoManager redMeteoManager;
    private static Logger LOGGER = LoggerFactory.getLogger(MeteoRest.class);

    @Autowired
    public MeteoRest(RedMeteoManager redMeteoManager) {
        this.redMeteoManager = redMeteoManager;
    }

    @GetMapping(value = {"/{station}"},
            consumes = MediaType.ALL_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<MeteoObs>> getMeteo(@PathVariable("station") String stationCode) {
        if (StringUtils.isEmpty(stationCode)) {
            throw new ValidationException("Se necesita un código de estación válido");
        }

        if (StringUtils.length(stationCode) != 8) {
            throw new ValidationException("El largo del código es inválido");
        }

        if (!StringUtils.startsWith(stationCode, "RMCL")) {
            throw new ValidationException("Se necesita un código válido");
        }

        final Station station = redMeteoManager.getStation(stationCode);
        if (station == null) {
            throw new NoDataException(String.format("No se encontró la estación: %s", stationCode));
        }

        List<Observation> observations = redMeteoManager.getObservations(station);
        if (observations == null || observations.isEmpty()) {
            throw new NoDataException("No se encontraron observaciones");
        }

        List<MeteoObs> list = new ArrayList<>();
        for (Observation current : observations) {
            list.add(new MeteoObs(current));
        }

        return ResponseEntity.ok(list);
    }
}
