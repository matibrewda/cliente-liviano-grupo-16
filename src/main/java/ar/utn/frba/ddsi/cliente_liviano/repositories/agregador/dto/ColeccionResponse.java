package ar.utn.frba.ddsi.cliente_liviano.repositories.agregador.dto;

import ar.utn.frba.ddsi.cliente_liviano.DTO.ColeccionDTO;
import ar.utn.frba.ddsi.cliente_liviano.DTO.FiltroDto;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.Collections;
import java.util.List;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ColeccionResponse {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("titulo")
    private String titulo;

    @JsonProperty("descripcion")
    private String descripcion;

    @JsonProperty("algoritmoConsenso")
    private String tipoConsenso;

    @JsonProperty("fuentes")
    private List<FuenteItemResponse> fuentes;

    /** Criterio de pertenencia (filtros). Puede venir en GET /admin/colecciones/{id}. */
    @JsonProperty("filtros")
    private List<FiltroDto> filtros;

    @JsonProperty("criterioPertenencia")
    private List<FiltroDto> criterioPertenencia;

    public ColeccionDTO toColeccionDTO() {
        ColeccionDTO coleccionDTO = new ColeccionDTO();
        coleccionDTO.setHandle(this.id != null ? this.id.toString() : null);
        coleccionDTO.setTitulo(this.titulo);
        coleccionDTO.setDescripcion(this.descripcion);
        coleccionDTO.setTipoConsenso(this.tipoConsenso);
        List<FiltroDto> filtrosCriterio = this.filtros != null ? this.filtros : this.criterioPertenencia;
        coleccionDTO.setFiltrosCriterioPertenencia(filtrosCriterio != null ? filtrosCriterio : Collections.emptyList());
        List<String> tipos = this.fuentes != null
                ? this.fuentes.stream().map(FuenteItemResponse::getTipo).toList()
                : Collections.emptyList();
        coleccionDTO.setFuentesTipos(tipos);

        return coleccionDTO;
    }
}