package cl.utem.meteo.domain.data.out;

import cl.utem.meteo.domain.model.Seba;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class JwtVO extends Seba {

    private final String token;

    public JwtVO(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}
