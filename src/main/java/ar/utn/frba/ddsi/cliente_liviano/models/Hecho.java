package ar.utn.frba.ddsi.cliente_liviano.models;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;
import java.time.LocalDateTime;


@Getter
public class Hecho {
    private Long id;
    @Setter
    private String titulo;
    @Setter
    private String descripcion;
    @Setter
    private byte[] datosMultimedia;
    @Setter
    private Categoria categoria;
    @Setter
    private Ubicacion ubicacion;
    @Setter
    private Origen origen;
    @Setter
    private LocalDate fechaAcontecimiento;
    @Setter
    private LocalDateTime fechaCarga;
    private Boolean borradoLogico;
    private LocalDateTime fechaBorradoLogico;

    public Hecho(){}
    public Hecho(String titulo,
                 String descripcion,
                 Categoria categoria,
                 Ubicacion ubicacion,
                 LocalDate fechaAcontecimiento,
                 LocalDateTime fechaCarga) {

        this.titulo = titulo;
        this.descripcion = descripcion;
        this.categoria = categoria;
        this.ubicacion = ubicacion;
        this.fechaAcontecimiento = fechaAcontecimiento;
        this.fechaCarga = fechaCarga;
        this.borradoLogico = false;
    }



}