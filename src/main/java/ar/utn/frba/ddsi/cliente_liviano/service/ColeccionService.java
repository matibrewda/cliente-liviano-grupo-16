package ar.utn.frba.ddsi.cliente_liviano.service;

import ar.utn.frba.ddsi.cliente_liviano.DTO.CategoriaDTO;
import ar.utn.frba.ddsi.cliente_liviano.DTO.HechoDTO;

import ar.utn.frba.ddsi.cliente_liviano.DTO.OrigenDTO;
import ar.utn.frba.ddsi.cliente_liviano.DTO.UbicacionDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ColeccionService {

    private final RestTemplate restTemplate = new RestTemplate();

    //TODO: Probar + Agregar en properties
    private final String BASE_URL = "http://localhost:8080/api/colecciones";
    private final String URL_HECHOS = BASE_URL + "/{coleccionId}/hechos";

    public List<HechoDTO> obtenerHechosPorColeccion(String coleccionId,
                                                    String fechaDesde,
                                                    String fechaHasta,
                                                    String ubicacion,
                                                    String categoria,
                                                    String origen) {

        UriComponentsBuilder builder = UriComponentsBuilder
                .fromUriString(BASE_URL)
                .path("/{coleccionId}/hechos");

        if (fechaDesde != null && !fechaDesde.isEmpty())
            builder.queryParam("fechaDesde", fechaDesde);
        if (fechaHasta != null && !fechaHasta.isEmpty())
            builder.queryParam("fechaHasta", fechaHasta);
        if (ubicacion != null && !ubicacion.isEmpty())
            builder.queryParam("ubicacion", ubicacion);
        if (categoria != null && !categoria.isEmpty())
            builder.queryParam("categoria", categoria);
        if (origen != null && !origen.isEmpty())
            builder.queryParam("origen", origen);

        URI uri = builder.build(coleccionId);

        try {
            ResponseEntity<HechoDTO[]> response =
                    restTemplate.getForEntity(uri, HechoDTO[].class);
            return List.of(response.getBody());
        } catch (Exception e) {
            //Creo coleccion de prueba
            List<HechoDTO> hechos = new ArrayList<>();

            var hechoDTO = new HechoDTO();
            hechoDTO.setId(1L);
            hechoDTO.setCategoriaDTO(new CategoriaDTO("Categoria-1"));
            hechoDTO.setTitulo("Hechos");
            hechoDTO.setDescripcion("Ejemplo");
            hechoDTO.setUbicacionDTO(new UbicacionDTO(-34.603734,-58.381570));
            hechoDTO.setFechaAcontecimiento(LocalDate.of(2025, 3, 12));
            hechoDTO.setFechaCarga(LocalDateTime.now());
            hechoDTO.setOrigenDTO(new OrigenDTO("Estatica"));


            var hechoDTO2 = new HechoDTO();
            hechoDTO2.setId(5L);
            hechoDTO2.setCategoriaDTO(new CategoriaDTO("Categoria-2"));
            hechoDTO2.setTitulo("Corte de tránsito en Puerto Madero");
            hechoDTO2.setDescripcion("Manifestación pacífica que afecta el tránsito en Av. Alicia Moreau de Justo.");
            hechoDTO2.setUbicacionDTO(new UbicacionDTO(-34.608300, -58.362400)); // Puerto Madero
            hechoDTO2.setFechaAcontecimiento(LocalDate.of(2025, 3, 15));
            hechoDTO2.setFechaCarga(LocalDateTime.now());
            hechoDTO2.setOrigenDTO(new OrigenDTO("Dinamica"));

            hechos.add(hechoDTO);
            hechos.add(hechoDTO2);
            return hechos.stream().filter(h ->
                            fechaDesde == null || fechaDesde.isBlank() ||
                                    !h.getFechaAcontecimiento().isBefore(LocalDate.parse(fechaDesde))
                    )
                    .filter(h ->
                            fechaHasta == null || fechaHasta.isBlank() ||
                                    !h.getFechaAcontecimiento().isAfter(LocalDate.parse(fechaHasta))
                    )
                    .filter(h ->
                            categoria == null || categoria.isBlank() ||
                                    (h.getCategoriaDTO() != null &&
                                            categoria.equalsIgnoreCase(h.getCategoriaDTO().getNombre()))
                    )
                    .filter(h ->
                            origen == null || origen.isBlank() ||
                                    (h.getOrigenDTO() != null &&
                                            origen.equalsIgnoreCase(h.getOrigenDTO().getDescripcion()))
                    )
                    .toList();


        }
    }
}