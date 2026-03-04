package ar.utn.frba.ddsi.cliente_liviano.DTO;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class CategoriaDTO {
    public Long id;
    private String nombre;

    public CategoriaDTO(String nombre) {
        this.nombre = nombre;
    }
}