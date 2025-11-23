package ar.utn.frba.ddsi.cliente_liviano.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CategoriaDTO {
    public Long id;
    private String nombre;

    public CategoriaDTO(String nombre) {
        this.nombre = nombre;
    }
}
