package ar.utn.frba.ddsi.cliente_liviano.service;

import ar.utn.frba.ddsi.cliente_liviano.DTO.HechoDTO;
import ar.utn.frba.ddsi.cliente_liviano.repositories.agregador.ColeccionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;


@Service
public class ColeccionService {
    @Autowired
    private final ColeccionRepository coleccionRepository;

    public ColeccionService(ColeccionRepository coleccionRepository) {
        this.coleccionRepository = coleccionRepository;
    }
    public HechoDTO obtenerHechoPorColeccionId(String coleccionId, String hechoId) {

        String path = UriComponentsBuilder
                .fromPath("/colecciones/{coleccionId}/hechos/{hechoId}")
                .buildAndExpand(coleccionId, hechoId)
                .toUriString();

        System.out.println(path);

        return coleccionRepository.obtenerHechoDeColeccion(path);
    }
}
