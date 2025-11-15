package cl.utem.meteo.domain.model;

import cl.utem.meteo.domain.enums.Profile;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "credentials")
public class Credential extends PkEntityBase {

    @Column(name = "token", nullable = false, unique = true)
    private String token = null;

    @Column(name = "username", nullable = false)
    private String username = null;

    @Column(name = "password", nullable = false)
    private String password = null;

    @Column(name = "profile", nullable = false, columnDefinition = "smallint")
    private Profile profile = Profile.VIEWER;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

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

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }
}
