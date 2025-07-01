package interfaces.domain;

import domain.entities.Comic;
import domain.valueobjects.Divisa;
import java.util.List;

/**
 * Interfaz del servicio de dominio para la gestión de comics.
 * 
 * Define las operaciones de negocio complejas relacionadas con comics
 * que involucran validaciones y reglas que no pertenecen a la entidad Comic.
 */
public interface IComicService {
    
    /**
     * Agrega un nuevo cómic al catálogo aplicando todas las reglas de negocio.
     * 
     * @param nombre El nombre del cómic
     * @param descripcion La descripción del cómic
     * @param precio El precio del cómic
     * @return El cómic agregado al catálogo
     * @throws IllegalStateException si no se puede agregar el cómic
     */
    Comic agregarComic(String nombre, String descripcion, Divisa precio);
    
    /**
     * Verifica si un cómic puede ser eliminado del catálogo.
     * No se puede eliminar si tiene reservas activas o ventas asociadas.
     * 
     * @param comic El cómic a verificar
     * @return true si puede ser eliminado, false en caso contrario
     */
    boolean puedeEliminarComic(Comic comic);
    
    /**
     * Elimina un cómic del catálogo aplicando todas las reglas de negocio.
     * 
     * @param comic El cómic a eliminar
     * @throws IllegalStateException si no se puede eliminar el cómic
     */
    void eliminarComic(Comic comic);
    
    /**
     * Actualiza la información de un cómic aplicando validaciones.
     * 
     * @param comic El cómic a actualizar
     * @return El cómic actualizado
     * @throws IllegalStateException si no se puede actualizar
     */
    Comic actualizarComic(Comic comic);
    
    /**
     * Busca comics por diferentes criterios.
     * 
     * @param criterio El criterio de búsqueda (nombre, descripción)
     * @return Lista de comics que coinciden con el criterio
     */
    List<Comic> buscarComics(String criterio);
    
    /**
     * Verifica si ya existe un cómic con el mismo nombre.
     * 
     * @param nombre El nombre a verificar
     * @return true si ya existe, false en caso contrario
     */
    boolean nombreComicYaExiste(String nombre);
    
    /**
     * Obtiene todos los comics disponibles (sin reservas activas).
     * 
     * @return Lista de comics disponibles
     */
    List<Comic> obtenerComicsDisponibles();
}