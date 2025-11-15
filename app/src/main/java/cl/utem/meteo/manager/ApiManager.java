package cl.utem.meteo.manager;

import cl.utem.meteo.domain.model.Credential;
import cl.utem.meteo.domain.repository.CredentialRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ApiManager {

    private final CredentialRepository credentialRepository;

    @Autowired
    public ApiManager(CredentialRepository credentialRepository) {
        this.credentialRepository = credentialRepository;
    }

    public Credential getCredential(final String username, final String password) {
        Credential credential = null;
        if (!StringUtils.isAnyBlank(username, password)) {
            // hashear 256 password + PIMIENTA
            
            credential = credentialRepository.findByUsernameIgnoreCaseAndPassword(username, password);
        }
        return credential;
    }
}
