package ar.utn.frba.ddsi.cliente_liviano.models.repositories;
import ar.utn.frba.ddsi.cliente_liviano.models.Categoria;
import ar.utn.frba.ddsi.cliente_liviano.models.Hecho;
import ar.utn.frba.ddsi.cliente_liviano.models.Ubicacion;
import ar.utn.frba.ddsi.cliente_liviano.models.repositories.dto.FuenteDinamicaHechoRequest;
import ar.utn.frba.ddsi.cliente_liviano.models.repositories.dto.FuenteDinamicaHechoResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class HechosRepository {
    private HttpClient client;
    private ObjectMapper mapper;

    @Value("${fuente-dinamica.base-url}")
    private String baseURL;

    private final List<Hecho> hechos = new ArrayList<Hecho>();

    public HechosRepository(ObjectMapper objectMapper){
        this.mapper = objectMapper;
        this.client = HttpClient.newHttpClient();
    }

    public List<Hecho> findAll(){return hechos;}

    public Optional<Hecho> findById(Long id){
        Hecho hecho;
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(this.baseURL + "/api/hechos/" + id))
                .header("Content-Type", "application/json")
                .GET()
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            FuenteDinamicaHechoResponse hechoDTO = mapper.readValue(response.body(), FuenteDinamicaHechoResponse.class);
            hecho = hechoDTO.toHecho();
        } catch (InterruptedException | IOException e) {
            throw new RuntimeException("Request was interrupted", e);
        }

        return Optional.of(hecho);
    }

    public Hecho save(Hecho hechoNuevo){
        Hecho hecho;

        String jsonBody;
        try {
            jsonBody = mapper.writeValueAsString(new FuenteDinamicaHechoRequest(
                    hechoNuevo.getTitulo(),
                    hechoNuevo.getDescripcion(),
                    hechoNuevo.getCategoria().getId(),
                    hechoNuevo.getUbicacion().getLatitud(),
                    hechoNuevo.getUbicacion().getLongitud(),
                    hechoNuevo.getFechaAcontecimiento())
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(this.baseURL + "/api/hechos"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            FuenteDinamicaHechoResponse hechoDTO = mapper.readValue(response.body(), FuenteDinamicaHechoResponse.class);
            hecho = hechoDTO.toHecho();
        } catch (InterruptedException | IOException e) {
            throw new RuntimeException("Request was interrupted", e);
        }

        return hecho;
    }

    public Hecho update(Hecho hechoNuevo){
        Hecho hecho;

        String jsonBody;
        try {
            jsonBody = mapper.writeValueAsString(new FuenteDinamicaHechoRequest(
                    hechoNuevo.getTitulo(),
                    hechoNuevo.getDescripcion(),
                    hechoNuevo.getCategoria().getId(),
                    hechoNuevo.getUbicacion().getLatitud(),
                    hechoNuevo.getUbicacion().getLongitud(),
                    hechoNuevo.getFechaAcontecimiento())
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(this.baseURL + "/api/hechos/" + hechoNuevo.getId()))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            FuenteDinamicaHechoResponse hechoDTO = mapper.readValue(response.body(), FuenteDinamicaHechoResponse.class);
            hecho = hechoDTO.toHecho();
        } catch (InterruptedException | IOException e) {
            throw new RuntimeException("Request was interrupted", e);
        }

        return hecho;
    }

}



