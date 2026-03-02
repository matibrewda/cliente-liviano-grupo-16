package ar.utn.frba.ddsi.cliente_liviano.controllers;

import ar.utn.frba.ddsi.cliente_liviano.DTO.ColeccionDTO;
import ar.utn.frba.ddsi.cliente_liviano.servicesAgregador.AgregadorColeccionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AgregadorColeccionService coleccionService;

    @GetMapping("/colecciones/{coleccionId}/consenso")
    public String configurarConsenso(@PathVariable String coleccionId, Model model) {
        List<ColeccionDTO> colecciones = coleccionService.obtenerColecciones();
        Optional<ColeccionDTO> coleccionOpt = colecciones.stream()
                .filter(c -> c.getHandle().equals(coleccionId))
                .findFirst();


        model.addAttribute("coleccion", coleccionOpt.get());
        return "colecciones/configurar-consenso";
    }

    @PostMapping("/colecciones/{coleccionId}/consenso")
    public String guardarConsenso(@PathVariable String coleccionId,
                                   @RequestParam String tipoConsenso,
                                   RedirectAttributes redirectAttributes) {
        try {
            coleccionService.actualizarConsenso(coleccionId, tipoConsenso);
            redirectAttributes.addFlashAttribute("success", "Configuración de consenso guardada exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al guardar la configuración: " + e.getMessage());
            return "redirect:/admin/colecciones/" + coleccionId + "/consenso";
        }
        return "redirect:/colecciones";
    }
}

