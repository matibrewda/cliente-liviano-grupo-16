package ar.utn.frba.ddsi.cliente_liviano.models;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Categoria {
    public Integer id;
    private String nombre;

    public Categoria() {}
    public Categoria(String nombre) {
        this.nombre = nombre;
    }
}