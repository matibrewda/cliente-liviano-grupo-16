package ar.utn.frba.ddsi.cliente_liviano.service;

import ar.utn.frba.ddsi.cliente_liviano.DTO.ColeccionDTO;
import ar.utn.frba.ddsi.cliente_liviano.DTO.HechoDTO;
import ar.utn.frba.ddsi.cliente_liviano.repositories.agregador.ColeccionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;

@Service
public class ColeccionService {
    @Autowired
    private final ColeccionRepository coleccionRepository;

    public ColeccionService(ColeccionRepository coleccionRepository) {
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




    public HechoDTO obtenerHechoPorColeccionId(String coleccionId, String hechoId) {

        String path = UriComponentsBuilder
                .fromPath("/colecciones/{coleccionId}/hechos/{hechoId}")
                .buildAndExpand(coleccionId, hechoId)
                .toUriString();


        return coleccionRepository.obtenerHechoDeColeccion(path);
    }
}
