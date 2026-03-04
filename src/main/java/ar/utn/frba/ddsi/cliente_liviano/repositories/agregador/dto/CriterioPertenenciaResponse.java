package ar.utn.frba.ddsi.cliente_liviano.repositories.agregador.dto;

import ar.utn.frba.ddsi.cliente_liviano.DTO.FiltroDto;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.Collections;
import java.util.List;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class CriterioPertenenciaResponse {

    @JsonProperty("filtros")
    private List<FiltroDto> filtros;

    public List<FiltroDto> getFiltros() {
        return filtros != null ? filtros : Collections.emptyList();
    }
}
