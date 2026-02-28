package ar.utn.frba.ddsi.cliente_liviano.repositories.agregador;


import ar.utn.frba.ddsi.cliente_liviano.DTO.HechoDTO;
import ar.utn.frba.ddsi.cliente_liviano.repositories.agregador.dto.HechoResponse;
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
public class AgregadorHechoRepository {

    private final HttpClient client;
    private final ObjectMapper mapper;

    @Value("${agregador.base-url}")
    private String baseURL;

    public AgregadorHechoRepository(ObjectMapper objectMapper) {
        this.mapper = objectMapper;
        this.client = HttpClient.newHttpClient();
    }

    public List<HechoDTO> obtenerTodosLosHechos() {

        URI uri = URI.create(baseURL + "/hechos");

        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .header("Content-Type", "application/json")
                .GET()
                .build();
        try {
            HttpResponse<String> response =
                    client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() / 100 != 2) {
                throw new RuntimeException(
                        "Error llamando al agregador: " +
                                response.statusCode() + " - " + response.body()
                );
            }

            HechoResponse[] hechosResponse =
                    mapper.readValue(response.body(), HechoResponse[].class);

            System.out.println(Arrays.toString(hechosResponse));

            return Arrays.stream(hechosResponse)
                    .map(HechoResponse::toHechoDTO)
                    .toList();

        } catch (IOException e) {
            throw new RuntimeException("Error parseando respuesta de hechos", e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Request interrumpida al consultar agregador", e);
        }
    }

    public HechoDTO getHechoById(Long id){

        URI uri = URI.create(baseURL + "/hechos/" + id);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .header("Content-Type", "application/json")
                .GET()
                .build();
        try {
            HttpResponse<String> response =
                    client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() / 100 != 2) {
                throw new RuntimeException(
                        "Error llamando al agregador: " +
                                response.statusCode() + " - " + response.body()
                );
            }

            HechoResponse hechoResponse = mapper.readValue(response.body(), HechoResponse.class);

            return hechoResponse.toHechoDTO();

        } catch (IOException e) {
            throw new RuntimeException("Error parseando respuesta de hechos", e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Request interrumpida al consultar agregador", e);
        }
    }

}

