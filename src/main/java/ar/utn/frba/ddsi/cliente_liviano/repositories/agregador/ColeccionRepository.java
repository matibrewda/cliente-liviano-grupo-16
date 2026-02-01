package ar.utn.frba.ddsi.cliente_liviano.repositories.agregador;


import ar.utn.frba.ddsi.cliente_liviano.DTO.ColeccionDTO;
import ar.utn.frba.ddsi.cliente_liviano.DTO.input.ColeccionInputDTO;
import ar.utn.frba.ddsi.cliente_liviano.DTO.HechoDTO;
import ar.utn.frba.ddsi.cliente_liviano.repositories.agregador.dto.ColeccionResponse;
import ar.utn.frba.ddsi.cliente_liviano.repositories.agregador.dto.ColeccionRequest;
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
public class ColeccionRepository {

    private final HttpClient client;
    private final ObjectMapper mapper;

    @Value("${agregador.base-url}")
    private String baseURL;

    public ColeccionRepository(ObjectMapper objectMapper) {
        this.mapper = objectMapper;
        this.client = HttpClient.newHttpClient();
    }

    public HechoDTO obtenerHechoDeColeccion(String path) {
        URI uri = URI.create(baseURL + path);

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

            HechoResponse hechosResponse =
                    mapper.readValue(response.body(), HechoResponse.class);

            System.out.println(hechosResponse);

            return hechosResponse.toHechoDTO();


        } catch (IOException e) {
            throw new RuntimeException("Error parseando respuesta de hechos", e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Request interrumpida al consultar agregador", e);
        }
    }

    public List<HechoDTO> obtenerHechosPorColeccion(String path){

        URI uri = URI.create(baseURL + path);

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


    public ColeccionDTO save(ColeccionInputDTO coleccionNueva) {
        ColeccionDTO coleccion;

        String jsonBody;
        try {
            jsonBody = mapper.writeValueAsString(new ColeccionRequest(
                    coleccionNueva.getTitulo(),
                    coleccionNueva.getDescripcion()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(this.baseURL + "/admin/colecciones"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();

        try {
            HttpResponse<String> response =client.send(request, HttpResponse.BodyHandlers.ofString());

            ColeccionResponse coleccionResponse =mapper.readValue(response.body(), ColeccionResponse.class);

            coleccion = coleccionResponse.toColeccionDTO();

        } catch (InterruptedException | IOException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Request was interrupted", e);
        }

        return coleccion;
    }

}
