package ar.utn.frba.ddsi.cliente_liviano.servicesAgregador;

import ar.utn.frba.ddsi.cliente_liviano.DTO.ColeccionDTO;
import ar.utn.frba.ddsi.cliente_liviano.DTO.input.ColeccionInputDTO;
import ar.utn.frba.ddsi.cliente_liviano.DTO.HechoDTO;
import ar.utn.frba.ddsi.cliente_liviano.repositories.agregador.ColeccionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;

@Service
public class AgregadorColeccionService {
    @Autowired
    private final ColeccionRepository coleccionRepository;

    public AgregadorColeccionService(ColeccionRepository coleccionRepository) {
        this.coleccionRepository = coleccionRepository;
    }
    public List<ColeccionDTO> obtenerColecciones(){
        try {
            return coleccionRepository.findAll();
        }catch (Exception e){
            //Creo coleccion de prueba
            List <ColeccionDTO> colecciones = new ArrayList<>();
            var coleccionDTO = new ColeccionDTO();
            coleccionDTO.setHandle("AR-1011");
            coleccionDTO.setDescripcion("Por la noche del viernes ...");
            coleccionDTO.setTitulo("Incendio en Ezeiza");
            colecciones.add(coleccionDTO);
            return colecciones;
        }
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










    public HechoDTO obtenerHechoPorColeccionId(String coleccionId, String hechoId) {

        String path = UriComponentsBuilder
                .fromPath("/colecciones/{coleccionId}/hechos/{hechoId}")
                .buildAndExpand(coleccionId, hechoId)
                .toUriString();


        return coleccionRepository.obtenerHechoDeColeccion(path);
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


    public ColeccionDTO crear(ColeccionInputDTO coleccionInputDTO) {
        return this.coleccionRepository.save(coleccionInputDTO);
    }
}
