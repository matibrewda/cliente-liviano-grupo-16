package ar.utn.frba.ddsi.cliente_liviano.models.repositories.dto;

import ar.utn.frba.ddsi.cliente_liviano.models.usuario.Usuario;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginResponse {
    private String nombreReal;
    private String token;

    public Usuario toUsuario(String username) {
        return new Usuario(username, "", nombreReal, token);
    }
}
