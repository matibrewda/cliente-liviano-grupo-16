package ar.utn.frba.ddsi.cliente_liviano.services;

import ar.utn.frba.ddsi.cliente_liviano.exceptions.NotFoundException;
import ar.utn.frba.ddsi.cliente_liviano.models.Hecho;
import ar.utn.frba.ddsi.cliente_liviano.models.dto.HechoInputDTO;
import ar.utn.frba.ddsi.cliente_liviano.models.repositories.HechosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;
import java.util.Set;

@Service
public class HechosService {
    @Autowired
    private HechosRepository hechosRepository;

    public Hecho crearHecho(Hecho hecho) {
        return this.hechosRepository.save(hecho);
    }

    public Hecho actualizarHecho(Long id, HechoInputDTO hechoDTO) {
        Hecho hecho = intentarRecuperarHecho(id);

        return hechosRepository.update(convertirDTOHEntity(hechoDTO));
    }

    public Optional<HechoInputDTO> obtenerHechoPorID(Long id) {
        Hecho hecho = intentarRecuperarHecho(id);
        return Optional.of(convertirHDTO(hecho));
    }

    private Hecho intentarRecuperarHecho(Long id) {
        Optional<Hecho> hecho = hechosRepository.findById(id);
        if(hecho.isEmpty()) {
            throw new NotFoundException("Hecho", id);
        }
        return hecho.get();
    }

    private HechoInputDTO convertirHDTO(Hecho hecho) {
        HechoInputDTO dto = new HechoInputDTO();
        dto.setId(hecho.getId());
        dto.setTitulo(hecho.getTitulo());
        dto.setDescripcion(hecho.getDescripcion());
        dto.setCategoria(hecho.getCategoria());
        dto.setFechaAcontecimiento(hecho.getFechaAcontecimiento());
        dto.setUbicacion(hecho.getUbicacion());
        dto.setMultimediaUrl(hecho.getMultimediaUrl());
        return dto;
    }

    private Hecho convertirDTOHEntity(HechoInputDTO hechoDTO) {
        Hecho hecho = new Hecho();
        hecho.setId(hechoDTO.getId());
        hecho.setTitulo(hechoDTO.getTitulo());
        hecho.setDescripcion(hechoDTO.getDescripcion());
        hecho.setCategoria(hechoDTO.getCategoria());
        hecho.setFechaAcontecimiento(hechoDTO.getFechaAcontecimiento());
        hecho.setUbicacion(hechoDTO.getUbicacion());
        return hecho;
    }

    public void subirMultimedia(Long idHecho, MultipartFile archivo) {
        if (archivo == null || archivo.isEmpty()) {
            return;
        }

        // Validación tamaño: 5MB
        long maxBytes = 5L * 1024 * 1024; // 5MB
        if (archivo.getSize() > maxBytes) {
            throw new IllegalArgumentException("El archivo supera el máximo permitido de 5MB.");
        }

        // Validación tipo: solo imágenes
        String contentType = archivo.getContentType();
        if (contentType == null || !contentType.toLowerCase().startsWith("image/")) {
            throw new IllegalArgumentException("Solo se permiten imágenes (Content-Type image/*).");
        }

        // (Opcional) whitelist más estricta:
        Set<String> permitidos = Set.of(
                "image/jpeg", "image/jpg", "image/png", "image/webp", "image/gif"
        );
        if (!permitidos.contains(contentType.toLowerCase())) {
            throw new IllegalArgumentException("Formato de imagen no permitido. Usá JPG, PNG, WEBP o GIF.");
        }

        hechosRepository.subirMultimedia(idHecho, archivo);
    }
}
