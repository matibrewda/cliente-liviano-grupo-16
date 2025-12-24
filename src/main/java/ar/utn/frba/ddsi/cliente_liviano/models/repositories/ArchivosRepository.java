package ar.utn.frba.ddsi.cliente_liviano.models.repositories;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import java.net.http.HttpClient;


@Repository
public class ArchivosRepository {
    private HttpClient client;
    private ObjectMapper mapper;

    @Value("${fuente-estatica.base-url}")
    private String baseURL;

    public  ArchivosRepository(ObjectMapper objectMapper){
        this.client = HttpClient.newHttpClient();
    }

    public void almacenarArchivo(MultipartFile file) {
    }
}
