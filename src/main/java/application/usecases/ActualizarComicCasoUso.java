package application.usecases;

import domain.entities.Comic;
import domain.valueobjects.Divisa;
import exceptions.ComicNoEncontradoException;
import interfaces.domain.IComicService;

/**
 * Caso de uso para actualizar la información de un cómic.
 * 
 * PRINCIPIOS DE CLEAN ARCHITECTURE:
 * - Responsabilidad única: solo actualiza información del cómic
 * - Delega la lógica de negocio al servicio de dominio
 * - Actúa como punto de entrada desde la capa de presentación
 */
public class ActualizarComicCasoUso {
    
    private final IComicService comicService;
    
    /**
     * Constructor que recibe las dependencias necesarias.
     * 
     * @param comicService Servicio de dominio para gestión de comics
     */
    public ActualizarComicCasoUso(IComicService comicService) {
        if (comicService == null) {
            throw new IllegalArgumentException("El servicio de cómic no puede ser nulo");
        }
        this.comicService = comicService;
    }
    
    /**
     * Ejecuta el caso de uso para actualizar un cómic.
     * 
     * @param comic El cómic a actualizar
     * @return El cómic actualizado
     * @throws IllegalArgumentException si el cómic es nulo
     * @throws ComicNoEncontradoException si el cómic no existe
     */
    public Comic ejecutar(Comic comic) {
        return comicService.actualizarComic(comic);
    }
}