package application.usecases;

import interfaces.domain.IInventarioService;
import java.util.Map;

/**
 * Caso de uso para generar reporte del estado del inventario.
 * 
 * PRINCIPIOS DE CLEAN ARCHITECTURE:
 * - Responsabilidad única: solo genera reporte de inventario
 * - Delega la lógica de negocio al servicio de dominio
 * - Actúa como punto de entrada desde la capa de presentación
 */
public class GenerarReporteInventarioCasoUso {
    
    private final IInventarioService inventarioService;
    
    /**
     * Constructor que recibe las dependencias necesarias.
     * 
     * @param inventarioService Servicio de dominio para gestión de inventario
     */
    public GenerarReporteInventarioCasoUso(IInventarioService inventarioService) {
        if (inventarioService == null) {
            throw new IllegalArgumentException("El servicio de inventario no puede ser nulo");
        }
        this.inventarioService = inventarioService;
    }
    
    /**
     * Ejecuta el caso de uso para generar estadísticas del inventario.
     * 
     * @return Mapa con estadísticas del inventario (total comics, disponibles, reservados, etc.)
     */
    public Map<String, Long> ejecutar() {
        return inventarioService.obtenerEstadisticasInventario();
    }
}