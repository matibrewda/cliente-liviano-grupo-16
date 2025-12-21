package ar.utn.frba.ddsi.cliente_liviano.controllers;

import ar.utn.frba.ddsi.cliente_liviano.DTO.ColeccionDTO;
import ar.utn.frba.ddsi.cliente_liviano.service.ColeccionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/colecciones")
public class ColleccionController {

    @Autowired
    private ColeccionService coleccionService;

    @GetMapping
    public String listarColecciones(Model model){
        List<ColeccionDTO> coleccionesDTO = coleccionService.obtenerColecciones();
        model.addAttribute("colecciones", coleccionesDTO);
        model.addAttribute("totalColecciones", coleccionesDTO.size());
        return "colecciones/lista";
    }


}
