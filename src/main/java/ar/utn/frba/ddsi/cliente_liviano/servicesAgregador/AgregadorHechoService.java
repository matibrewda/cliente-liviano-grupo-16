package ar.utn.frba.ddsi.cliente_liviano.servicesAgregador;


import ar.utn.frba.ddsi.cliente_liviano.DTO.HechoDTO;
import ar.utn.frba.ddsi.cliente_liviano.repositories.agregador.AgregadorHechoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AgregadorHechoService {
    @Autowired
    private final AgregadorHechoRepository agregadorHechoRepository;

    public AgregadorHechoService(AgregadorHechoRepository agregadorHechoRepository) {
        this.agregadorHechoRepository = agregadorHechoRepository;

    }

    public List<HechoDTO> obtenerTodosLosHechos(){
        return agregadorHechoRepository.obtenerTodosLosHechos();
    }
}
