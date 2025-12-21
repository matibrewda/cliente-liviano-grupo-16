package ar.utn.frba.ddsi.cliente_liviano.repositories.agregador;

import ar.utn.frba.ddsi.cliente_liviano.DTO.ColeccionDTO;
import ar.utn.frba.ddsi.cliente_liviano.repositories.agregador.dto.ColeccionResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Arrays;
import java.util.List;


@Repository
public class ColeccionRepository {

    private final HttpClient client;
    private final ObjectMapper mapper;

    @Value("${agregador.base-url}")
    private String baseURL;

    public ColeccionRepository(ObjectMapper objectMapper) {
        this.mapper = objectMapper;
        this.client = HttpClient.newHttpClient();
    }


    public List<ColeccionDTO> findAll() {

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseURL + "/colecciones/"))
                .header("Content-Type", "application/json")
                .GET()
                .build();

        try {
            HttpResponse<String> response =
                    client.send(request, HttpResponse.BodyHandlers.ofString());

            ColeccionResponse[] coleccionesResponse =
                    mapper.readValue(response.body(), ColeccionResponse[].class);
            System.out.println("Colecciones: " + Arrays.stream(coleccionesResponse).toList());
            return Arrays.stream(coleccionesResponse)
                    .map(ColeccionResponse::toColeccionDTO)
                    .toList();

        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Error al obtener colecciones desde el agregador", e);
        }
    }
}
