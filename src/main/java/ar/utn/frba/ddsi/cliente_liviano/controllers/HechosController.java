package ar.utn.frba.ddsi.cliente_liviano.controllers;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

@Controller
@RequestMapping("/hechos")
public class HechosController {
    @GetMapping
    public String listarHechos(Model model) {
        return "listar-hechos";
    }
    @GetMapping("/crear")
    public String nuevoHecho(){
        return "crear-hecho";
    }


}
