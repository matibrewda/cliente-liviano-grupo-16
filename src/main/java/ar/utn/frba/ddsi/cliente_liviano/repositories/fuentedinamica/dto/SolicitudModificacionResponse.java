package ar.utn.frba.ddsi.cliente_liviano.repositories.fuentedinamica.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class SolicitudModificacionResponse {
    private Long idSolicitud;
    private Long idHecho;
    /** Título solicitado (nuevo). La API puede enviarlo como tituloHechoSolicitado, titulo, tituloSolicitado, etc. */
    @JsonAlias({"tituloHechoSolicitado", "tituloSolicitado", "tituloPropuesto", "tituloNuevo"})
    private String titulo;
    /** Título actual del hecho (antes del cambio). */
    @JsonAlias({"tituloHechoActual", "tituloActual"})
    private String tituloHecho;
    /** Descripción solicitada (nueva). */
    @JsonAlias({"descripcionSolicitada", "descripcionPropuesta", "descripcionNueva"})
    private String descripcion;
    @JsonAlias({"categoriaSolicitada", "categoriaPropuesta"})
    private String categoriaNombre;
    private Double latitud;
    private Double longitud;
    /** Fecha acontecimiento solicitada (puede venir como fechaAcontecimiento o fechaAcontecimientoSolicitada). */
    @JsonAlias({"fechaAcontecimientoSolicitada", "fechaAcontecimientoPropuesta"})
    private String fechaAcontecimiento;
    private String motivo;
    private String estado;

    @JsonProperty("ubicacion")
    public void setUbicacionFromJson(UbicacionDto u) {
        if (u != null) {
            this.latitud = u.getLatitud();
            this.longitud = u.getLongitud();
        }
    }

    /** Si la API devuelve los datos solicitados en un objeto anidado (solicitud, cambios, datosSolicitados). */
    @JsonProperty("solicitud")
    public void setSolicitudFromJson(DatosSolicitudDto d) {
        if (d != null) {
            if (d.getTitulo() != null) this.titulo = d.getTitulo();
            if (d.getDescripcion() != null) this.descripcion = d.getDescripcion();
            if (d.getCategoriaNombre() != null) this.categoriaNombre = d.getCategoriaNombre();
            if (d.getFechaAcontecimiento() != null) this.fechaAcontecimiento = d.getFechaAcontecimiento();
            if (d.getUbicacion() != null) {
                this.latitud = d.getUbicacion().getLatitud();
                this.longitud = d.getUbicacion().getLongitud();
            }
        }
    }

    @JsonProperty("cambios")
    public void setCambiosFromJson(DatosSolicitudDto d) {
        setSolicitudFromJson(d);
    }

    @JsonProperty("datosSolicitados")
    public void setDatosSolicitadosFromJson(DatosSolicitudDto d) {
        setSolicitudFromJson(d);
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class UbicacionDto {
        private Double latitud;
        private Double longitud;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class DatosSolicitudDto {
        private String titulo;
        private String descripcion;
        private String categoriaNombre;
        private String fechaAcontecimiento;
        private UbicacionDto ubicacion;
    }
}
