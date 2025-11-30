package cl.utem.meteo.manager.helper;

import cl.utem.meteo.domain.model.Observation;
import cl.utem.meteo.domain.model.Station;
import cl.utem.meteo.domain.repository.ObservationRepository;
import cl.utem.meteo.domain.repository.StationRepository;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class CacheHelper {

    private final StationRepository stationRepository;
    private final ObservationRepository observationRepository;

    private static final String METEO_CACHE_NAME = "meteoCache";

    private static final Logger LOGGER = LoggerFactory.getLogger(CacheHelper.class);

    @Autowired
    public CacheHelper(StationRepository stationRepository, ObservationRepository observationRepository) {
        this.stationRepository = stationRepository;
        this.observationRepository = observationRepository;
    }

    @CacheEvict(cacheNames = METEO_CACHE_NAME, allEntries = true, beforeInvocation = false)
    public void evictAllCacheEntries() {
        LOGGER.info("Limpiando caché {} (todas las entradas)", METEO_CACHE_NAME);
    }

    @Cacheable(
            cacheNames = METEO_CACHE_NAME,
            key = "'getStation:code:' + #code",
            sync = true
    )
    public Station getStation(final String code) {
        return stationRepository.findByCodeIgnoreCase(code);
    }
    
    @Cacheable(
            cacheNames = METEO_CACHE_NAME,
            key = "'getObservations:station:' + #station.code",
            sync = true
    )
    public List<Observation> getObservations(final Station station) {
        if (station == null) {
            return List.of();
        }

        return observationRepository.findByStation(station);
    }
}
