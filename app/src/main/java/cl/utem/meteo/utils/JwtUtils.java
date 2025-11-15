package cl.utem.meteo.utils;

import cl.utem.meteo.domain.model.Credential;
import cl.utem.meteo.exception.AuthException;
import cl.utem.meteo.exception.ValidationException;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import java.time.Duration;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class JwtUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(JwtUtils.class);

    private static final String ISSUER = "UTEM";
    private static final Duration JWT_DURATION = Duration.ofMinutes(67);
    private static final long CLOCK_SKEW_LEEWAY_SECONDS = 67L;

    private static final String CLAIM_TOKEN = "credential_id";

    private JwtUtils() {
        throw new IllegalStateException("Clase utilitaria");
    }

    public static String makeJwt(final String sign, final String subject, final String audience, final Credential credential) {
        try {
            if (StringUtils.isBlank(sign) || sign.length() < 32) {
                LOGGER.warn("Firma HMAC inválida o débil (min 32 caracteres).");
                throw new ValidationException("Fallo en la firma");
            }

            final String token = credential.getToken();
            if (StringUtils.isAnyBlank(sign, subject, audience, token)) {
                throw new ValidationException();
            }

            Map<String, String> data = new HashMap<>();
            data.put("username", credential.getUsername());
            data.put("profile", credential.getProfile().getLabel());
            data.put("access", OffsetDateTime.now().toString());

            final Instant issuedAt = Instant.now();
            final Instant expiresAt = issuedAt.plus(JWT_DURATION);

            final Algorithm algorithm = Algorithm.HMAC512(sign);
            com.auth0.jwt.JWTCreator.Builder builder = JWT.create()
                    .withJWTId(UUID.randomUUID().toString())
                    .withIssuer(ISSUER)
                    .withSubject(subject)
                    .withIssuedAt(Date.from(issuedAt))
                    .withExpiresAt(Date.from(expiresAt))
                    .withPayload(data)
                    .withClaim(CLAIM_TOKEN, token);

            if (StringUtils.isNotBlank(audience)) {
                builder = builder.withAudience(audience);
            }

            return builder.sign(algorithm);
        } catch (Exception e) {
            throw new AuthException("No fue posible autenticar con las credenciales provistas", e);
        }
    }

    /**
     * Extrae el claim "c" (commerce code) de un Bearer token válido. Devuelve
     * "" si no es válido.
     *
     * @param sign Secreto HMAC
     * @param bearerToken Cabecera Authorization completa ("Bearer ...")
     * @return Claim "c" o ""
     */
    public static String getData(final String sign, final String bearerToken) {
        if (StringUtils.isBlank(sign) || sign.length() < 32 || StringUtils.isBlank(bearerToken)) {
            return StringUtils.EMPTY;
        }

        final String jwtText = StringUtils.trimToEmpty(
                StringUtils.removeStartIgnoreCase(bearerToken, "Bearer ")
        );

        if (StringUtils.isBlank(jwtText)) {
            return StringUtils.EMPTY;
        }

        try {
            final Algorithm algorithm = Algorithm.HMAC512(sign);
            final JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer(ISSUER)
                    .acceptLeeway(CLOCK_SKEW_LEEWAY_SECONDS)
                    .build();

            final DecodedJWT jwt = verifier.verify(jwtText);
            return StringUtils.defaultString(jwt.getClaim(CLAIM_TOKEN).asString());
        } catch (Exception e) {
            LOGGER.error("Error al verificar/leer JWT: {}", e.getLocalizedMessage());
            LOGGER.debug("Error al verificar/leer JWT: {}", e.getMessage(), e);
            return StringUtils.EMPTY;
        }
    }
}
