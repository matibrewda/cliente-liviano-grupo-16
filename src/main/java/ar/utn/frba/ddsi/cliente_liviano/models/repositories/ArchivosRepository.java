package ar.utn.frba.ddsi.cliente_liviano.models.repositories;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;


@Repository
public class ArchivosRepository {
    private HttpClient client;

    @Value("${fuente-estatica.base-url}")
    private String baseURL;

    public ArchivosRepository(){
        this.client = HttpClient.newHttpClient();
    }

    public void save(MultipartFile file) throws IOException, InterruptedException {
        String boundary = "Boundary" + System.currentTimeMillis();

        // Asegúrate de que la URL no tenga dobles barras
        String finalURL = (this.baseURL.endsWith("/") ? this.baseURL.substring(0, this.baseURL.length() - 1) : this.baseURL)
                + "/api/fuente/estatica/hechos/cargarArchivo";

        byte[] fileBytes = file.getBytes();
        String fileName = file.getOriginalFilename();
        String contentType = file.getContentType() != null ? file.getContentType() : "application/octet-stream";

        // Construcción del cuerpo usando un enfoque más legible
        byte[] header = ("--" + boundary + "\r\n" +
                "Content-Disposition: form-data; name=\"file\"; filename=\"" + fileName + "\"\r\n" +
                "Content-Type: " + contentType + "\r\n\r\n").getBytes(StandardCharsets.UTF_8);

        byte[] footer = ("\r\n--" + boundary + "--\r\n").getBytes(StandardCharsets.UTF_8);

        // Concatenación de bytes
        ByteBuffer payload = ByteBuffer.allocate(header.length + fileBytes.length + footer.length);
        payload.put(header);
        payload.put(fileBytes);
        payload.put(footer);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(finalURL))
                .header("Content-Type", "multipart/form-data; boundary=" + boundary)
                .POST(HttpRequest.BodyPublishers.ofByteArray(payload.array()))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println("Status Code: " + response.statusCode());
        if (response.statusCode() >= 400) {
            // Imprime el body para ver si Spring te da una pista del error
            System.err.println("Response body: " + response.body());
            throw new IOException("Error al cargar archivo: " + response.statusCode());
        }
    }
}