package application.usecases;

import domain.entities.Reserva;
import exceptions.ReservaNoCancelableException;
import interfaces.domain.IReservaService;

/**
 * Caso de uso para cancelar una reserva existente.
 * 
 * PRINCIPIOS DE CLEAN ARCHITECTURE:
 * - Responsabilidad única: solo maneja la cancelación de reservas
 * - Delega la lógica de negocio al servicio de dominio
 * - Actúa como punto de entrada desde la capa de presentación
 */
public class CancelarReservaCasoUso {
    
    private final IReservaService reservaService;
    
    /**
     * Constructor que recibe las dependencias necesarias.
     * 
     * @param reservaService Servicio de dominio para gestión de reservas
     */
    public CancelarReservaCasoUso(IReservaService reservaService) {
        if (reservaService == null) {
            throw new IllegalArgumentException("El servicio de reserva no puede ser nulo");
        }
        this.reservaService = reservaService;
    }
    
    /**
     * Ejecuta el caso de uso de cancelar reserva.
     * 
     * @param reserva La reserva a cancelar
     * @throws IllegalArgumentException si la reserva es nula
     * @throws ReservaNoCancelableException si la reserva no se puede cancelar
     */
    public void ejecutar(Reserva reserva) {
        reservaService.cancelarReserva(reserva);
    }
    
}