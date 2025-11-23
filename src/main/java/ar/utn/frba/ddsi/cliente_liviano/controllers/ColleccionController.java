package ar.utn.frba.ddsi.cliente_liviano.controllers;

import ar.utn.frba.ddsi.cliente_liviano.DTO.HechoDTO;
import ar.utn.frba.ddsi.cliente_liviano.service.ColeccionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/colecciones")
public class ColleccionController {

    @Autowired
    private ColeccionService coleccionService;

    @GetMapping("/{coleccionId}/hechos")
    public String hecho(@PathVariable String coleccionId, Model model){
        List<HechoDTO> hechos = coleccionService.obtenerHechosPorColeccion(coleccionId);
        model.addAttribute("coleccionId", coleccionId);
        model.addAttribute("hechos", hechos);
        return "colecciones/hechos";
    }
}
