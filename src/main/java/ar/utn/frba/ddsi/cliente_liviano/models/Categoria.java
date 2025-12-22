package ar.utn.frba.ddsi.cliente_liviano.models;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Categoria {
    public Long id;
    private String nombre;

    public Categoria() {}
    public Categoria(Long id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }
}