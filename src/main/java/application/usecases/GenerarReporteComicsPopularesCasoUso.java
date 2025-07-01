package application.usecases;

import domain.entities.Comic;
import interfaces.domain.IInventarioService;
import java.util.Map;

/**
 * Caso de uso para generar reporte de cómics más populares.
 * 
 * PRINCIPIOS DE CLEAN ARCHITECTURE:
 * - Responsabilidad única: solo genera reporte de popularidad
 * - Delega la lógica de negocio al servicio de dominio
 * - Actúa como punto de entrada desde la capa de presentación
 */
public class GenerarReporteComicsPopularesCasoUso {
    
    private final IInventarioService inventarioService;
    
    /**
     * Constructor que recibe las dependencias necesarias.
     * 
     * @param inventarioService Servicio de dominio para gestión de inventario
     */
    public GenerarReporteComicsPopularesCasoUso(IInventarioService inventarioService) {
        if (inventarioService == null) {
            throw new IllegalArgumentException("El servicio de inventario no puede ser nulo");
        }
        this.inventarioService = inventarioService;
    }
    
    /**
     * Ejecuta el caso de uso para obtener los cómics más vendidos.
     * 
     * @param limite Número máximo de cómics a retornar
     * @return Mapa de cómics y cantidad de ventas ordenados por ventas
     * @throws IllegalArgumentException si el límite es menor a 1
     */
    public Map<Comic, Long> ejecutar(int limite) {
        return inventarioService.obtenerComicsMasVendidos(limite);
    }
}