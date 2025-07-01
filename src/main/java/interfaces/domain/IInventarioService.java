package interfaces.domain;

import domain.entities.Comic;
import java.util.List;
import java.util.Map;

/**
 * Interfaz del servicio de dominio para la gestión de inventario.
 * 
 * Define las operaciones relacionadas con la gestión de stock, disponibilidad
 * y reportes de comics más vendidos/reservados.
 */
public interface IInventarioService {
    
    /**
     * Obtiene todos los comics disponibles para reserva o venta.
     * 
     * @return Lista de comics disponibles (sin reservas activas)
     */
    List<Comic> obtenerComicsDisponibles();
    
    /**
     * Obtiene todos los comics que están actualmente reservados.
     * 
     * @return Lista de comics con reservas activas
     */
    List<Comic> obtenerComicsReservados();
    
    /**
     * Genera un reporte de los comics más vendidos.
     * 
     * @param limite Número máximo de comics a incluir en el reporte
     * @return Mapa con el cómic y la cantidad de ventas (ordenado descendentemente)
     */
    Map<Comic, Long> obtenerComicsMasVendidos(int limite);
    
    /**
     * Genera un reporte de los comics más reservados.
     * 
     * @param limite Número máximo de comics a incluir en el reporte
     * @return Mapa con el cómic y la cantidad de reservas (ordenado descendentemente)
     */
    Map<Comic, Long> obtenerComicsMasReservados(int limite);
    
    /**
     * Obtiene estadísticas generales del inventario.
     * 
     * @return Mapa con estadísticas clave (total comics, disponibles, reservados, etc.)
     */
    Map<String, Long> obtenerEstadisticasInventario();
    
    /**
     * Verifica el estado de disponibilidad de un cómic específico.
     * 
     * @param comic El cómic a verificar
     * @return true si está disponible, false si está reservado
     */
    boolean verificarDisponibilidad(Comic comic);
    
    /**
     * Obtiene comics sin actividad (sin ventas ni reservas).
     * Útil para identificar comics que podrían ser descontinuados.
     * 
     * @return Lista de comics sin actividad
     */
    List<Comic> obtenerComicsSinActividad();
}