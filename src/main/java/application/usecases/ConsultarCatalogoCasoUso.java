package application.usecases;

import domain.entities.Comic;
import interfaces.domain.IComicService;
import java.util.List;

/**
 * Caso de uso para consultar el catálogo completo de cómics.
 * 
 * PRINCIPIOS DE CLEAN ARCHITECTURE:
 * - Responsabilidad única: solo obtiene el catálogo de cómics
 * - Delega la lógica de negocio al servicio de dominio
 * - Actúa como punto de entrada desde la capa de presentación
 */
public class ConsultarCatalogoCasoUso {
    
    private final IComicService comicService;
    
    /**
     * Constructor que recibe las dependencias necesarias.
     * 
     * @param comicService Servicio de dominio para gestión de comics
     */
    public ConsultarCatalogoCasoUso(IComicService comicService) {
        if (comicService == null) {
            throw new IllegalArgumentException("El servicio de cómic no puede ser nulo");
        }
        this.comicService = comicService;
    }
    
    /**
     * Ejecuta el caso de uso para obtener todo el catálogo de cómics.
     * 
     * @return Lista de todos los cómics disponibles
     */
    public List<Comic> ejecutar() {
        return comicService.obtenerComicsDisponibles();
    }
}