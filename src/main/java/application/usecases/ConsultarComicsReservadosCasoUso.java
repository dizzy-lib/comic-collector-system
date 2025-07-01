package application.usecases;

import domain.entities.Comic;
import interfaces.domain.IInventarioService;
import java.util.List;

/**
 * Caso de uso para consultar los cómics que están reservados.
 * 
 * PRINCIPIOS DE CLEAN ARCHITECTURE:
 * - Responsabilidad única: solo obtiene cómics reservados
 * - Delega la lógica de negocio al servicio de dominio
 * - Actúa como punto de entrada desde la capa de presentación
 */
public class ConsultarComicsReservadosCasoUso {
    
    private final IInventarioService inventarioService;
    
    /**
     * Constructor que recibe las dependencias necesarias.
     * 
     * @param inventarioService Servicio de dominio para gestión de inventario
     */
    public ConsultarComicsReservadosCasoUso(IInventarioService inventarioService) {
        if (inventarioService == null) {
            throw new IllegalArgumentException("El servicio de inventario no puede ser nulo");
        }
        this.inventarioService = inventarioService;
    }
    
    /**
     * Ejecuta el caso de uso para obtener los cómics que están reservados.
     * 
     * @return Lista de cómics que tienen reservas activas
     */
    public List<Comic> ejecutar() {
        return inventarioService.obtenerComicsReservados();
    }
}