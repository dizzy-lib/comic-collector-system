package domain.entities;

import domain.valueobjects.Divisa;
import exceptions.ComicInvalidoException;
import interfaces.domain.IComic;

import java.util.UUID;

public class Comic implements IComic {
    private String id;
    private String nombre;
    private String descripcion;
    private Divisa precio;

    public Comic(String nombre, String descripcion, Divisa precio) {
        // genera un uuid para el comic a agregar
        this.id = UUID.randomUUID().toString();

        this.nombre = validarNombre(nombre);
        this.descripcion = validarDescripcion(descripcion);
        this.precio = validarPrecio(precio);
    }
    
    private String validarNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new ComicInvalidoException("El nombre del cómic no puede ser nulo o vacío");
        }
        return nombre.trim();
    }
    
    private String validarDescripcion(String descripcion) {
        if (descripcion == null || descripcion.trim().isEmpty()) {
            throw new ComicInvalidoException("La descripción del cómic no puede ser nula o vacía");
        }
        return descripcion.trim();
    }
    
    private Divisa validarPrecio(Divisa precio) {
        if (precio == null) {
            throw new ComicInvalidoException("El precio del cómic no puede ser nulo");
        }
        return precio;
    }

    @Override
    public String getId() { return this.id; }

    @Override
    public String getNombre() { return this.nombre; }

    @Override
    public String getDescription() { return this.descripcion; }

    @Override
    public Divisa getPrecio() { return this.precio; }

    @Override
    public void setNombre(String nombre) {
        this.nombre = validarNombre(nombre);
    }

    @Override
    public void setDescription(String description) {
        this.descripcion = validarDescripcion(description);
    }

    @Override
    public void setPrecio(Divisa precio) {
        this.precio = validarPrecio(precio);
    }
}
