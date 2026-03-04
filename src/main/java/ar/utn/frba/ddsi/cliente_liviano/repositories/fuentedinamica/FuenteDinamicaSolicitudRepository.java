package ar.utn.frba.ddsi.cliente_liviano.repositories.fuentedinamica;

import ar.utn.frba.ddsi.cliente_liviano.repositories.fuentedinamica.dto.AprobarSolicitudRequest;
import ar.utn.frba.ddsi.cliente_liviano.repositories.fuentedinamica.dto.ComentarioRequest;
import ar.utn.frba.ddsi.cliente_liviano.repositories.fuentedinamica.dto.SolicitudModificacionRequest;
import ar.utn.frba.ddsi.cliente_liviano.repositories.fuentedinamica.dto.SolicitudModificacionResponse;
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
public class FuenteDinamicaSolicitudRepository {

    private final HttpClient client;
    private final ObjectMapper mapper;

    @Value("${fuente-dinamica.base-url}")
    private String baseURL;

    public FuenteDinamicaSolicitudRepository(ObjectMapper objectMapper) {
        this.mapper = objectMapper;
        this.client = HttpClient.newHttpClient();
    }

    public List<SolicitudModificacionResponse> obtenerSolicitudes() {
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
                        "Error llamando a fuente dinámica (obtener solicitudes): " +
                                response.statusCode() + " - " + response.body()
                );
            }
            SolicitudModificacionResponse[] solicitudes = mapper.readValue(response.body(), SolicitudModificacionResponse[].class);
            return Arrays.asList(solicitudes);
        } catch (IOException e) {
            throw new RuntimeException("Error llamando a fuente dinámica (IO)", e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Request interrumpida al llamar fuente dinámica", e);
        }
    }

    public void crearSolicitud(SolicitudModificacionRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("request no puede ser null");
        }
        URI uri = URI.create(baseURL + "/solicitudes");
        String jsonBody;
        try {
            jsonBody = mapper.writeValueAsString(request);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error serializando body de solicitud de modificación", e);
        }

        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(uri)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();

        try {
            HttpResponse<String> response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() / 100 != 2) {
                throw new RuntimeException(
                        "Error llamando a fuente dinámica (crear solicitud): " +
                                response.statusCode() + " - " + response.body()
                );
            }
        } catch (IOException e) {
            throw new RuntimeException("Error llamando a fuente dinámica (IO)", e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Request interrumpida al llamar fuente dinámica", e);
        }
    }

    public void aprobarSolicitud(Long idSolicitud, AprobarSolicitudRequest request) {
        if (idSolicitud == null) {
            throw new IllegalArgumentException("idSolicitud no puede ser null");
        }
        if (request == null) {
            throw new IllegalArgumentException("request no puede ser null");
        }
        URI uri = URI.create(baseURL + "/solicitudes/" + idSolicitud + "/aprobar");
        String jsonBody;
        try {
            jsonBody = mapper.writeValueAsString(request);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error serializando body de aprobar", e);
        }
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(uri)
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();

        try {
            HttpResponse<String> response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() / 100 != 2) {
                throw new RuntimeException(
                        "Error llamando a fuente dinámica (aprobar solicitud): " +
                                response.statusCode() + " - " + response.body()
                );
            }
        } catch (IOException e) {
            throw new RuntimeException("Error llamando a fuente dinámica (IO)", e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Request interrumpida al llamar fuente dinámica", e);
        }
    }

    public void rechazarSolicitud(Long idSolicitud, String comentario) {
        if (idSolicitud == null) {
            throw new IllegalArgumentException("idSolicitud no puede ser null");
        }
        URI uri = URI.create(baseURL + "/solicitudes/" + idSolicitud + "/rechazar");
        String jsonBody;
        try {
            jsonBody = mapper.writeValueAsString(new ComentarioRequest(comentario != null ? comentario : ""));
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error serializando body de rechazar", e);
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
                        "Error llamando a fuente dinámica (rechazar solicitud): " +
                                response.statusCode() + " - " + response.body()
                );
            }
        } catch (IOException e) {
            throw new RuntimeException("Error llamando a fuente dinámica (IO)", e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Request interrumpida al llamar fuente dinámica", e);
        }
    }
}
