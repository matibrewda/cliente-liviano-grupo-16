package ar.utn.frba.ddsi.cliente_liviano.DTO;

import ar.utn.frba.ddsi.cliente_liviano.models.Categoria;
import ar.utn.frba.ddsi.cliente_liviano.models.Origen;
import ar.utn.frba.ddsi.cliente_liviano.models.Ubicacion;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class HechoDTO {

    private Long id;
    private String titulo;
    private String descripcion;
    private byte[] datosMultimedia;
    private CategoriaDTO categoriaDTO;
    private UbicacionDTO ubicacionDTO;
    private OrigenDTO origenDTO;
    private LocalDate fechaAcontecimiento;
    private LocalDateTime fechaCarga;
    private Boolean borradoLogico;
    private LocalDateTime fechaBorradoLogico;
    private String modoNavegacion;

    public HechoDTO(){}
    public HechoDTO(String titulo,
                 String descripcion,
                 CategoriaDTO categoria,
                 UbicacionDTO ubicacion,
                 LocalDate fechaAcontecimiento,
                 LocalDateTime fechaCarga) {

        this.titulo = titulo;
        this.descripcion = descripcion;
        this.categoriaDTO = categoria;
        this.ubicacionDTO = ubicacion;
        this.fechaAcontecimiento = fechaAcontecimiento;
        this.fechaCarga = fechaCarga;
        this.borradoLogico = false;
    }

}