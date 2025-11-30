package cl.utem.meteo.api.v1;

import cl.utem.meteo.domain.data.out.MeteoObs;
import cl.utem.meteo.domain.model.Station;
import cl.utem.meteo.exception.NoDataException;
import cl.utem.meteo.manager.RedMeteoManager;
import cl.utem.meteo.utils.PageUtils;
import cl.utem.meteo.utils.RmUtils;
import cl.utem.meteo.utils.TextUtils;
import cl.utem.meteo.utils.ValidationUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "observations")
@RestController
@RequestMapping(value = "/v1/observations", produces = MediaType.APPLICATION_JSON_VALUE)
public class MeteoRest {

    private final RedMeteoManager redMeteoManager;
    private static final Logger LOGGER = LoggerFactory.getLogger(MeteoRest.class);

    @Autowired
    public MeteoRest(RedMeteoManager redMeteoManager) {
        this.redMeteoManager = redMeteoManager;
    }

    @Operation(summary = "Listar observaciones por estación")
    @GetMapping(value = {"/{station}"}, consumes = MediaType.ALL_VALUE)
    public ResponseEntity<List<MeteoObs>> getMeteo(HttpServletRequest request,
            @Parameter(description = "Código de estación (p.ej., RMCL1234)", required = true)
            @PathVariable("station") String stationCode) {
        // Validamos el código de estación
        ValidationUtils.validate(stationCode, TextUtils.escapeForLog(stationCode));

        final Station station = redMeteoManager.getStation(TextUtils.upper(stationCode));
        if (station == null) {
            throw new NoDataException(String.format("No se encontró la estación: %s", stationCode));
        }

        List<MeteoObs> observations = RmUtils.buildObservations(redMeteoManager.getObservations(station));
        if (observations == null || observations.isEmpty()) {
            throw new NoDataException("No se encontraron observaciones");
        }

        return ResponseEntity.ok(observations);
    }

    @GetMapping(value = {"/"}, consumes = MediaType.ALL_VALUE)
    public ResponseEntity<List<MeteoObs>> getMeteo(HttpServletRequest request,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            @RequestParam(name = "sort", defaultValue = "created") String sort,
            @RequestParam(name = "direction", defaultValue = "ASC") String direction) {

        LOGGER.debug("Opciones de búsqueda: Página {} Tamaño de página {} Campo ordenamiento: {} Dirección ordenamiento: {}", page, size, sort, direction);

        final Sort.Direction sortDirection = "DESC".equals(direction) ? Sort.Direction.DESC : Sort.Direction.ASC;
        final Sort sortBy = Sort.by(sortDirection, sort);
        final Pageable pageable = PageUtils.getPageable(page, size, sortBy);

        List<MeteoObs> observations = RmUtils.buildObservations(redMeteoManager.getObservations(pageable));
        if (observations == null || observations.isEmpty()) {
            throw new NoDataException("No se encontraron observaciones");
        }
        return ResponseEntity.ok(observations);
    }
}
