package application.usecases;

import domain.entities.Comic;
import interfaces.domain.IComicService;
import java.util.List;

/**
 * Caso de uso para buscar cómics por criterio.
 * 
 * PRINCIPIOS DE CLEAN ARCHITECTURE:
 * - Responsabilidad única: solo busca cómics por criterio
 * - Delega la lógica de negocio al servicio de dominio
 * - Actúa como punto de entrada desde la capa de presentación
 */
public class BuscarComicsCasoUso {
    
    private final IComicService comicService;
    
    /**
     * Constructor que recibe las dependencias necesarias.
     * 
     * @param comicService Servicio de dominio para gestión de comics
     */
    public BuscarComicsCasoUso(IComicService comicService) {
        if (comicService == null) {
            throw new IllegalArgumentException("El servicio de cómic no puede ser nulo");
        }
        this.comicService = comicService;
    }
    
    /**
     * Ejecuta el caso de uso para buscar cómics por criterio.
     * 
     * @param criterio El criterio de búsqueda
     * @return Lista de cómics que coinciden con el criterio
     * @throws IllegalArgumentException si el criterio es nulo o vacío
     */
    public List<Comic> ejecutar(String criterio) {
        return comicService.buscarComics(criterio);
    }
}