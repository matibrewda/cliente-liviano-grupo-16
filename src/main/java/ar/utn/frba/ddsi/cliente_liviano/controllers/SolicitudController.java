package ar.utn.frba.ddsi.cliente_liviano.controllers;

import ar.utn.frba.ddsi.cliente_liviano.repositories.agregador.dto.SolicitudResponse;
import ar.utn.frba.ddsi.cliente_liviano.servicesAgregador.AgregadorSolicitudesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/solicitudes")
public class SolicitudController {
    @Autowired
    private AgregadorSolicitudesService agregadorSolicitudesService;

    @GetMapping("/solicitud-eliminacion/hecho/{idHecho:\\d+}")
    public String solicitarEliminacion(@PathVariable Long idHecho, Model model) {
        model.addAttribute("titulo", "Solicitud eliminacion");
        model.addAttribute("idHecho", idHecho);
        return "solicitudes/hecho-eliminacion";
    }

    @PostMapping("/crear/solicitud-eliminacion/hecho/{idHecho:\\d+}")
    public String enviarSolicitud(@PathVariable Long idHecho,
                                  @RequestParam String motivo,
                                  Model model) {

//        if (motivo == null || motivo.trim().length() < 500) {
//            model.addAttribute("titulo", "Solicitud eliminacion");
//            model.addAttribute("idHecho", idHecho);
//            model.addAttribute("errorMotivo",
//                    "El motivo debe tener al menos 500 caracteres.");
//            model.addAttribute("motivo", motivo);
//
//            return "solicitudes/hecho-eliminacion";
//        }

        agregadorSolicitudesService.crearSolicitud(idHecho, motivo);
        return "redirect:/hechos/detalle/"+idHecho;
    }

    @GetMapping("/admin")
    public String listarSolicitudes(Model model) {
        List<SolicitudResponse> solicitudes = agregadorSolicitudesService.obtenerSolicitudes();
        model.addAttribute("titulo", "Administrar Solicitudes");
        model.addAttribute("solicitudes", solicitudes);
        return "solicitudes/lista-admin";
    }

    @PostMapping("/{id}/aprobar")
    public String aprobarSolicitud(@PathVariable Long id,
                                   @RequestParam String comentario) {
        agregadorSolicitudesService.aprobarSolicitud(id, comentario);
        return "redirect:/solicitudes/admin";
    }

    @PostMapping("/{id}/rechazar")
    public String rechazarSolicitud(@PathVariable Long id,
                                    @RequestParam String comentario) {
        agregadorSolicitudesService.rechazarSolicitud(id, comentario);
        return "redirect:/solicitudes/admin";
    }
}