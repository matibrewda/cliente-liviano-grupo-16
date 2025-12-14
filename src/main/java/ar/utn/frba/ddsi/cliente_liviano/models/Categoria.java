package ar.utn.frba.ddsi.cliente_liviano.models;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Categoria {
    public Integer id;
    private String nombre;

    public Categoria() {}
    public Categoria(Integer id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }
}