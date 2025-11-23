package ar.utn.frba.ddsi.cliente_liviano.DTO;

import ar.utn.frba.ddsi.cliente_liviano.models.Usuario;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrigenDTO {
    private String descripcion;
    //Corroborar si es necesario
    //private Usuario contribuyente;
}
