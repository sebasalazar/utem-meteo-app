package cl.utem.meteo.utils;

import java.io.Serializable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class PageUtils implements Serializable {

    /**
     * El tiempo de ejecución de serialización asocia con cada clase
     * serializable un número de versión, llamado serialVersionUID, que se usa
     * durante la deserialización para verificar que el remitente y el receptor
     * de un objeto serializado hayan cargado clases para ese objeto que sean
     * compatibles con respecto a la serialización. Si el receptor ha cargado
     * una clase para el objeto que tiene un serialVersionUID diferente al de la
     * clase del remitente correspondiente, entonces la deserialización
     * resultará en una InvalidClassException.
     *
     * Una clase serializable declara su propio serialVersionUID como un campo
     * serialVersionUID que DEBE ser static, final, y de tipo long. Sino se
     * declara la JVM lo calcula en tiempo de ejecución.
     */
    private static final long serialVersionUID = 1L;
    /**
     * Cantidad de filas por defecto a procesar en cada iteración. Se busca un
     * número que no sature la memoria (depende de la complejidad de los
     * objetos) pero que permita aprovechar de buena manera la capacidad de
     * computo.
     */
    public static final int DEFAULT_PAGE_ROWS = 768;

    /**
     * Dado que nuestras entidades tiene todas un atributo de fecha created, se
     * usará como mecanismo por defecto.
     */
    private static final Sort DEFAULT_SORT = Sort.by("created").descending();

    /**
     *
     * @param total Cantidad total de datos a procesar
     * @return La cantidad de Páginas (ventanas) que tendremos que procesar.
     */
    public static int getWindowSize(final long total) {
        return getWindowSize(total, DEFAULT_PAGE_ROWS);
    }

    /**
     *
     * @param total Cantidad total de datos a procesar
     * @param pageRows Cantidad de filas por página
     * @return La cantidad de Páginas (ventanas) que tendremos que procesar.
     */
    public static int getWindowSize(final long total, final int pageRows) {
        int windows = 0;
        if (total > 0 && pageRows > 0) {
            // Calcula correctamente el número de páginas necesarias
            windows = (int) ((total + pageRows - 1) / pageRows);
        }
        return windows;
    }

    /**
     *
     * @param pageNum Número de página
     * @return Opciones de paginación.
     */
    public static Pageable getPageable(final int pageNum) {
        return PageRequest.of(pageNum, DEFAULT_PAGE_ROWS, DEFAULT_SORT);
    }

    /**
     *
     * @param pageNum Número de página
     * @param pageSize Tamaño de página
     * @return Opciones de paginación.
     */
    public static Pageable getPageable(final int pageNum, final int pageSize) {
        return PageRequest.of(pageNum, pageSize, DEFAULT_SORT);
    }

    /**
     *
     * @param pageNum Número de página
     * @param column Columna a ordenar decendentemente
     * @return Opciones de paginación.
     */
    public static Pageable getPageable(final int pageNum, final String column) {
        return PageRequest.of(pageNum, DEFAULT_PAGE_ROWS, Sort.by(column).descending());
    }

    /**
     *
     * @param pageNum Número de página
     * @param pageSize Tamaño de página
     * @param sortBy Orden de Página
     * @return Opciones de paginación.
     */
    public static Pageable getPageable(final int pageNum, final int pageSize, final Sort sortBy) {
        return PageRequest.of(pageNum, pageSize, sortBy);
    }

    /**
     * Comprueba si una página genérica contiene datos.
     *
     * @param <T> El tipo de los elementos contenidos en la página.
     * @param page La página a comprobar.
     * @return true si la página no es null y no está vacía, false en caso
     * contrario.
     */
    public static <T> boolean hasData(Page<T> page) {
        return page != null && !page.isEmpty();
    }
}
