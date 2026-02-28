package ar.utn.frba.ddsi.cliente_liviano.DTO;

import ar.utn.frba.ddsi.cliente_liviano.models.Categoria;
import ar.utn.frba.ddsi.cliente_liviano.models.Hecho;
import ar.utn.frba.ddsi.cliente_liviano.models.Origen;
import ar.utn.frba.ddsi.cliente_liviano.models.Ubicacion;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HechoDTO {

    private Long id;
    private String titulo;
    private String descripcion;
    private byte[] datosMultimedia;
    private CategoriaDTO categoriaDTO;
    private UbicacionDTO ubicacionDTO;
    private String origen;
    private LocalDate fechaAcontecimiento;
    private LocalDateTime fechaCarga;
    private Boolean borradoLogico;
    private LocalDateTime fechaBorradoLogico;
    private boolean visible;
    private boolean consenso;

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
    }

    public Hecho toHecho() {
        return new Hecho(
                this.getId(),
                this.getTitulo(),
                this.getDescripcion(),
                new Categoria(this.getCategoriaDTO().getId(),this.getCategoriaDTO().getNombre()),
                new Ubicacion(this.getUbicacionDTO().getLatitud(),this.getUbicacionDTO().getLongitud()),
                this.getFechaAcontecimiento(),
                this.getFechaCarga()
        );
    }
}