package ar.utn.frba.ddsi.cliente_liviano.controllers;

import ar.utn.frba.ddsi.cliente_liviano.exceptions.NotFoundException;
import ar.utn.frba.ddsi.cliente_liviano.models.Categoria;
import ar.utn.frba.ddsi.cliente_liviano.models.Hecho;
import ar.utn.frba.ddsi.cliente_liviano.models.Origen;
import ar.utn.frba.ddsi.cliente_liviano.models.Ubicacion;
import ar.utn.frba.ddsi.cliente_liviano.models.dto.HechoDTO;
import ar.utn.frba.ddsi.cliente_liviano.services.HechosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/hechos")
public class HechosController {
    @Autowired
    private HechosService hechosService;

    @GetMapping
    public String listarHechos(Model model) {
        return "listar-hechos";
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioCrear(Model model) {
        Hecho hecho = new Hecho();
        hecho.setUbicacion(new Ubicacion());
        hecho.setCategoria(new Categoria());
        model.addAttribute("hecho", hecho);
        model.addAttribute("titulo", "Crear Nuevo Hecho");
        return "/contribuyente/crear-hecho";
    }

    @GetMapping("/{id}")
    public String verDetalleAlumno(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            HechoDTO hecho = hechosService.obtenerHechoPorID(id).get();

            model.addAttribute("hecho", hecho);
            model.addAttribute("titulo", "Detalle del Hecho");

            return "/contribuyente/detalle-hecho";
        }
        catch (NotFoundException ex) {
            redirectAttributes.addFlashAttribute("mensaje", ex.getMessage());
            return "redirect:/404";
        }
    }

    @PostMapping("/crear")
    public String crearHecho(@ModelAttribute("hecho")HechoDTO hecho,
                             BindingResult bindingResult,
                             Model model,
                             RedirectAttributes redirectAttributes) {

        Hecho hechoCreado = this.hechosService.crearHecho(hecho.ToDomain());

        redirectAttributes.addFlashAttribute("mensaje", "Hecho " + hechoCreado.getId() + " creado exitosamente");
        redirectAttributes.addFlashAttribute("tipoMensaje", "success");

        return "redirect:/hechos/" + hechoCreado.getId();
    }
}
