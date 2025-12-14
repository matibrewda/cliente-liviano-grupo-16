package ar.utn.frba.ddsi.cliente_liviano.models.usuario;


import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Usuario {
    private Long id;
    private String nombre;
    private String apellido;
    private String nombreDeUsuario;
    private String contrasenia;
    private Rol rol;
    private List<Permiso> permisos = new ArrayList<Permiso>();

    public void agregarPermisos(Permiso p) {
        this.permisos.add(p);
    }
}
