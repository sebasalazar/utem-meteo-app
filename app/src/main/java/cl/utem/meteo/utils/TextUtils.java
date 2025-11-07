package cl.utem.meteo.utils;

import java.util.Locale;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringEscapeUtils;

/**
 * Utilidades de texto con convenciones coherentes para el proyecto.
 * <p>
 * Objetivos:
 * <ul>
 * <li>Normalizar cadenas de entrada (espacios y null-safety).</li>
 * <li>Transformar a mayúsculas/minúsculas utilizando {@code Locale} de Chile
 * (es-CL).</li>
 * </ul>
 *
 * <h3>Comportamiento clave</h3>
 * <ul>
 * <li>Todos los métodos son <b>null-safe</b>: si la entrada es {@code null}, se
 * trata como cadena vacía.</li>
 * <li>{@link #normalize(String)} recorta bordes y colapsa espacios internos
 * repetidos a un solo espacio.</li>
 * <li>{@link #upper(String)} y {@link #lower(String)} aplican
 * {@link #normalize(String)} antes de cambiar el caso.</li>
 * <li>Se usa {@code Locale} <b>es-CL</b> para evitar sorpresas de casing con
 * alfabetos particulares.</li>
 * </ul>
 *
 * <h3>Limitaciones</h3>
 * <ul>
 * <li>No realiza normalización Unicode canónica (no elimina
 * acentos/diacríticos).</li>
 * <li>{@code normalize} no remueve espacios no separables (NBSP) si no fueron
 * detectados por {@code normalizeSpace}.</li>
 * </ul>
 *
 * <h3>Thread-safety</h3>
 * <p>
 * La clase es inmutable y sin estado; es segura para uso concurrente.</p>
 */
public final class TextUtils {

    /**
     * Locale fijo para reglas de mayúsculas/minúsculas coherentes con español
     * de Chile.
     */
    private static final Locale LOCALE_CL = Locale.forLanguageTag("es-CL");

    /**
     * Clase utilitaria: evitar instanciación.
     */
    private TextUtils() {
        throw new IllegalStateException("Clase utilitaria no instanciable");
    }

    /**
     * Normaliza una cadena:
     * <ol>
     * <li>Convierte {@code null} a {@code ""} (cadena vacía).</li>
     * <li>Elimina espacios en los extremos.</li>
     * <li>Colapsa secuencias internas de espacio en un solo espacio.</li>
     * </ol>
     *
     * <p>
     * Ejemplos:</p>
     * <pre>
     * normalize(null)                -> ""
     * normalize("  hola   mundo  ")  -> "hola mundo"
     * normalize("   ")               -> ""
     * </pre>
     *
     * @param text texto de entrada (puede ser {@code null})
     * @return texto normalizado; nunca {@code null}
     */
    public static String normalize(final String text) {
        return StringUtils.normalizeSpace(StringUtils.trimToEmpty(text));
    }

    /**
     * Convierte a MAYÚSCULAS usando {@code es-CL} tras
     * {@link #normalize(String)}.
     *
     * <p>
     * Ejemplos:</p>
     * <pre>
     * upper("  río   mapocho ") -> "RÍO MAPOCHO"
     * upper(null)               -> ""
     * </pre>
     *
     * @param text texto de entrada (puede ser {@code null})
     * @return texto normalizado y en mayúsculas; nunca {@code null}
     */
    public static String upper(final String text) {
        return StringUtils.upperCase(normalize(text), LOCALE_CL);
    }

    /**
     * Convierte a minúsculas usando {@code es-CL} tras
     * {@link #normalize(String)}.
     *
     * <p>
     * Ejemplos:</p>
     * <pre>
     * lower("  RÍO   MAPOCHO ") -> "río mapocho"
     * lower(null)               -> ""
     * </pre>
     *
     * @param text texto de entrada (puede ser {@code null})
     * @return texto normalizado y en minúsculas; nunca {@code null}
     */
    public static String lower(final String text) {
        return StringUtils.lowerCase(normalize(text), LOCALE_CL);
    }

    /**
     * Escapa texto para uso seguro en logs: - Convierte CR/LF/TAB y otros
     * controles a secuencias Java visibles (\\n, \\r, \\t, \\uXXXX). - Escapa
     * separadores Unicode de línea/párrafo (U+2028/U+2029). - Sustituye
     * controles no imprimibles (excepto CR/LF/TAB) por el símbolo � antes de
     * escapar. - Aplica límite de longitud con sufijo informativo.
     *
     * @param rawText Texto original (acepta null).
     * @return Cadena segura y escapada para log.
     */
    public static String escapeForLog(final String rawText) {
        return escapeForLog(rawText, 256);
    }

    /**
     * Escapa texto para uso seguro en logs: - Convierte CR/LF/TAB y otros
     * controles a secuencias Java visibles (\\n, \\r, \\t, \\uXXXX). - Escapa
     * separadores Unicode de línea/párrafo (U+2028/U+2029). - Sustituye
     * controles no imprimibles (excepto CR/LF/TAB) por el símbolo * antes de
     * escapar. - Aplica límite de longitud con sufijo informativo.
     *
     * @param rawText Texto original (acepta null).
     * @param maxLength Longitud máxima del resultado (p.ej. 2000).
     * @return Cadena segura y escapada para log.
     */
    public static String escapeForLog(final String rawText, final int maxLength) {
        final String nonNullInput = normalize(rawText);

        // 1) Limpia controles “peligrosos” excepto CR/LF/TAB (se escaparán luego).
        final String cleanedControls = nonNullInput.replaceAll("[\\p{Cntrl}&&[^\\r\\n\\t]]", "*");

        // 2) Escapa como literal Java (convierte \n, \r, \t, comillas, etc. a secuencias visibles).
        String escaped = StringEscapeUtils.escapeJson(cleanedControls);

        // 3) Endurece separadores de línea/párrafo (no siempre escapados según fuente).
        escaped = escaped
                .replace("\u2028", "\\u2028")
                .replace("\u2029", "\\u2029");

        // 4) Límite de longitud seguro
        final int safeMax = Math.max(64, maxLength);
        if (escaped.length() > safeMax) {
            final String prefix = escaped.substring(0, safeMax);
            final int omitted = escaped.length() - safeMax;
            escaped = prefix + " ... [+" + omitted + " caracteres]";
        }

        return escaped;
    }
}
