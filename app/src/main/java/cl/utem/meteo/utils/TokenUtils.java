package cl.utem.meteo.utils;

import java.security.SecureRandom;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;

public final class TokenUtils {

    /**
     * Largo por defecto del token de cliente.
     */
    private static final int CREDENTIAL_TOKEN_SIZE = 17;

    /**
     * Alfabeto permitido (alfanumérico ASCII).
     */
    private static final char[] TOKEN_ALPHABET
            = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789".toCharArray();

    /**
     * Patrón de validación para token de consumidor: exactamente N
     * alfanuméricos.
     */
    private static final Pattern CUSTOMER_TOKEN_PATTERN
            = Pattern.compile("^[A-Za-z0-9]{" + CREDENTIAL_TOKEN_SIZE + "}$");

    /**
     * PRNG criptográficamente seguro, inicializado una sola vez.
     */
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    private TokenUtils() {
        throw new IllegalStateException("Clase utilitaria no instanciable");
    }

    /**
     * Genera un token alfanumérico de largo especificado (>=1).
     *
     * @param tokenLength Largo del token
     * @return token generado
     */
    public static String generateToken(final int tokenLength) {
        if (tokenLength <= 0) {
            throw new IllegalArgumentException("tokenLength debe ser > 0");
        }
        final StringBuilder builder = new StringBuilder(tokenLength);
        for (int i = 0; i < tokenLength; i++) {
            final int index = SECURE_RANDOM.nextInt(TOKEN_ALPHABET.length);
            builder.append(TOKEN_ALPHABET[index]);
        }
        return builder.toString();
    }

    /**
     * Genera un token alfanumérico de largo {@link #CREDENTIAL_TOKEN_SIZE}.
     *
     * @return Token válido
     */
    public static String getCredentialToken() {
        return generateToken(CREDENTIAL_TOKEN_SIZE);
    }

    /**
     * Valida largo y alfabeto del token de cliente.
     *
     * @param credentialToken Token de cliente
     * @return true si es correcto o false encualquier otro caso
     */
    public static boolean isValidCredentialToken(final String credentialToken) {
        if (StringUtils.isBlank(credentialToken)) {
            return false;
        }
        return CUSTOMER_TOKEN_PATTERN.matcher(credentialToken).matches();
    }
}
