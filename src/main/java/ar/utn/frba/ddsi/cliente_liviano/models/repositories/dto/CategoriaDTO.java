package ar.utn.frba.ddsi.cliente_liviano.models.repositories.dto;

import ar.utn.frba.ddsi.cliente_liviano.models.Categoria;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class CategoriaDTO {
    @JsonProperty("id")
    private Long id;

    @JsonProperty("nombre")
    private String nombre;

    public Categoria toCategoria() {
        return new Categoria(this.id, this.nombre);
    }
}
