package ar.utn.frba.ddsi.cliente_liviano.models.repositories.dto;

import ar.utn.frba.ddsi.cliente_liviano.models.Categoria;
import ar.utn.frba.ddsi.cliente_liviano.models.Hecho;
import ar.utn.frba.ddsi.cliente_liviano.models.Ubicacion;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class FuenteDinamicaHechoResponse {
    @JsonProperty("id")
    private Long id;


    @JsonProperty("titulo")
    private String titulo;

    @JsonProperty("descripcion")
    private String descripcion;

    @JsonProperty("categoria")
    private Long categoria;

    @JsonProperty("ubicacionLatitud")
    private double ubicacionLatitud;

    @JsonProperty("ubicacionLongitud")
    private double ubicacionLongitud;

    @JsonProperty("fechaAcontecimiento")
    private LocalDate fechaAcontecimiento;

    @JsonProperty("multimediaUrl")
    private String multimediaUrl;

    public Hecho toHecho() {
        return new Hecho(
                this.getId(),
                this.getTitulo(),
                this.getDescripcion(),
                new Categoria(this.getCategoria(), ""),
                new Ubicacion(this.getUbicacionLatitud(), this.getUbicacionLongitud()),
                this.fechaAcontecimiento,
                this.fechaAcontecimiento.atStartOfDay(),
                this.getMultimediaUrl()
        );
    }
}
