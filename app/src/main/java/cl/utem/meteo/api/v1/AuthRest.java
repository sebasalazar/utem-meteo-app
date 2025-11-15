package cl.utem.meteo.api.v1;

import cl.utem.meteo.domain.data.in.LoginVO;
import cl.utem.meteo.domain.enums.Profile;
import cl.utem.meteo.domain.model.Credential;
import cl.utem.meteo.exception.AuthException;
import cl.utem.meteo.manager.ApiManager;
import cl.utem.meteo.utils.JwtUtils;
import cl.utem.meteo.utils.TextUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(name = "/v1/auth")
public class AuthRest {

    @Value("${jwt.sign}")
    private String jwtSign;

    private final ApiManager apiManager;

    @Autowired
    public AuthRest(ApiManager apiManager) {
        this.apiManager = apiManager;
    }

    @PostMapping(value = "/login",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity login(@RequestBody LoginVO body) {
        if (body == null) {
            throw new AuthException();
        }

        final String username = TextUtils.upper(body.getUsername());
        if (StringUtils.isBlank(username)) {
            throw new AuthException();
        }

        final String password = TextUtils.normalize(body.getPassword());
        if (StringUtils.isBlank(password)) {
            throw new AuthException();
        }

        final Credential credential = apiManager.getCredential(username, password);
        if (credential == null) {
            throw new AuthException();
        }

        if (credential.getProfile() != Profile.API) {
            throw new AuthException();
        }

        String makeJwt = JwtUtils.makeJwt(TextUtils.normalize(jwtSign), "Login api", "api", credential);
        return null;
    }

}
