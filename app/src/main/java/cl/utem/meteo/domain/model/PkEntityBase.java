package cl.utem.meteo.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import java.time.OffsetDateTime;
import java.util.Objects;

/**
 * Superclase mapeada para entidades con PK autoincremental y trazabilidad
 * básica.
 * <ul>
 * <li>PK Long con {@code GenerationType.IDENTITY}.</li>
 * <li>Campos {@code created}/{@code updated} con zona horaria.</li>
 * <li>{@code equals/hashCode} seguros para colecciones: consistentes
 * antes/después de persistir.</li>
 * </ul>
 *
 * <h2>Notas de diseño</h2>
 * <ul>
 * <li><b>Timestamps:</b> se fijan en {@code @PrePersist/@PreUpdate} (evita
 * olvidar setearlos).</li>
 * <li><b>equals/hashCode:</b> si la entidad no tiene id aún, se usa identidad
 * de objeto (referencia); una vez asignado el id, la igualdad se basa solo en
 * el id.</li>
 * </ul>
 */
@MappedSuperclass
public class PkEntityBase extends Seba {

    /**
     * Llave primaria Autoincremental
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pk", nullable = false)
    private Long id = null;

    /**
     * Fecha de creación del objeto
     */
    @Column(name = "created", nullable = false, updatable = false)
    protected OffsetDateTime created = OffsetDateTime.now();

    /**
     * Fecha de actualización de la fecha
     */
    @Column(name = "updated", nullable = false)
    protected OffsetDateTime updated = OffsetDateTime.now();

    /**
     * Callback JPA: setea created/updated al insertar.
     */
    @PrePersist
    protected void onPrePersist() {
        OffsetDateTime now = OffsetDateTime.now();
        this.created = now;
        this.updated = now;
    }

    /**
     * Callback JPA: refresca updated al actualizar.
     */
    @PreUpdate
    protected void onPreUpdate() {
        this.updated = OffsetDateTime.now();
    }

    /**
     *
     * @return El identificador generado por el motor de base de datos.
     */
    public Long getId() {
        return id;
    }

    /**
     *
     * @param id Fuerza un identificador, no necesariamente se podrá persistir
     * dependerá del mecanismo interno.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     *
     * @return Fecha de creación
     */
    public OffsetDateTime getCreated() {
        return created;
    }

    /**
     *
     * @param created Fecha de creación
     */
    public void setCreated(OffsetDateTime created) {
        this.created = created;
    }

    /**
     *
     * @return Fecha de última actualización
     */
    public OffsetDateTime getUpdated() {
        return updated;
    }

    /**
     *
     * @param updated Fecha de última actualización
     */
    public void setUpdated(OffsetDateTime updated) {
        this.updated = updated;
    }

    /**
     * Contrato de igualdad:
     * <ul>
     * <li>Si ambos tienen id no nulo: compara por id.</li>
     * <li>Si al menos uno no tiene id: cae a igualdad por identidad (this ==
     * obj).</li>
     * </ul>
     * Esto evita romper sets/mapas antes de persistir.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        // misma jerarquía concreta
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        PkEntityBase other = (PkEntityBase) obj;

        // si ambos tienen id, compara por id
        if (this.id != null && other.id != null) {
            return Objects.equals(this.id, other.id);
        }
        // al menos uno no tiene id -> no considerarlos "iguales" salvo que sea el mismo objeto
        return false;
    }

    /**
     * Contrato de hash:
     * <ul>
     * <li>Con id no nulo: hash basado en id (estable para colecciones).</li>
     * <li>Sin id: hash de clase (evita cambios al persistir; acepta más
     * colisiones pero es seguro).</li>
     * </ul>
     */
    @Override
    public int hashCode() {
        return (id != null) ? (getClass().hashCode() * 31 + id.hashCode()) : getClass().hashCode();
    }
}
