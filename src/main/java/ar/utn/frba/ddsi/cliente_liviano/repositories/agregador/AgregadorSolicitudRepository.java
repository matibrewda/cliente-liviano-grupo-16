package ar.utn.frba.ddsi.cliente_liviano.repositories.agregador;


import ar.utn.frba.ddsi.cliente_liviano.repositories.agregador.dto.ComentarioRequest;
import ar.utn.frba.ddsi.cliente_liviano.repositories.agregador.dto.SolicitudEliminacionRequest;
import ar.utn.frba.ddsi.cliente_liviano.repositories.agregador.dto.SolicitudResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
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
public class AgregadorSolicitudRepository {

    private final HttpClient client;
    private final ObjectMapper mapper;

    @Value("${agregador.base-url}")
    private String baseURL;

    public AgregadorSolicitudRepository(ObjectMapper objectMapper) {
        this.mapper = objectMapper;
        this.client = HttpClient.newHttpClient();
    }

    public void crearSolicitud(Long idHecho, String motivo) {

        if (idHecho == null) {
            throw new IllegalArgumentException("idHecho no puede ser null");
        }
        if (motivo == null || motivo.isBlank()) {
            throw new IllegalArgumentException("motivo no puede estar vacío");
        }

        URI uri = URI.create(baseURL + "/solicitudes"); // ajustá si tu endpoint real es otro
        String jsonBody;
        try {
            jsonBody = mapper.writeValueAsString(new SolicitudEliminacionRequest(idHecho, motivo));
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error serializando body de solicitud", e);
        }

        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() / 100 != 2) {
                throw new RuntimeException(
                        "Error llamando al agregador (crear solicitud): " +
                                response.statusCode() + " - " + response.body()
                );
            }


        } catch (IOException e) {
            throw new RuntimeException("Error llamando al agregador (IO)", e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Request interrumpida al llamar agregador", e);
        }
    }

    public List<SolicitudResponse> obtenerSolicitudes() {
        URI uri = URI.create(baseURL + "/solicitudes");

        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .header("Content-Type", "application/json")
                .GET()
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() / 100 != 2) {
                throw new RuntimeException(
                        "Error llamando al agregador (obtener solicitudes): " +
                                response.statusCode() + " - " + response.body()
                );
            }

            SolicitudResponse[] solicitudes = mapper.readValue(response.body(), SolicitudResponse[].class);
            return Arrays.asList(solicitudes);

        } catch (IOException e) {
            throw new RuntimeException("Error llamando al agregador (IO)", e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Request interrumpida al llamar agregador", e);
        }
    }

    public void aprobarSolicitud(Long id, String comentario) {
        procesarSolicitud(id, comentario, "aprobar");
    }

    public void rechazarSolicitud(Long id, String comentario) {
        procesarSolicitud(id, comentario, "rechazar");
    }

    private void procesarSolicitud(Long id, String comentario, String accion) {
        if (id == null) {
            throw new IllegalArgumentException("id no puede ser null");
        }

        URI uri = URI.create(baseURL + "/solicitudes/" + id + "/" + accion);
        String jsonBody;
        try {
            jsonBody = mapper.writeValueAsString(new ComentarioRequest(comentario));
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error serializando body de solicitud", e);
        }

        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() / 100 != 2) {
                throw new RuntimeException(
                        "Error llamando al agregador (" + accion + " solicitud): " +
                                response.statusCode() + " - " + response.body()
                );
            }

        } catch (IOException e) {
            throw new RuntimeException("Error llamando al agregador (IO)", e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Request interrumpida al llamar agregador", e);
        }
    }
}

