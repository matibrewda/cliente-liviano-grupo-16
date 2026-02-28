package ar.utn.frba.ddsi.cliente_liviano.servicesAgregador;

import ar.utn.frba.ddsi.cliente_liviano.repositories.agregador.AgregadorSolicitudRepository;
import ar.utn.frba.ddsi.cliente_liviano.repositories.agregador.dto.SolicitudResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AgregadorSolicitudesService {
    @Autowired
    private AgregadorSolicitudRepository agregadorSolicitudRepository;

    public void crearSolicitud(Long idHecho, String motivo) {
        agregadorSolicitudRepository.crearSolicitud(idHecho, motivo);
    }

    public List<SolicitudResponse> obtenerSolicitudes() {
        return agregadorSolicitudRepository.obtenerSolicitudes();
    }

    public void aprobarSolicitud(Long id, String comentario) {
        agregadorSolicitudRepository.aprobarSolicitud(id, comentario);
    }

    public void rechazarSolicitud(Long id, String comentario) {
        agregadorSolicitudRepository.rechazarSolicitud(id, comentario);
    }
}
