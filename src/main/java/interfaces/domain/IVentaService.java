package interfaces.domain;

import domain.entities.Venta;
import domain.entities.Usuario;
import domain.entities.Comic;
import domain.entities.Reserva;
import java.util.Optional;

/**
 * Interfaz del servicio de dominio para la gestión de ventas.
 * 
 * Define las operaciones de negocio complejas que involucran la coordinación
 * entre ventas, reservas, comics y usuarios.
 */
public interface IVentaService {
    
    /**
     * Valida si un usuario puede comprar un cómic específico.
     * 
     * @param usuario El usuario que desea comprar
     * @param comic El cómic a comprar
     * @return true si puede realizar la compra, false en caso contrario
     */
    boolean puedeComprar(Usuario usuario, Comic comic);
    
    /**
     * Procesa una venta aplicando todas las reglas de negocio.
     * Incluye verificación de disponibilidad, manejo de reservas y creación de la venta.
     * 
     * @param usuario El usuario que realiza la compra
     * @param comic El cómic a comprar
     * @return La venta procesada
     * @throws IllegalStateException si no se puede procesar la venta
     */
    Venta procesarVenta(Usuario usuario, Comic comic);
    
    /**
     * Verifica si existe una reserva activa del usuario para el cómic
     * y la procesa para convertirla en venta.
     * 
     * @param usuario El usuario
     * @param comic El cómic
     * @return La reserva encontrada si existe
     */
    Optional<Reserva> verificarYProcesarReserva(Usuario usuario, Comic comic);
    
    /**
     * Verifica si un cómic está disponible para venta.
     * Considera reservas activas y disponibilidad general.
     * 
     * @param comic El cómic a verificar
     * @param usuario El usuario que desea comprar (para verificar sus reservas)
     * @return true si está disponible para este usuario
     */
    boolean estaDisponibleParaVenta(Comic comic, Usuario usuario);
}