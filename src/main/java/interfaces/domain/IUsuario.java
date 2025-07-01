package interfaces.domain;

public interface IUsuario {
    String getNombre();
    String getApellido();
    String getEmail();
    String getNombreCompleto();
    void setEmail(String email);
}
