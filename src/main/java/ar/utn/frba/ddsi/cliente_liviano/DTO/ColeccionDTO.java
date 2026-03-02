package ar.utn.frba.ddsi.cliente_liviano.DTO;

import ar.utn.frba.ddsi.cliente_liviano.models.Coleccion;
import ar.utn.frba.ddsi.cliente_liviano.models.Hecho;
import ar.utn.frba.ddsi.cliente_liviano.models.interfaces.IConsensoStrategy;
import ar.utn.frba.ddsi.cliente_liviano.models.interfaces.ICriterioDePertenencia;
import ar.utn.frba.ddsi.cliente_liviano.models.interfaces.IFuenteDeDatos;
import lombok.Getter;
import lombok.Setter;


import java.util.List;
@Setter
@Getter
public class ColeccionDTO {
    private String handle;
    private String titulo;
    private String descripcion;
    private ICriterioDePertenencia criterioPertenencia;
    private List<IFuenteDeDatos> fuenteDeDatos;
    private IConsensoStrategy algoritmoDeConsenso;
    private String tipoConsenso;
    private List<Hecho> hechos;
}
