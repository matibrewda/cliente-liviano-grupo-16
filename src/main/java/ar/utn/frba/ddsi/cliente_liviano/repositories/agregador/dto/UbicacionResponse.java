package ar.utn.frba.ddsi.cliente_liviano.repositories.agregador.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class UbicacionResponse {
    @JsonProperty("id")
    private Long id;
    @JsonProperty("latitud")
    private Double latitud;
    @JsonProperty("longitud")
    private Double longitud;
}