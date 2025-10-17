package ar.utn.frba.ddsi.cliente_liviano.models;

import ar.utn.frba.ddsi.cliente_liviano.models.interfaces.IConsensoStrategy;
import ar.utn.frba.ddsi.cliente_liviano.models.interfaces.ICriterioDePertenencia;
import ar.utn.frba.ddsi.cliente_liviano.models.interfaces.IFuenteDeDatos;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

public class Coleccion {
    private String handle;
    private String titulo;
    private String descripcion;
    private ICriterioDePertenencia criterioPertenencia;
    private List<IFuenteDeDatos> fuenteDeDatos;
    private IConsensoStrategy algoritmoDeConsenso = new ConsensoDefault();

    @Getter
    private List<Hecho> hechos;

    public String getId() {
        return this.handle;
    }

    public Coleccion(ICriterioDePertenencia criterioPertenencia, List<Hecho> hechos, List<IFuenteDeDatos> fuenteDeDatos) {
        this.criterioPertenencia = criterioPertenencia;
        this.fuenteDeDatos = fuenteDeDatos;
        this.handle = UUID.randomUUID().toString();
        this.hechos = hechos.stream().filter(h -> this.criterioPertenencia.cumple(h)).toList();
    }

    public void agregarFuente(IFuenteDeDatos fuente){
        this.fuenteDeDatos.add(fuente);
    }

    public void quitarFuente(IFuenteDeDatos fuente){
        this.fuenteDeDatos.remove(fuente);
    }

    public void setAlgoritmoDeConsenso(IConsensoStrategy algoritmoDeConsenso) {
        this.algoritmoDeConsenso = algoritmoDeConsenso;
    }
}
