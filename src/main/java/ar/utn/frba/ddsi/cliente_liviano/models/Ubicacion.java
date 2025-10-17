package ar.utn.frba.ddsi.cliente_liviano.models;

import lombok.Data;

@Data
public class Ubicacion {
    private double latitud;
    private double longitud;

    public Ubicacion() {}

    public Ubicacion(double latitud, double longitud) {
        this.latitud = latitud;
        this.longitud = longitud;
    }
}
