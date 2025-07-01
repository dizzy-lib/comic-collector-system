package application.usecases;

import domain.entities.Comic;
import interfaces.domain.IInventarioService;
import java.util.List;

/**
 * Caso de uso para consultar cómics sin actividad (sin ventas ni reservas).
 * 
 * PRINCIPIOS DE CLEAN ARCHITECTURE:
 * - Responsabilidad única: solo obtiene cómics sin actividad
 * - Delega la lógica de negocio al servicio de dominio
 * - Actúa como punto de entrada desde la capa de presentación
 */
public class ConsultarComicsSinActividadCasoUso {
    
    private final IInventarioService inventarioService;
    
    /**
     * Constructor que recibe las dependencias necesarias.
     * 
     * @param inventarioService Servicio de dominio para gestión de inventario
     */
    public ConsultarComicsSinActividadCasoUso(IInventarioService inventarioService) {
        if (inventarioService == null) {
            throw new IllegalArgumentException("El servicio de inventario no puede ser nulo");
        }
        this.inventarioService = inventarioService;
    }
    
    /**
     * Ejecuta el caso de uso para obtener cómics sin actividad.
     * 
     * @return Lista de cómics que no tienen ventas ni reservas
     */
    public List<Comic> ejecutar() {
        return inventarioService.obtenerComicsSinActividad();
    }
}