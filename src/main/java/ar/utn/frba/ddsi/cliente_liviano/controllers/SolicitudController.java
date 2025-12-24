package ar.utn.frba.ddsi.cliente_liviano.controllers;

import ar.utn.frba.ddsi.cliente_liviano.servicesAgregador.AgregadorSolicitudesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/solicitud")
public class SolicitudController {
    @Autowired
    private AgregadorSolicitudesService agregadorSolicitudesService;

    @GetMapping("/solicitud-eliminacion/hecho/{idHecho:\\d+}")
    public String solicitarEliminacion(@PathVariable Long idHecho, Model model){
        model.addAttribute("titulo", "Solicitud eliminacion");
        model.addAttribute("idHecho", idHecho);
        return "solicitudes/hecho-eliminacion";
    }

    @PostMapping("/crear/solicitud-eliminacion/hecho/{idHecho:\\d+}")
    public String enviarSolicitud(@PathVariable Long idHecho,
                                  @RequestParam String motivo) {
        System.out.println("Motivo: " + motivo);
        System.out.println("hecho: " + idHecho);
        agregadorSolicitudesService.crearSolicitud(idHecho, motivo);
        return "redirect:/";
    }
}