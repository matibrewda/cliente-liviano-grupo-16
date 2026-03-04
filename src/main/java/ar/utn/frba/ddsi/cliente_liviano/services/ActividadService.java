package ar.utn.frba.ddsi.cliente_liviano.services;

import ar.utn.frba.ddsi.cliente_liviano.DTO.ActividadDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Service
public class ActividadService {

    @Value("${control-actividad.base-url}")
    private String baseUrl;

    private final RestTemplate restTemplate;

    public ActividadService(RestTemplateBuilder builder) {
        this.restTemplate = builder.build();
    }

    public List<ActividadDTO> obtenerActividades(
            LocalDateTime desde,
            LocalDateTime hasta
    ) {

        String url = UriComponentsBuilder
                .fromHttpUrl(baseUrl + "/actividades")
                .queryParam("fechaDesde", desde)
                .queryParam("fechaHasta", hasta)
                .toUriString();

        ResponseEntity<ActividadDTO[]> response =
                restTemplate.getForEntity(url, ActividadDTO[].class);

        return Arrays.asList(response.getBody());
    }
}