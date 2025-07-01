package interfaces.domain;

import domain.entities.Reserva;
import domain.entities.Usuario;
import domain.entities.Comic;
import java.util.List;

/**
 * Interfaz del servicio de dominio para la gestión de reservas.
 * 
 * Define las operaciones de negocio complejas que involucran múltiples entidades
 * y reglas de negocio que no pertenecen a una sola entidad.
 */
public interface IReservaService {
    
    /**
     * Valida si un usuario puede realizar una nueva reserva para un cómic específico.
     * 
     * @param usuario El usuario que desea hacer la reserva
     * @param comic El cómic a reservar
     * @return true si puede realizar la reserva, false en caso contrario
     */
    boolean puedeReservar(Usuario usuario, Comic comic);
    
    /**
     * Crea una nueva reserva aplicando todas las reglas de negocio.
     * 
     * @param usuario El usuario que hace la reserva
     * @param comic El cómic a reservar
     * @return La reserva creada
     * @throws IllegalStateException si no se puede crear la reserva
     */
    Reserva crearReserva(Usuario usuario, Comic comic);
    
    /**
     * Procesa la expiración automática de reservas vencidas.
     * 
     * @return Lista de reservas que fueron expiradas
     */
    List<Reserva> procesarReservasExpiradas();
    
    /**
     * Verifica si un cómic está disponible para reserva (no tiene reservas activas).
     * 
     * @param comic El cómic a verificar
     * @return true si está disponible, false si ya está reservado
     */
    boolean estaDisponibleParaReserva(Comic comic);
    
    /**
     * Obtiene las reservas activas de un usuario.
     * 
     * @param usuario El usuario
     * @return Lista de reservas activas del usuario
     */
    List<Reserva> obtenerReservasActivasDelUsuario(Usuario usuario);
    
    /**
     * Cancela una reserva aplicando las reglas de negocio correspondientes.
     * 
     * @param reserva La reserva a cancelar
     * @throws IllegalStateException si la reserva no se puede cancelar
     */
    void cancelarReserva(Reserva reserva);
}