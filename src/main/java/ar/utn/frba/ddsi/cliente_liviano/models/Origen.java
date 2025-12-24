package ar.utn.frba.ddsi.cliente_liviano.models;

import ar.utn.frba.ddsi.cliente_liviano.models.usuario.Usuario;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class Origen {
    private String descripcion;
    private Usuario contribuyente;

    public Origen(){

    }

    public Origen(String descripcion, Usuario contribuyente){
        this.descripcion = descripcion;
        this.contribuyente = contribuyente;
    }

    public static Origen CrearCargaManual(){
        var origen = new Origen();
        origen.descripcion = "Carga manual";
        return origen;
    }
    public static Origen CrearProvistoPorContribuyente(Usuario contribuyente){
        var origen = new Origen();
        origen.descripcion = "Provisto por contribuyente";

        // Puede ser anónimo
        if(contribuyente != null){
            origen.contribuyente = contribuyente;
        }
        return origen;
    }
}