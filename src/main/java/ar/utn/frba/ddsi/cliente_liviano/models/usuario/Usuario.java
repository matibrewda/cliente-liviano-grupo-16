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
    private List<String> roles;
}
