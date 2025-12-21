package ar.utn.frba.ddsi.cliente_liviano.repositories.agregador.dto;

import ar.utn.frba.ddsi.cliente_liviano.DTO.ColeccionDTO;
import ar.utn.frba.ddsi.cliente_liviano.models.Coleccion;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ColeccionResponse {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("titulo")
    private String titulo;

    @JsonProperty("descripcion")
    private String descripcion;


    public ColeccionDTO toColeccionDTO() {
        ColeccionDTO coleccionDTO = new ColeccionDTO();
        coleccionDTO.setHandle(this.id != null ? this.id.toString() : null);
        coleccionDTO.setTitulo(this.titulo);
        coleccionDTO.setDescripcion(this.descripcion);

        return coleccionDTO;
    }
}