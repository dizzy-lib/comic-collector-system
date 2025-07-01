package application.usecases;

import domain.entities.Comic;
import domain.valueobjects.Divisa;
import exceptions.NombreComicYaExisteException;
import interfaces.domain.IComicService;

/**
 * Caso de uso para agregar un nuevo cómic al catálogo.
 * 
 * PRINCIPIOS DE CLEAN ARCHITECTURE:
 * - Orquesta la operación de agregado delegando la lógica de negocio al servicio de dominio
 * - Actúa como punto de entrada desde la capa de presentación
 * - Mantiene la separación entre casos de uso y lógica de dominio
 */
public class AgregarLibroCasoUso {
    
    private final IComicService comicService;
    
    /**
     * Constructor que recibe las dependencias necesarias.
     * 
     * @param comicService Servicio de dominio para gestión de comics
     */
    public AgregarLibroCasoUso(IComicService comicService) {
        if (comicService == null) {
            throw new IllegalArgumentException("El servicio de cómic no puede ser nulo");
        }
        this.comicService = comicService;
    }
    
    /**
     * Ejecuta el caso de uso de agregar cómic.
     * 
     * @param nombre El nombre del cómic
     * @param descripcion La descripción del cómic
     * @param precio El precio del cómic
     * @return El cómic agregado al catálogo
     * @throws IllegalArgumentException si los datos de entrada son inválidos
     * @throws NombreComicYaExisteException si ya existe un cómic con ese nombre
     */
    public Comic ejecutar(String nombre, String descripcion, Divisa precio) {
        // Delegar toda la lógica de negocio al servicio de dominio
        return comicService.agregarComic(nombre, descripcion, precio);
    }
}
