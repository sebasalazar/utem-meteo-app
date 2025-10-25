package cl.utem.meteo.domain.repository;

import cl.utem.meteo.domain.model.Observation;
import cl.utem.meteo.domain.model.Station;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ObservationRepository extends JpaRepository<Observation, Long> {

    Observation findByStationAndCodeIgnoreCase(Station station, String code);
}
