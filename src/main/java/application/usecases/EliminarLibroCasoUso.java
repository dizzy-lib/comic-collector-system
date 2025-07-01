package application.usecases;

import domain.entities.Comic;
import exceptions.ComicNoEliminableException;
import exceptions.ComicNoEncontradoException;
import interfaces.domain.IComicService;

/**
 * Caso de uso para eliminar un cómic del catálogo.
 * 
 * PRINCIPIOS DE CLEAN ARCHITECTURE:
 * - Orquesta la operación de eliminación delegando la lógica de negocio al servicio de dominio
 * - Actúa como punto de entrada desde la capa de presentación
 * - Mantiene la separación entre casos de uso y lógica de dominio
 */
public class EliminarLibroCasoUso {
    
    private final IComicService comicService;
    
    /**
     * Constructor que recibe las dependencias necesarias.
     * 
     * @param comicService Servicio de dominio para gestión de comics
     */
    public EliminarLibroCasoUso(IComicService comicService) {
        if (comicService == null) {
            throw new IllegalArgumentException("El servicio de cómic no puede ser nulo");
        }
        this.comicService = comicService;
    }
    
    /**
     * Ejecuta el caso de uso de eliminar cómic.
     * 
     * @param comic El cómic a eliminar
     * @throws IllegalArgumentException si el cómic es nulo
     * @throws ComicNoEncontradoException si el cómic no existe
     * @throws ComicNoEliminableException si el cómic tiene reservas activas o ventas
     */
    public void ejecutar(Comic comic) {
        // Delegar toda la lógica de negocio al servicio de dominio
        comicService.eliminarComic(comic);
    }
    
    /**
     * Verifica si un cómic puede ser eliminado antes de intentar eliminarlo.
     * Útil para validaciones en la UI.
     * 
     * @param comic El cómic a verificar
     * @return true si puede ser eliminado, false en caso contrario
     */
    public boolean puedeEliminar(Comic comic) {
        return comicService.puedeEliminarComic(comic);
    }
}
