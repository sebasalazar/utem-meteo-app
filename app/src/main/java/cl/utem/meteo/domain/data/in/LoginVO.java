package cl.utem.meteo.domain.data.in;

import cl.utem.meteo.domain.model.Seba;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Login")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LoginVO extends Seba {

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED,
            description = "Nombre de usuario",
            example = "jperez")
    private String username = null;

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED,
            description = "Contraseña para acceder al sistema",
            example = "f70cb740")
    private String password = null;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
