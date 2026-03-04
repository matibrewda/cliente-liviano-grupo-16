package ar.utn.frba.ddsi.cliente_liviano.services;

import ar.utn.frba.ddsi.cliente_liviano.models.dto.LoginRequestDTO;
import ar.utn.frba.ddsi.cliente_liviano.models.repositories.dto.LoginRequest;
import ar.utn.frba.ddsi.cliente_liviano.models.repositories.dto.LoginResponse;
import ar.utn.frba.ddsi.cliente_liviano.models.usuario.Usuario;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
public class LoginService {
    private HttpClient client;
    private ObjectMapper mapper;

    @Value("${login.base-url}")
    private String baseURL;

    public LoginService(ObjectMapper objectMapper){
        this.mapper = objectMapper;
        this.client = HttpClient.newHttpClient();
    }

    public LoginResponse login(LoginRequestDTO usuario){
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

            return loginDTO;
        } catch (InterruptedException | IOException e) {
            throw new RuntimeException("Request was interrupted", e);
        }
    }
}
