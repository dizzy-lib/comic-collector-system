package application.usecases;

import domain.entities.Reserva;
import interfaces.domain.IReservaService;
import java.util.List;

/**
 * Caso de uso para procesar reservas expiradas automáticamente.
 * 
 * PRINCIPIOS DE CLEAN ARCHITECTURE:
 * - Responsabilidad única: solo procesa reservas expiradas
 * - Delega la lógica de negocio al servicio de dominio
 * - Actúa como punto de entrada desde la capa de presentación
 */
public class ProcesarReservasExpiradasCasoUso {
    
    private final IReservaService reservaService;
    
    /**
     * Constructor que recibe las dependencias necesarias.
     * 
     * @param reservaService Servicio de dominio para gestión de reservas
     */
    public ProcesarReservasExpiradasCasoUso(IReservaService reservaService) {
        if (reservaService == null) {
            throw new IllegalArgumentException("El servicio de reserva no puede ser nulo");
        }
        this.reservaService = reservaService;
    }
    
    /**
     * Ejecuta el caso de uso para procesar reservas expiradas.
     * 
     * @return Lista de reservas que fueron procesadas como expiradas
     */
    public List<Reserva> ejecutar() {
        return reservaService.procesarReservasExpiradas();
    }
}