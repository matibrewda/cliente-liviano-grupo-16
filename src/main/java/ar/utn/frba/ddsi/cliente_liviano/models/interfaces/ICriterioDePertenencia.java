package ar.utn.frba.ddsi.cliente_liviano.models.interfaces;

import ar.utn.frba.ddsi.cliente_liviano.models.Hecho;

public interface ICriterioDePertenencia {
    boolean cumple(Hecho h);
}
