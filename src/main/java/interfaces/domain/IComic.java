package interfaces.domain;

import domain.valueobjects.Divisa;

public interface IComic {
    String getId();
    String getNombre();
    String getDescription();
    Divisa getPrecio();
    void setNombre(String nombre);
    void setDescription(String description);
    void setPrecio(Divisa precio);
}