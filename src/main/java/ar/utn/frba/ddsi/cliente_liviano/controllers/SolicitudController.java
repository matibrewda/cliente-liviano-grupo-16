package ar.utn.frba.ddsi.cliente_liviano.controllers;

import ar.utn.frba.ddsi.cliente_liviano.repositories.agregador.dto.SolicitudResponse;
import ar.utn.frba.ddsi.cliente_liviano.servicesAgregador.AgregadorSolicitudesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
                                  RedirectAttributes redirectAttributes) {

//        if (motivo == null || motivo.trim().length() < 500) {
//            redirectAttributes.addFlashAttribute("errorMotivo", "El motivo debe tener al menos 500 caracteres.");
//            redirectAttributes.addFlashAttribute("motivo", motivo);
//            return "redirect:/solicitudes/solicitud-eliminacion/hecho/" + idHecho;
//        }

        try {
            agregadorSolicitudesService.crearSolicitud(idHecho, motivo);
            redirectAttributes.addFlashAttribute("mensaje", "Solicitud de eliminación enviada correctamente.");
            return "redirect:/hechos/detalle/" + idHecho;
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "No se pudo crear la solicitud. " + (e.getMessage() != null ? e.getMessage() : "Error en el servidor."));
            redirectAttributes.addFlashAttribute("motivo", motivo);
            return "redirect:/solicitudes/solicitud-eliminacion/hecho/" + idHecho;
        }
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