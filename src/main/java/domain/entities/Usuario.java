package domain.entities;

import domain.valueobjects.Email;
import exceptions.NombreInvalidoException;
import exceptions.ApellidoInvalidoException;
import interfaces.domain.IUsuario;

public class Usuario implements IUsuario {
    private int id;
    private String nombre;
    private String apellido;
    private Email email;

    public Usuario( String nombre, String apellido, String email ) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new NombreInvalidoException("El nombre no puede ser nulo o vacío");
        }
        if (apellido == null || apellido.trim().isEmpty()) {
            throw new ApellidoInvalidoException("El apellido no puede ser nulo o vacío");
        }
        
        this.nombre = nombre.trim();
        this.apellido = apellido.trim();
        this.email = new Email(email);
    }

    public int getId() { return id; }
    
    public void setId(int id) { this.id = id; }

    @Override
    public String getNombre() { return nombre; }

    @Override
    public String getNombreCompleto() {
        return String.format( "%s %s", nombre, apellido );
    }

    @Override
    public String getApellido() { return apellido; }

    @Override
    public String getEmail() { return email.getValue(); }

    @Override
    public void setEmail( String email ) { this.email = new Email(email); }
}
