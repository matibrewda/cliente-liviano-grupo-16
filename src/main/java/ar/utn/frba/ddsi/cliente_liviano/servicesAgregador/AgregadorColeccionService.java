package ar.utn.frba.ddsi.cliente_liviano.servicesAgregador;

import ar.utn.frba.ddsi.cliente_liviano.DTO.ColeccionDTO;
import ar.utn.frba.ddsi.cliente_liviano.DTO.FiltroDto;
import ar.utn.frba.ddsi.cliente_liviano.DTO.FuentesConfigDTO;
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

    private static final String TIPO_PROXY = "proxy";
    private static final String TIPO_ESTATICA = "estatica";
    private static final String TIPO_DINAMICA = "dinamica";

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

            String[] parts = ubicacion.split(";");

            if (parts.length >= 2) {

                String lat = parts[0].trim().replace(',', '.');
                String lon = parts[1].trim().replace(',', '.');

                builder.queryParam("zona.latitud", lat);
                builder.queryParam("zona.longitud", lon);
            }

            if (parts.length >= 3) {

                String radio = parts[2].trim().replace(',', '.');

                builder.queryParam("zona.radio", radio);
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

    public ColeccionDTO obtenerColeccionPorId(Long id){
        return coleccionRepository.findById(id);
    }

    public ColeccionDTO actualizarColeccion(Long id, ColeccionInputDTO coleccionInputDTO) {
        return coleccionRepository.actualizarColeccion(id, coleccionInputDTO);
    }

    public void eliminar(Long id) {
        coleccionRepository.eliminar(id);
    }

    public void actualizarConsenso(String coleccionId, String tipoConsenso) {
        coleccionRepository.actualizarConsenso(coleccionId, tipoConsenso);
    }

    public FuentesConfigDTO obtenerFuentes(String coleccionId) {
        Long id = Long.parseLong(coleccionId);
        ColeccionDTO coleccion = coleccionRepository.findById(id);
        List<String> tipos = coleccion.getFuentesTipos() != null ? coleccion.getFuentesTipos() : List.of();
        return new FuentesConfigDTO(
                tipos.contains(TIPO_PROXY),
                tipos.contains(TIPO_ESTATICA),
                tipos.contains(TIPO_DINAMICA)
        );
    }

    public void actualizarFuentes(String coleccionId, boolean fuenteProxy, boolean fuenteEstatica, boolean fuenteDinamica) {
        Long id = Long.parseLong(coleccionId);
        ColeccionDTO coleccion = coleccionRepository.findById(id);
        List<String> actuales = coleccion.getFuentesTipos() != null ? coleccion.getFuentesTipos() : List.of();
        if (fuenteProxy && !actuales.contains(TIPO_PROXY)) {
            coleccionRepository.agregarFuente(id, TIPO_PROXY);
        } else if (!fuenteProxy && actuales.contains(TIPO_PROXY)) {
            coleccionRepository.quitarFuente(id, TIPO_PROXY);
        }
        if (fuenteEstatica && !actuales.contains(TIPO_ESTATICA)) {
            coleccionRepository.agregarFuente(id, TIPO_ESTATICA);
        } else if (!fuenteEstatica && actuales.contains(TIPO_ESTATICA)) {
            coleccionRepository.quitarFuente(id, TIPO_ESTATICA);
        }
        if (fuenteDinamica && !actuales.contains(TIPO_DINAMICA)) {
            coleccionRepository.agregarFuente(id, TIPO_DINAMICA);
        } else if (!fuenteDinamica && actuales.contains(TIPO_DINAMICA)) {
            coleccionRepository.quitarFuente(id, TIPO_DINAMICA);
        }
    }

    public List<FiltroDto> obtenerCriterioPertenencia(Long id) {
        return coleccionRepository.getCriterioPertenencia(id);
    }

    public void actualizarCriterioPertenencia(Long id, List<FiltroDto> filtros) {
        ColeccionDTO actual = coleccionRepository.findById(id);
        ColeccionInputDTO input = new ColeccionInputDTO();
        input.setTitulo(actual.getTitulo() != null ? actual.getTitulo() : "");
        input.setDescripcion(actual.getDescripcion() != null ? actual.getDescripcion() : "");
        input.setFiltros(filtros != null ? filtros : List.of());
        coleccionRepository.actualizarColeccion(id, input);
    }
}
