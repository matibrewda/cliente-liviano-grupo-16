package ar.utn.frba.ddsi.cliente_liviano.services;

import ar.utn.frba.ddsi.cliente_liviano.exceptions.NotFoundException;
import ar.utn.frba.ddsi.cliente_liviano.models.Hecho;
import ar.utn.frba.ddsi.cliente_liviano.models.dto.HechoInputDTO;
import ar.utn.frba.ddsi.cliente_liviano.models.repositories.HechosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

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
}
