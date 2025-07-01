package domain.entities;

import domain.enums.EstadoReserva;
import exceptions.ReservaInvalidaException;
import exceptions.ReservaYaActivaException;
import exceptions.ReservaYaExpiradaException;
import interfaces.domain.IReserva;

import java.time.LocalDateTime;
import java.util.UUID;

public class Reserva implements IReserva, Comparable<Reserva> {
    private String id;
    private Usuario usuario;
    private Comic comic;
    private LocalDateTime fechaReserva;
    private LocalDateTime fechaExpiracion;
    private EstadoReserva estado;

    public Reserva(Usuario usuario, Comic comic) {
        if (usuario == null) {
            throw new ReservaInvalidaException("El usuario no puede ser nulo");
        }
        if (comic == null) {
            throw new ReservaInvalidaException("El cómic no puede ser nulo");
        }
        
        this.id = UUID.randomUUID().toString();
        this.usuario = usuario;
        this.comic = comic;
        
        // Solo establecer fecha de reserva y estado, la expiración la maneja el servicio
        this.fechaReserva = LocalDateTime.now();
        this.estado = EstadoReserva.ACTIVO;
    }

    @Override
    public LocalDateTime getFechaReserva() { return fechaReserva; }

    @Override
    public LocalDateTime getFechaExpiracionReserva() { return fechaExpiracion; }

    @Override
    public String getId() { return id; }

    @Override
    public Comic getComic() { return comic; }

    @Override
    public Usuario getUsuario() { return usuario; }

    @Override
    public EstadoReserva getEstadoReserva() { return estado; }

    @Override
    public void activarReserva() {
        // Verifica si la reserva se encuentra activa
        // una reserva activa no se puede modificar para volver a activar
        // a no ser que se encuentre expirada
        if (this.estado == EstadoReserva.ACTIVO) {
            throw new ReservaYaActivaException(String.format("Reserva %s activada, cancelar la reserva y volver a intentar", id));
        }

        // Establece fecha de reserva actual
        this.fechaReserva = LocalDateTime.now();
        this.estado = EstadoReserva.ACTIVO;
        
        // La fecha de expiración debe ser establecida por el servicio de dominio
        // que contiene las reglas de negocio
    }

    @Override
    public void setReservaInactiva() {
        // verifica que la reserva se encuentre activa
        if (this.estado == EstadoReserva.EXPIRADA) {
            throw new ReservaYaExpiradaException(String.format("Reserva %s ya expirada", id));
        }

        this.estado = EstadoReserva.EXPIRADA;
    }

    /**
     * Establece la fecha de expiración de la reserva.
     * Este método debe ser llamado por el servicio de dominio que contiene
     * las reglas de negocio para calcular el tiempo de expiración.
     * 
     * @param fechaExpiracion La fecha y hora de expiración
     */
    public void establecerFechaExpiracion(LocalDateTime fechaExpiracion) {
        if (fechaExpiracion == null) {
            throw new ReservaInvalidaException("La fecha de expiración no puede ser nula");
        }
        if (fechaExpiracion.isBefore(this.fechaReserva)) {
            throw new ReservaInvalidaException("La fecha de expiración no puede ser anterior a la fecha de reserva");
        }
        this.fechaExpiracion = fechaExpiracion;
    }

    @Override
    public int compareTo(Reserva otra) {
        if (otra == null) return 1;
        
        int comparacionFecha = this.fechaExpiracion.compareTo(otra.fechaExpiracion);
        if (comparacionFecha != 0) {
            return comparacionFecha;
        }
        
        return this.id.compareTo(otra.id);
    }
}
