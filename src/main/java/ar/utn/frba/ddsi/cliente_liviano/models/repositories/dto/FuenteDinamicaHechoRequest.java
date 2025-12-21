package ar.utn.frba.ddsi.cliente_liviano.models.repositories.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class FuenteDinamicaHechoRequest {

    @JsonProperty("titulo")
    private String titulo;

    @JsonProperty("descripcion")
    private String descripcion;

    @JsonProperty("categoria")
    private Integer categoria;

    @JsonProperty("ubicacionLatitud")
    private double ubicacionLatitud;

    @JsonProperty("ubicacionLongitud")
    private double ubicacionLongitud;

    @JsonProperty("fechaAcontecimiento")
    private LocalDate fechaAcontecimiento;


    public FuenteDinamicaHechoRequest(
            String titulo,
            String descripcion,
            Integer Categoria,
            Double Latitud,
            Double Longitud,
            LocalDate fechaAcontecimiento) {

        this.titulo = titulo;
        this.descripcion = descripcion;
        this.categoria = Categoria;
        this.ubicacionLatitud = Latitud;
        this.ubicacionLongitud = Longitud;
        this.fechaAcontecimiento = fechaAcontecimiento;
    }
}
