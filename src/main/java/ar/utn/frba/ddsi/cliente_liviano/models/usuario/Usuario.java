package ar.utn.frba.ddsi.cliente_liviano.models.usuario;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public class Usuario {
    private String username;
    private String password;
    private String nombre;
    private String token;
    private Rol rol;
    private List<Permiso> permisos = new ArrayList<Permiso>();

    public Usuario() {}

    public Usuario(String username, String password, String nombre, String token) {
        this.username = username;
        this.password = password;
        this.nombre = nombre;
        this.token = token;
    }

    public void agregarPermisos(Permiso p) {
        this.permisos.add(p);
    }
}
