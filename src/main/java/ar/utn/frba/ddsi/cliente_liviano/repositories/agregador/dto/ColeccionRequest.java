package ar.utn.frba.ddsi.cliente_liviano.repositories.agregador.dto;

import ar.utn.frba.ddsi.cliente_liviano.DTO.ColeccionDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ColeccionRequest {


    @JsonProperty("titulo")
    private String titulo;

    @JsonProperty("descripcion")
    private String descripcion;


    public ColeccionDTO toColeccionDTO() {
        ColeccionDTO coleccionDTO = new ColeccionDTO();
        coleccionDTO.setTitulo(this.titulo);
        coleccionDTO.setDescripcion(this.descripcion);

        return coleccionDTO;
    }
}