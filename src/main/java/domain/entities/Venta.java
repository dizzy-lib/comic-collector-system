package domain.entities;

import domain.valueobjects.Divisa;
import exceptions.VentaInvalidaException;
import interfaces.domain.IVenta;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class Venta implements IVenta, Comparable<Venta> {
    // Establece precio de IVA
    private static final double IVA = 0.19;

    private String id;
    private Usuario usuario;
    private Comic comic;
    private LocalDateTime fechaVenta;

    private Divisa precioFinal;

    public Venta(Usuario usuario, Comic comic) {
        if (usuario == null) {
            throw new VentaInvalidaException("El usuario no puede ser nulo");
        }
        if (comic == null) {
            throw new VentaInvalidaException("El cómic no puede ser nulo");
        }
        
        this.id = UUID.randomUUID().toString();
        this.usuario = usuario;
        this.comic = comic;
        this.fechaVenta = LocalDateTime.now();
    }

    @Override
    public String getId() { return id; }

    @Override
    public Usuario getUsuario() { return usuario; }

    @Override
    public Comic getComic() { return comic; }

    @Override
    public LocalDateTime getFechaVenta() { return fechaVenta; }

    @Override
    public Divisa getPrecioFinal() {
        // obtiene el precio de venta del comic
        Divisa precioVenta = this.comic.getPrecio();

        // obtiene el impuesto aplicado
        Divisa impuestoAplicado = calcularImpuesto();

        // retorna el precio final
        return precioVenta.sumar(impuestoAplicado);
    }

    private Divisa calcularImpuesto() {
        BigDecimal tasa_impuesto = new BigDecimal(IVA);
        BigDecimal impuesto = this.comic.getPrecio().getMonto().multiply(tasa_impuesto);

        return Divisa.pesos(impuesto);
    }

    @Override
    public int compareTo(Venta otra) {
        if (otra == null) return 1;
        
        int comparacionFecha = this.fechaVenta.compareTo(otra.fechaVenta);
        if (comparacionFecha != 0) {
            return comparacionFecha;
        }
        
        return this.id.compareTo(otra.id);
    }
}
