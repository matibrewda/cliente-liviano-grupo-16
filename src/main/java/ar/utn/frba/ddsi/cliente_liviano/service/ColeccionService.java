package ar.utn.frba.ddsi.cliente_liviano.service;

import ar.utn.frba.ddsi.cliente_liviano.DTO.ColeccionDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class ColeccionService {

    private final RestTemplate restTemplate = new RestTemplate();

    //TODO: Probar + Agregar en properties
    private final String BASE_URL = "http://agregador:8080/api/colecciones";


    public List<ColeccionDTO> obtenerColecciones(){
        try {
            ResponseEntity<ColeccionDTO[]> response = this.restTemplate.getForEntity(BASE_URL, ColeccionDTO[].class);
            return List.of(response.getBody());
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


}
