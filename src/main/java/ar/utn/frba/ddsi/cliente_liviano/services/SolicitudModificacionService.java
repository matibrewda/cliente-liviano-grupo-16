package ar.utn.frba.ddsi.cliente_liviano.services;

import ar.utn.frba.ddsi.cliente_liviano.repositories.fuentedinamica.FuenteDinamicaSolicitudRepository;
import ar.utn.frba.ddsi.cliente_liviano.repositories.fuentedinamica.dto.SolicitudModificacionRequest;
import ar.utn.frba.ddsi.cliente_liviano.repositories.fuentedinamica.dto.SolicitudModificacionResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SolicitudModificacionService {

    @Autowired
    private FuenteDinamicaSolicitudRepository fuenteDinamicaSolicitudRepository;

    public List<SolicitudModificacionResponse> listar() {
        return fuenteDinamicaSolicitudRepository.obtenerSolicitudes();
    }

    public void crear(SolicitudModificacionRequest request) {
        fuenteDinamicaSolicitudRepository.crearSolicitud(request);
    }

    public void aprobar(Long idSolicitud) {
        fuenteDinamicaSolicitudRepository.aprobarSolicitud(idSolicitud);
    }
}
