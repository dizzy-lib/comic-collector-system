package application.usecases;

import domain.entities.Reserva;
import domain.entities.Usuario;
import interfaces.domain.IReservaService;
import java.util.List;

/**
 * Caso de uso para consultar las reservas de un usuario.
 * 
 * PRINCIPIOS DE CLEAN ARCHITECTURE:
 * - Responsabilidad única: solo maneja la consulta de reservas por usuario
 * - Delega la lógica de negocio al servicio de dominio
 * - Actúa como punto de entrada desde la capa de presentación
 */
public class ConsultarReservasUsuarioCasoUso {
    
    private final IReservaService reservaService;
    
    /**
     * Constructor que recibe las dependencias necesarias.
     * 
     * @param reservaService Servicio de dominio para gestión de reservas
     */
    public ConsultarReservasUsuarioCasoUso(IReservaService reservaService) {
        if (reservaService == null) {
            throw new IllegalArgumentException("El servicio de reserva no puede ser nulo");
        }
        this.reservaService = reservaService;
    }
    
    /**
     * Ejecuta el caso de uso para obtener las reservas activas de un usuario.
     * 
     * @param usuario El usuario del cual consultar reservas
     * @return Lista de reservas activas del usuario
     * @throws IllegalArgumentException si el usuario es nulo
     */
    public List<Reserva> ejecutar(Usuario usuario) {
        return reservaService.obtenerReservasActivasDelUsuario(usuario);
    }
}