package ar.utn.frba.ddsi.cliente_liviano.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FuentesConfigDTO {
    private boolean fuenteProxy;
    private boolean fuenteEstatica;
    private boolean fuenteDinamica;
}
