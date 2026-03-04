package ar.utn.frba.ddsi.cliente_liviano.repositories.agregador;


import ar.utn.frba.ddsi.cliente_liviano.DTO.ColeccionDTO;
import ar.utn.frba.ddsi.cliente_liviano.DTO.input.ColeccionInputDTO;
import ar.utn.frba.ddsi.cliente_liviano.DTO.FiltroDto;
import ar.utn.frba.ddsi.cliente_liviano.DTO.HechoDTO;
import ar.utn.frba.ddsi.cliente_liviano.exceptions.AgregadorApiException;
import ar.utn.frba.ddsi.cliente_liviano.repositories.agregador.dto.ColeccionRequest;
import ar.utn.frba.ddsi.cliente_liviano.repositories.agregador.dto.ColeccionResponse;
import ar.utn.frba.ddsi.cliente_liviano.repositories.agregador.dto.CriterioPertenenciaResponse;
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
import java.util.Collections;
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
                    0L,
                    coleccionNueva.getTitulo(),
                    coleccionNueva.getDescripcion(),
                    coleccionNueva.getFiltros()));
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

    public ColeccionDTO findById(Long id) {

        ColeccionDTO coleccion;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(this.baseURL + "/admin/colecciones/" + id))
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

            ColeccionResponse coleccionResponse =
                    mapper.readValue(response.body(), ColeccionResponse.class);

            coleccion = coleccionResponse.toColeccionDTO();

        } catch (InterruptedException | IOException e) {
            throw new RuntimeException("Request was interrupted", e);
        }

        return coleccion;
    }



    public ColeccionDTO actualizarColeccion(Long idColeccion, ColeccionInputDTO coleccionNueva) {
        ColeccionDTO coleccion;

        String jsonBody;
        try {
            jsonBody = mapper.writeValueAsString(new ColeccionRequest(
                    idColeccion,
                    coleccionNueva.getTitulo(),
                    coleccionNueva.getDescripcion(),
                    coleccionNueva.getFiltros()
            ));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(this.baseURL + "/admin/colecciones/" + idColeccion))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();

        try {
            HttpResponse<String> response =
                    client.send(request, HttpResponse.BodyHandlers.ofString());

            // si no es 2xx, error
            if (response.statusCode() / 100 != 2) {
                throw new RuntimeException(
                        "Error actualizando colección: " + response.statusCode() + " - " + response.body()
                );
            }

            ColeccionResponse coleccionResponse =
                    mapper.readValue(response.body(), ColeccionResponse.class);

            coleccion = coleccionResponse.toColeccionDTO();

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Request was interrupted", e);
        } catch (IOException e) {
            throw new RuntimeException("Error parseando respuesta de colección", e);
        }

        return coleccion;
    }

    /** Actualiza solo los filtros (criterio de pertenencia). PUT con body {"filtros": [...]} como en el API del agregador. */
    public void actualizarSoloFiltros(Long id, List<FiltroDto> filtros) {
        String jsonBody;
        try {
            jsonBody = mapper.writeValueAsString(java.util.Map.of("filtros", filtros != null ? filtros : List.of()));
        } catch (IOException e) {
            throw new RuntimeException("Error serializando filtros", e);
        }
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(this.baseURL + "/admin/colecciones/" + id))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();
        try {
            HttpResponse<String> response =
                    client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() / 100 != 2) {
                throw new RuntimeException(
                        "Error actualizando colección: " + response.statusCode() + " - " + response.body());
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Request interrumpida al actualizar filtros", e);
        } catch (IOException e) {
            throw new RuntimeException("Error de IO al actualizar filtros", e);
        }
    }

    public void eliminar(Long id) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(this.baseURL + "/admin/colecciones/" + id))
                .header("Content-Type", "application/json")
                .DELETE()
                .build();

        try {
            HttpResponse<String> response =
                    client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() / 100 != 2) {
                throw new AgregadorApiException(response.statusCode(), response.body());
            }

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Request interrumpida al eliminar colección", e);
        } catch (IOException e) {
            throw new RuntimeException("Error de IO al eliminar colección", e);
        }
    }

    public void actualizarConsenso(String coleccionId, String tipoConsenso) {
        String jsonBody;
        try {
            jsonBody = mapper.writeValueAsString(java.util.Map.of("tipo", tipoConsenso));
        } catch (IOException e) {
            throw new RuntimeException("Error serializando request de consenso", e);
        }

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(this.baseURL + "/admin/colecciones/" + coleccionId + "/consenso"))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();

        try {
            HttpResponse<String> response =
                    client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() / 100 != 2) {
                throw new RuntimeException(
                        "Error actualizando consenso: " + response.statusCode() + " - " + response.body()
                );
            }

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Request interrumpida al actualizar consenso", e);
        } catch (IOException e) {
            throw new RuntimeException("Error de IO al actualizar consenso", e);
        }
    }

    public void agregarFuente(Long id, String tipo) {
        String jsonBody;
        try {
            jsonBody = mapper.writeValueAsString(java.util.Map.of("tipo", tipo));
        } catch (IOException e) {
            throw new RuntimeException("Error serializando request de fuente", e);
        }
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(this.baseURL + "/admin/colecciones/" + id + "/fuente"))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();
        try {
            HttpResponse<String> response =
                    client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() / 100 != 2) {
                throw new RuntimeException(
                        "Error agregando fuente: " + response.statusCode() + " - " + response.body()
                );
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Request interrumpida al agregar fuente", e);
        } catch (IOException e) {
            throw new RuntimeException("Error de IO al agregar fuente", e);
        }
    }

    public void quitarFuente(Long id, String tipo) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(this.baseURL + "/admin/colecciones/" + id + "/fuente/" + tipo))
                .header("Content-Type", "application/json")
                .DELETE()
                .build();
        try {
            HttpResponse<String> response =
                    client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() / 100 != 2) {
                throw new RuntimeException(
                        "Error eliminando fuente: " + response.statusCode() + " - " + response.body()
                );
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Request interrumpida al eliminar fuente", e);
        } catch (IOException e) {
            throw new RuntimeException("Error de IO al eliminar fuente", e);
        }
    }

    public List<FiltroDto> getCriterioPertenencia(Long id) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(this.baseURL + "/admin/colecciones/" + id + "/criterio-pertenencia"))
                .header("Content-Type", "application/json")
                .GET()
                .build();
        try {
            HttpResponse<String> response =
                    client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() / 100 != 2) {
                return Collections.emptyList();
            }
            CriterioPertenenciaResponse body = mapper.readValue(response.body(), CriterioPertenenciaResponse.class);
            return body.getFiltros() != null ? body.getFiltros() : Collections.emptyList();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return Collections.emptyList();
        } catch (IOException e) {
            return Collections.emptyList();
        }
    }

}
