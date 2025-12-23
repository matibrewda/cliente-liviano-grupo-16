package ar.utn.frba.ddsi.cliente_liviano.controllers;

import ar.utn.frba.ddsi.cliente_liviano.DTO.ColeccionDTO;
import ar.utn.frba.ddsi.cliente_liviano.service.ColeccionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private ColeccionService coleccionService;

    @GetMapping("/colecciones/{coleccionId}/consenso")
    public String configurarConsenso(@PathVariable String coleccionId, Model model) {
        List<ColeccionDTO> colecciones = coleccionService.obtenerColecciones();
        Optional<ColeccionDTO> coleccionOpt = colecciones.stream()
                .filter(c -> c.getHandle().equals(coleccionId))
                .findFirst();


        model.addAttribute("coleccion", coleccionOpt.get());
        return "colecciones/configurar-consenso";
    }
}

