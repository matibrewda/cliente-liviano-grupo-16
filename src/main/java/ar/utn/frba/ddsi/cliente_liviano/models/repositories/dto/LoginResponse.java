package ar.utn.frba.ddsi.cliente_liviano.models.repositories.dto;

import ar.utn.frba.ddsi.cliente_liviano.models.usuario.Usuario;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class LoginResponse {
    private String nombreReal;
    private String token;
    private List<String> roles;
}
