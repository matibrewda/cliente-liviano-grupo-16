package ar.utn.frba.ddsi.cliente_liviano.models.repositories;

import ar.utn.frba.ddsi.cliente_liviano.models.Categoria;
import ar.utn.frba.ddsi.cliente_liviano.models.Hecho;
import ar.utn.frba.ddsi.cliente_liviano.models.repositories.dto.CategoriaDTO;
import ar.utn.frba.ddsi.cliente_liviano.models.repositories.dto.FuenteDinamicaHechoResponse;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Repository
public class CategoriaRepository {
    private HttpClient client;
    private ObjectMapper mapper;

    @Value("${agregador.base-url}")
    private String baseURL;

    public CategoriaRepository(ObjectMapper objectMapper){
        this.mapper = objectMapper;
        this.client = HttpClient.newHttpClient();
    }

    public List<Categoria> findAll(){
        List<Categoria> categorias = new ArrayList<>();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(this.baseURL + "/categorias"))
                .header("Content-Type", "application/json")
                .GET()
                .build();


        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            List<CategoriaDTO> dtos = mapper.readValue(response.body(), new TypeReference<>() {});
            dtos.forEach(categoriaDTO -> {
                categorias.add(categoriaDTO.toCategoria());
            });
        } catch (InterruptedException | IOException e) {
            throw new RuntimeException("Request was interrupted", e);
        }

        return categorias;
    }
}
