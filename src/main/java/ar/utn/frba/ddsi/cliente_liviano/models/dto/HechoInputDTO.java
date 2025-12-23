package ar.utn.frba.ddsi.cliente_liviano.models.dto;

import ar.utn.frba.ddsi.cliente_liviano.models.Categoria;
import ar.utn.frba.ddsi.cliente_liviano.models.Hecho;
import ar.utn.frba.ddsi.cliente_liviano.models.Origen;
import ar.utn.frba.ddsi.cliente_liviano.models.Ubicacion;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class HechoInputDTO {
    private Long id;
    private String titulo;
    private String descripcion;
    private byte[] datosMultimedia;
    private Categoria categoria;
    private Ubicacion ubicacion;
    private Origen origen;
    private LocalDate fechaAcontecimiento;

    public Hecho ToDomain () {
        return new Hecho(
                this.id,
                this.titulo,
                this.descripcion,
                this.categoria,
                this.ubicacion,
                this.fechaAcontecimiento,
                LocalDateTime.now());
    }
}
