package application.usecases;

import domain.entities.Venta;
import domain.entities.Usuario;
import domain.entities.Comic;
import exceptions.ComicNoDisponibleParaVentaException;
import exceptions.VentaNoProcesableException;
import interfaces.domain.IVentaService;

/**
 * Caso de uso para comprar un cómic.
 * 
 * PRINCIPIOS DE CLEAN ARCHITECTURE:
 * - Orquesta la operación de compra delegando la lógica de negocio al servicio de dominio
 * - Actúa como punto de entrada desde la capa de presentación
 * - Mantiene la separación entre casos de uso y lógica de dominio
 */
public class ComprarLibroCasoUso {
    
    private final IVentaService ventaService;
    
    /**
     * Constructor que recibe las dependencias necesarias.
     * 
     * @param ventaService Servicio de dominio para gestión de ventas
     */
    public ComprarLibroCasoUso(IVentaService ventaService) {
        if (ventaService == null) {
            throw new IllegalArgumentException("El servicio de venta no puede ser nulo");
        }
        this.ventaService = ventaService;
    }
    
    /**
     * Ejecuta el caso de uso de comprar cómic.
     * 
     * @param usuario El usuario que realiza la compra
     * @param comic El cómic a comprar
     * @return La venta procesada
     * @throws IllegalArgumentException si los parámetros son nulos
     * @throws ComicNoDisponibleParaVentaException si el cómic no está disponible
     * @throws VentaNoProcesableException si ocurre un error al procesar la venta
     */
    public Venta ejecutar(Usuario usuario, Comic comic) {
        // Delegar toda la lógica de negocio al servicio de dominio
        return ventaService.procesarVenta(usuario, comic);
    }
    
    /**
     * Verifica si un usuario puede comprar un cómic antes de intentar comprarlo.
     * Útil para validaciones en la UI.
     * 
     * @param usuario El usuario que desea comprar
     * @param comic El cómic a comprar
     * @return true si puede comprar, false en caso contrario
     */
    public boolean puedeComprar(Usuario usuario, Comic comic) {
        return ventaService.puedeComprar(usuario, comic);
    }
}
