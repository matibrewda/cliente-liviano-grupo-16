package ar.utn.frba.ddsi.cliente_liviano.servicesAgregador;

import ar.utn.frba.ddsi.cliente_liviano.repositories.agregador.AgregadorSolicitudRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AgregadorSolicitudesService {
    @Autowired
    private AgregadorSolicitudRepository agregadorSolicitudRepository;

    public void crearSolicitud(Long idHecho,String motivo){
        agregadorSolicitudRepository.crearSolicitud(idHecho,motivo);
    }
}
