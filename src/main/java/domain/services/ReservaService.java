package domain.services;

import domain.entities.Reserva;
import domain.entities.Usuario;
import domain.entities.Comic;
import domain.enums.EstadoReserva;
import exceptions.ComicNoDisponibleException;
import exceptions.LimiteReservasExcedidoException;
import exceptions.ReservaNoCancelableException;
import interfaces.domain.IReservaService;
import interfaces.repository.IReservaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Servicio de dominio para la gestión de reservas.
 * 
 * PRINCIPIOS DE ARQUITECTURA LIMPIA APLICADOS:
 * 
 * 1. RESPONSABILIDAD ÚNICA: Se encarga únicamente de la lógica de negocio de reservas
 *    que involucra múltiples entidades y no pertenece a una entidad específica.
 * 
 * 2. INVERSIÓN DE DEPENDENCIAS: Depende de abstracciones (IReservaRepository) 
 *    no de implementaciones concretas.
 * 
 * 3. SEPARACIÓN DE CONCERNS: La lógica de negocio está separada de la persistencia
 *    y la presentación.
 * 
 * 4. REGLAS DE NEGOCIO EXPLÍCITAS: Las políticas y reglas están claramente definidas
 *    en métodos con nombres descriptivos.
 * 
 * 5. INDEPENDENCIA DE FRAMEWORKS: No depende de frameworks externos, solo del dominio.
 */
public class ReservaService implements IReservaService {
    
    // Reglas de negocio como constantes

    // Límite de reservas por usuario
    private static final int LIMITE_RESERVAS_POR_USUARIO = 3;
    // Tiempo límite para cancelar una reserva
    private static final int HORAS_LIMITE_CANCELACION = 1;
    // Tiempo de duración de una reserva
    private static final int DIAS_MAXIMO_RESERVA = 2;
    
    private final IReservaRepository reservaRepository;
    
    /**
     * Constructor que recibe las dependencias necesarias (Dependency Injection).
     * 
     * @param reservaRepository Repositorio de reservas
     */
    public ReservaService(IReservaRepository reservaRepository) {
        if (reservaRepository == null) {
            throw new IllegalArgumentException("El repositorio de reservas no puede ser nulo");
        }
        this.reservaRepository = reservaRepository;
    }
    
    @Override
    public boolean puedeReservar(Usuario usuario, Comic comic) {
        if (usuario == null || comic == null) {
            return false;
        }
        
        // Verificar si el cómic está disponible
        if (!estaDisponibleParaReserva(comic)) {
            return false;
        }
        
        // Verificar límite de reservas del usuario
        List<Reserva> reservasActivas = obtenerReservasActivasDelUsuario(usuario);
        if (reservasActivas.size() >= LIMITE_RESERVAS_POR_USUARIO) {
            return false;
        }
        
        // Verificar si el usuario ya tiene una reserva activa para este cómic
        boolean yaReservoPorEsteUsuario = reservasActivas.stream()
                .anyMatch(reserva -> reserva.getComic().getId().equals(comic.getId()));
        
        return !yaReservoPorEsteUsuario;
    }
    
    @Override
    public Reserva crearReserva(Usuario usuario, Comic comic) {
        // Validaciones de negocio
        if (usuario == null) {
            throw new IllegalArgumentException("El usuario no puede ser nulo");
        }
        if (comic == null) {
            throw new IllegalArgumentException("El cómic no puede ser nulo");
        }
        
        // Aplicar reglas de negocio
        if (!estaDisponibleParaReserva(comic)) {
            throw new ComicNoDisponibleException(
                String.format("El cómic '%s' no está disponible para reserva", comic.getNombre())
            );
        }
        
        List<Reserva> reservasActivas = obtenerReservasActivasDelUsuario(usuario);
        if (reservasActivas.size() >= LIMITE_RESERVAS_POR_USUARIO) {
            throw new LimiteReservasExcedidoException(
                String.format("El usuario ha excedido el límite de %d reservas simultáneas", 
                LIMITE_RESERVAS_POR_USUARIO)
            );
        }
        
        // Verificar si ya tiene reserva para este cómic
        boolean yaReservoPorEsteUsuario = reservasActivas.stream()
                .anyMatch(reserva -> reserva.getComic().getId().equals(comic.getId()));
        
        if (yaReservoPorEsteUsuario) {
            throw new ComicNoDisponibleException(
                String.format("El usuario ya tiene una reserva activa para el cómic '%s'", comic.getNombre())
            );
        }
        
        // Crear la reserva con fecha de expiración calculada por el servicio
        Reserva nuevaReserva = new Reserva(usuario, comic);
        
        // Establecer fecha de expiración según reglas de negocio del servicio
        LocalDateTime fechaExpiracion = LocalDateTime.now().plusDays(DIAS_MAXIMO_RESERVA);
        nuevaReserva.establecerFechaExpiracion(fechaExpiracion);
        
        reservaRepository.guardar(nuevaReserva);
        
        return nuevaReserva;
    }
    
    @Override
    public List<Reserva> procesarReservasExpiradas() {
        List<Reserva> reservasExpiradas = reservaRepository.buscarReservasExpiradas();
        
        // Cambiar estado de las reservas expiradas
        for (Reserva reserva : reservasExpiradas) {
            if (reserva.getEstadoReserva() == EstadoReserva.ACTIVO) {
                reserva.setReservaInactiva();
                reservaRepository.actualizar(reserva);
            }
        }
        
        return reservasExpiradas;
    }
    
    @Override
    public boolean estaDisponibleParaReserva(Comic comic) {
        if (comic == null) {
            return false;
        }
        
        List<Reserva> reservasDelComic = reservaRepository.buscarPorComic(comic);
        
        // Verificar si hay alguna reserva activa para este cómic
        return reservasDelComic.stream()
                .noneMatch(reserva -> reserva.getEstadoReserva() == EstadoReserva.ACTIVO);
    }
    
    @Override
    public List<Reserva> obtenerReservasActivasDelUsuario(Usuario usuario) {
        if (usuario == null) {
            return List.of();
        }
        
        return reservaRepository.buscarPorUsuario(usuario).stream()
                .filter(reserva -> reserva.getEstadoReserva() == EstadoReserva.ACTIVO)
                .collect(Collectors.toList());
    }
    
    @Override
    public void cancelarReserva(Reserva reserva) {
        if (reserva == null) {
            throw new IllegalArgumentException("La reserva no puede ser nula");
        }
        
        // Verificar que la reserva esté activa
        if (reserva.getEstadoReserva() != EstadoReserva.ACTIVO) {
            throw new ReservaNoCancelableException(
                "Solo se pueden cancelar reservas en estado activo"
            );
        }
        
        // Verificar límite de tiempo para cancelación
        LocalDateTime limiteCancelacion = reserva.getFechaReserva().plusHours(HORAS_LIMITE_CANCELACION);
        if (LocalDateTime.now().isAfter(limiteCancelacion)) {
            throw new ReservaNoCancelableException(
                String.format("No se puede cancelar la reserva después de %d hora(s) de su creación", 
                HORAS_LIMITE_CANCELACION)
            );
        }
        
        // Cancelar la reserva
        reserva.setReservaInactiva();
        reservaRepository.actualizar(reserva);
    }
}
