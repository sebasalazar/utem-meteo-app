package cl.utem.meteo.domain.repository;

import cl.utem.meteo.domain.model.Credential;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CredentialRepository extends JpaRepository<Credential, Long> {

    public Credential findByToken(String token);

    public Credential findByUsernameIgnoreCaseAndPassword(String username, String password);
}
