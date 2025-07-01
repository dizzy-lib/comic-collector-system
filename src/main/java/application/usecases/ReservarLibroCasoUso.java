package application.usecases;

import domain.entities.Reserva;
import domain.entities.Usuario;
import domain.entities.Comic;
import exceptions.ComicNoDisponibleException;
import exceptions.LimiteReservasExcedidoException;
import interfaces.domain.IReservaService;

/**
 * Caso de uso para reservar un cómic.
 * 
 * PRINCIPIOS DE CLEAN ARCHITECTURE:
 * - Orquesta la operación de reserva delegando la lógica de negocio al servicio de dominio
 * - Actúa como punto de entrada desde la capa de presentación
 * - Mantiene la separación entre casos de uso y lógica de dominio
 */
public class ReservarLibroCasoUso {
    
    private final IReservaService reservaService;
    
    /**
     * Constructor que recibe las dependencias necesarias.
     * 
     * @param reservaService Servicio de dominio para gestión de reservas
     */
    public ReservarLibroCasoUso(IReservaService reservaService) {
        if (reservaService == null) {
            throw new IllegalArgumentException("El servicio de reserva no puede ser nulo");
        }
        this.reservaService = reservaService;
    }
    
    /**
     * Ejecuta el caso de uso de reservar cómic.
     * 
     * @param usuario El usuario que realiza la reserva
     * @param comic El cómic a reservar
     * @return La reserva creada
     * @throws IllegalArgumentException si los parámetros son nulos
     * @throws ComicNoDisponibleException si el cómic no está disponible
     * @throws LimiteReservasExcedidoException si el usuario excede el límite de reservas
     */
    public Reserva ejecutar(Usuario usuario, Comic comic) {
        // Delegar toda la lógica de negocio al servicio de dominio
        return reservaService.crearReserva(usuario, comic);
    }
    
    /**
     * Verifica si un usuario puede reservar un cómic antes de intentar reservarlo.
     * Útil para validaciones en la UI.
     * 
     * @param usuario El usuario que desea reservar
     * @param comic El cómic a reservar
     * @return true si puede reservar, false en caso contrario
     */
    public boolean puedeReservar(Usuario usuario, Comic comic) {
        return reservaService.puedeReservar(usuario, comic);
    }
    
}
