package application.usecases;

import domain.entities.Comic;
import interfaces.domain.IInventarioService;

/**
 * Caso de uso para consultar la disponibilidad de un cómic.
 * 
 * PRINCIPIOS DE CLEAN ARCHITECTURE:
 * - Responsabilidad única: solo verifica disponibilidad
 * - Delega la lógica de negocio al servicio de dominio
 * - Actúa como punto de entrada desde la capa de presentación
 */
public class ConsultarDisponibilidadComicCasoUso {
    
    private final IInventarioService inventarioService;
    
    /**
     * Constructor que recibe las dependencias necesarias.
     * 
     * @param inventarioService Servicio de dominio para gestión de inventario
     */
    public ConsultarDisponibilidadComicCasoUso(IInventarioService inventarioService) {
        if (inventarioService == null) {
            throw new IllegalArgumentException("El servicio de inventario no puede ser nulo");
        }
        this.inventarioService = inventarioService;
    }
    
    /**
     * Ejecuta el caso de uso para verificar si un cómic está disponible.
     * 
     * @param comic El cómic a verificar
     * @return true si está disponible, false en caso contrario
     * @throws IllegalArgumentException si el cómic es nulo
     */
    public boolean ejecutar(Comic comic) {
        return inventarioService.verificarDisponibilidad(comic);
    }
}