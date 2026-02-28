package ar.utn.frba.ddsi.cliente_liviano.models.repositories;

import ar.utn.frba.ddsi.cliente_liviano.models.usuario.Usuario;
import ar.utn.frba.ddsi.cliente_liviano.models.repositories.dto.LoginRequest;
import ar.utn.frba.ddsi.cliente_liviano.models.repositories.dto.LoginResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Repository
public class LoginRepository {
    private HttpClient client;
    private ObjectMapper mapper;

    @Value("${login.base-url}")
    private String baseURL;

    public LoginRepository(ObjectMapper objectMapper){
        this.mapper = objectMapper;
        this.client = HttpClient.newHttpClient();
    }

    public Usuario login(Usuario usuario){
        Usuario user;
        String jsonBody;
        try {
            jsonBody = mapper.writeValueAsString(new LoginRequest(
                    usuario.getUsername(),
                    usuario.getPassword())
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(this.baseURL + "/auth/login"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            LoginResponse loginDTO = mapper.readValue(response.body(), LoginResponse.class);
            user = loginDTO.toUsuario(usuario.getUsername());
        } catch (InterruptedException | IOException e) {
            throw new RuntimeException("Request was interrupted", e);
        }

        return user;
    }

}
