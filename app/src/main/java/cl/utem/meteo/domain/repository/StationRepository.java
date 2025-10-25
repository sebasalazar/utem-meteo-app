package cl.utem.meteo.domain.repository;

import cl.utem.meteo.domain.model.Station;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StationRepository 
        extends JpaRepository<Station, Long>{
    
    Station findByCodeIgnoreCase(String code);
}
