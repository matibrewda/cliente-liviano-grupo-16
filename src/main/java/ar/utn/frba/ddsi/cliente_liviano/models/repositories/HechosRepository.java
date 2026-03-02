package ar.utn.frba.ddsi.cliente_liviano.models.repositories;
import ar.utn.frba.ddsi.cliente_liviano.DTO.HechoDTO;
import ar.utn.frba.ddsi.cliente_liviano.models.Categoria;
import ar.utn.frba.ddsi.cliente_liviano.models.Hecho;
import ar.utn.frba.ddsi.cliente_liviano.models.Ubicacion;
import ar.utn.frba.ddsi.cliente_liviano.models.repositories.dto.FuenteDinamicaHechoRequest;
import ar.utn.frba.ddsi.cliente_liviano.models.repositories.dto.FuenteDinamicaHechoResponse;
import ar.utn.frba.ddsi.cliente_liviano.repositories.agregador.dto.HechoResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.spring6.ISpringTemplateEngine;
import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Repository
public class HechosRepository {
    private final ISpringTemplateEngine iSpringTemplateEngine;
    private HttpClient client;
    private ObjectMapper mapper;

    @Value("${fuente-dinamica.base-url}")
    private String baseURL;

    @Value("${agregador.base-url}")
    private String AgregadorURL;

    private final List<Hecho> hechos = new ArrayList<Hecho>();

    public HechosRepository(ObjectMapper objectMapper, ISpringTemplateEngine iSpringTemplateEngine){
        this.mapper = objectMapper;
        this.client = HttpClient.newHttpClient();
        this.iSpringTemplateEngine = iSpringTemplateEngine;
    }

    public List<Hecho> findAll(){
        URI uri = URI.create(AgregadorURL+ "/hechos");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .header("Content-Type", "application/json")
                .GET()
                .build();
        try {
            HttpResponse<String> response =
                    client.send(request, HttpResponse.BodyHandlers.ofString());

            HechoResponse[] hechosResponse =
                    mapper.readValue(response.body(), HechoResponse[].class);

            System.out.println(Arrays.toString(hechosResponse));

            List<HechoDTO> hechosdto =
                    Arrays.stream(hechosResponse)
                    .map(HechoResponse::toHechoDTO)
                    .toList();
            for (HechoDTO hechoDTO : hechosdto){
                hechos.add(hechoDTO.toHecho());
            }

        }catch (InterruptedException | IOException e) {
            throw new RuntimeException("Request was interrupted", e);
        }

        return hechos;
    }

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
                    hechoNuevo.getCategoria().getNombre(),
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
                    hechoNuevo.getCategoria().getNombre(),
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

    public void subirMultimedia(Long idHecho, MultipartFile archivo) {
        try {
            String boundary = "----Boundary" + System.currentTimeMillis();

            byte[] fileBytes = archivo.getBytes();
            String filename = archivo.getOriginalFilename() == null ? "archivo" : archivo.getOriginalFilename();
            String contentType = archivo.getContentType() == null ? "application/octet-stream" : archivo.getContentType();

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(bos, StandardCharsets.UTF_8), true);

            // parte archivo
            writer.append("--").append(boundary).append("\r\n");
            writer.append("Content-Disposition: form-data; name=\"archivo\"; filename=\"")
                    .append(filename).append("\"\r\n");
            writer.append("Content-Type: ").append(contentType).append("\r\n");
            writer.append("\r\n");
            writer.flush();

            bos.write(fileBytes);
            bos.flush();

            writer.append("\r\n");
            writer.append("--").append(boundary).append("--").append("\r\n");
            writer.flush();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(this.baseURL + "/api/hechos/" + idHecho + "/multimedia"))
                    .header("Content-Type", "multipart/form-data; boundary=" + boundary)
                    .PUT(HttpRequest.BodyPublishers.ofByteArray(bos.toByteArray()))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() < 200 || response.statusCode() >= 300) {
                throw new RuntimeException("Error subiendo multimedia: " + response.statusCode() + " - " + response.body());
            }

        } catch (Exception e) {
            throw new RuntimeException("No se pudo subir la multimedia", e);
        }
    }
}



