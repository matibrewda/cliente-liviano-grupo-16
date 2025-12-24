package ar.utn.frba.ddsi.cliente_liviano.models.dto;

import ar.utn.frba.ddsi.cliente_liviano.models.usuario.Usuario;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class LoginRequestDTO {
    private String username;
    private String password;

    public Usuario toUser() {
        return new Usuario(
                this.username,
                this.password,
                "",
                ""
        );
    }
}
