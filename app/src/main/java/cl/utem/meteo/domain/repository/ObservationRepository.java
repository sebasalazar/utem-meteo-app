package cl.utem.meteo.domain.repository;

import cl.utem.meteo.domain.model.Observation;
import cl.utem.meteo.domain.model.Station;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio JPA para {@link Observation}.
 * <p>
 * Provee operaciones CRUD estándar y consultas derivadas por convención de
 * nombres.
 * </p>
 *
 * <h2>Notas</h2>
 * <ul>
 * <li>La PK del agregado es {@code Long} (heredada de
 * {@code PkEntityBase}).</li>
 * <li>Para búsquedas frecuentes conviene indexar columnas como
 * {@code station_fk}, {@code code} (y opcionalmente {@code date_time}).</li>
 * </ul>
 */
@Repository
public interface ObservationRepository extends JpaRepository<Observation, Long> {

    /**
     * Busca una observación por estación y código, ignorando
     * mayúsculas/minúsculas en el código.
     * <p>
     * Esta consulta se deriva del nombre del método
     * ({@code findByStationAndCodeIgnoreCase}). Si no existe coincidencia,
     * Spring Data retornará {@code null}.
     * </p>
     *
     *
     * @param station instancia de {@link Station} asociada a la observación (no
     * nula)
     * @param code código de la variable/observación (no nulo/ni en blanco
     * cuando es válido)
     * @return la observación encontrada o {@code null} si no existe
     */
    Observation findByStationAndCodeIgnoreCase(Station station, String code);

    List<Observation> findByStation(Station station);
}
