package ar.utn.frba.ddsi.cliente_liviano.service;

import ar.utn.frba.ddsi.cliente_liviano.DTO.CategoriaDTO;
import ar.utn.frba.ddsi.cliente_liviano.DTO.HechoDTO;

import ar.utn.frba.ddsi.cliente_liviano.DTO.UbicacionDTO;
import ar.utn.frba.ddsi.cliente_liviano.repositories.agregador.ColeccionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ColeccionService {
    @Autowired
    private final ColeccionRepository coleccionRepository;

    public ColeccionService(ColeccionRepository coleccionRepository) {
        this.coleccionRepository = coleccionRepository;
    }

    public List<HechoDTO> obtenerHechosPorColeccion(String coleccionId,
                                                    String fechaDesde,
                                                    String fechaHasta,
                                                    String ubicacion,
                                                    String codCategoria,
                                                    String origen,
                                                    String modoNavegacion) { // equivalente a curado

        UriComponentsBuilder builder =
                UriComponentsBuilder.fromPath("/colecciones/{coleccionId}/hechos");

        if (fechaDesde != null && !fechaDesde.isBlank())
            builder.queryParam("fechaAcontecimientoDesde", agregarHoraInicio(fechaDesde));

        if (fechaHasta != null && !fechaHasta.isBlank())
            builder.queryParam("fechaAcontecimientoHasta", agregarhoraFin(fechaHasta));

        if (codCategoria != null && !codCategoria.isBlank())
            builder.queryParam("codigoCategoria", codCategoria);

        // ubicacion = "lat,long,radio"
        if (ubicacion != null && !ubicacion.isBlank()) {
            String[] parts = ubicacion.split(",");

            if (parts.length >= 2) {
                builder.queryParam("zona.latitud", parts[0].trim());
                builder.queryParam("zona.longitud", parts[1].trim());
            }
            if (parts.length >= 3) {
                builder.queryParam("zona.radio", parts[2].trim());
            }
        }

        // "CURADO" -> true, "IRRESTRICTO" -> false
        if (modoNavegacion != null && !modoNavegacion.isBlank()) {
            boolean curado = Boolean.parseBoolean(modoNavegacion);
            builder.queryParam("curado", curado);
        }
        if (origen != null && !origen.isBlank())
            builder.queryParam("origen", origen);

        String path = builder
                .buildAndExpand(coleccionId)
                .toUriString();

        return coleccionRepository.obtenerHechosPorColeccion(path);
    }

    private String agregarHoraInicio(String fecha) {
        if(fecha == null) {
            return null;
        }
        return fecha + "T00:00:00";
    }

    private String agregarhoraFin(String fecha) {
        if(fecha == null) {
            return null;
        }
        return fecha + "T23:59:59";
    }

}