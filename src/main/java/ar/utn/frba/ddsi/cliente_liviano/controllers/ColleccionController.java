package ar.utn.frba.ddsi.cliente_liviano.controllers;

import ar.utn.frba.ddsi.cliente_liviano.DTO.HechoDTO;
import ar.utn.frba.ddsi.cliente_liviano.service.ColeccionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/colecciones")
public class ColleccionController {

    @Autowired
    private ColeccionService coleccionService;

    @GetMapping("/{coleccionId}/hechos/{hechoId}")
    public String verHechoPorID(@PathVariable String coleccionId, @PathVariable String hechoId,Model model){
        HechoDTO hecho = coleccionService.obtenerHechoPorColeccionId(coleccionId, hechoId);
        model.addAttribute("hecho", hecho);
        model.addAttribute("coleccionId",coleccionId);

        return "colecciones/detalle-hecho";
    }
}