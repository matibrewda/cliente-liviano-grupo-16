package ar.utn.frba.ddsi.cliente_liviano.models.dto;

import lombok.*;

@Data
@NoArgsConstructor
@Getter
@Setter
public class LoginRequestDTO {
    private String username;
    private String password;
}
