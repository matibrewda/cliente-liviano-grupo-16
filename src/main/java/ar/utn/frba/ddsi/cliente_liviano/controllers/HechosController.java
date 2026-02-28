package ar.utn.frba.ddsi.cliente_liviano.controllers;

import ar.utn.frba.ddsi.cliente_liviano.DTO.HechoDTO;
import ar.utn.frba.ddsi.cliente_liviano.exceptions.NotFoundException;
import ar.utn.frba.ddsi.cliente_liviano.models.Categoria;
import ar.utn.frba.ddsi.cliente_liviano.models.Hecho;
import ar.utn.frba.ddsi.cliente_liviano.models.Ubicacion;
import ar.utn.frba.ddsi.cliente_liviano.models.dto.HechoInputDTO;
import ar.utn.frba.ddsi.cliente_liviano.services.CategoriaService;
import ar.utn.frba.ddsi.cliente_liviano.services.HechosService;
import ar.utn.frba.ddsi.cliente_liviano.servicesAgregador.AgregadorHechoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/hechos")
public class HechosController {
    @Autowired
    private HechosService hechosService;
    @Autowired
    private CategoriaService categoriaService;

    @Autowired
    private AgregadorHechoService agregadorHechoService;

    @GetMapping
    public String listarHechos(Model model) {
        List<HechoDTO> hechos = agregadorHechoService.obtenerTodosLosHechos();
        model.addAttribute("hechos", hechos);
        return "listar-hechos";
    }
    @GetMapping("/detalle/{idHecho}")
    public String detalleHechoByID(@PathVariable Long idHecho, Model model) {
        var hecho = agregadorHechoService.getHechoById(idHecho);
        model.addAttribute("hecho", hecho);
        return "detalle-un-hecho";
    }


    @GetMapping("/{id}")
    public String verDetalleHecho(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            HechoInputDTO hecho = hechosService.obtenerHechoPorID(id).get();
            List<Categoria> categorias = categoriaService.getAll();
            categorias.forEach(categoria -> {
                if (categoria.getId().equals(hecho.getCategoria().getId())) {
                    hecho.setCategoria(categoria);
                }
            });

            model.addAttribute("hecho", hecho);
            model.addAttribute("titulo", "Detalle del Hecho");

            return "/contribuyente/detalle-hecho";
        }
        catch (NotFoundException ex) {
            redirectAttributes.addFlashAttribute("mensaje", ex.getMessage());
            return "redirect:/404";
        }
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioCrear(Model model) {
        Hecho hecho = new Hecho();
        hecho.setUbicacion(new Ubicacion());
        hecho.setCategoria(new Categoria());
        List<Categoria> categoriasDisponibles = categoriaService.getAll();
        model.addAttribute("categoriasDisponibles", categoriasDisponibles);
        model.addAttribute("hecho", hecho);
        model.addAttribute("titulo", "Crear Nuevo Hecho");
        return "/contribuyente/crear-hecho";
    }

    @PostMapping("/crear")
    public String crearHecho(@ModelAttribute("hecho") HechoInputDTO hecho,
                             @RequestParam(value = "archivo", required = false) MultipartFile archivo,
                             BindingResult bindingResult,
                             Model model,
                             RedirectAttributes redirectAttributes) {

        Hecho hechoCreado = this.hechosService.crearHecho(hecho.ToDomain());

        // Si el usuario adjuntó archivo, lo subimos con PUT al microservicio dinámico
        if (archivo != null && !archivo.isEmpty()) {
            try {
                this.hechosService.subirMultimedia(hechoCreado.getId(), archivo);
            } catch (Exception e) {
                redirectAttributes.addFlashAttribute("mensaje",
                        "Hecho creado, pero no se pudo subir la multimedia.");
                redirectAttributes.addFlashAttribute("tipoMensaje", "warning");
                return "redirect:/hechos/" + hechoCreado.getId();
            }
        }


        redirectAttributes.addFlashAttribute("mensaje", "Hecho " + hechoCreado.getId() + " creado exitosamente");
        redirectAttributes.addFlashAttribute("tipoMensaje", "success");

        return "redirect:/hechos/" + hechoCreado.getId();
    }

    @GetMapping("/{id}/editar")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            HechoInputDTO hechoDTO = hechosService.obtenerHechoPorID(id).get();
            List<Categoria> categoriasDisponibles = categoriaService.getAll();
            model.addAttribute("categoriasDisponibles", categoriasDisponibles);
            model.addAttribute("hecho", hechoDTO);
            model.addAttribute("titulo", "Editar Hecho");
            return "/contribuyente/editar-hecho";
        }
        catch (NotFoundException ex) {
            redirectAttributes.addFlashAttribute("mensaje", ex.getMessage());
            return "redirect:/404";
        }
    }

    @PostMapping("/{id}/actualizar")
    public String actualizarHecho(@PathVariable Long id,
                                   @ModelAttribute("hecho") HechoInputDTO hechoDTO,
                                   BindingResult bindingResult,
                                   Model model,
                                   RedirectAttributes redirectAttributes
    ){
        try {
            Hecho hechoActualizado = hechosService.actualizarHecho(id, hechoDTO);

            redirectAttributes.addFlashAttribute("mensaje", "Hecho actualizado exitosamente");
            redirectAttributes.addFlashAttribute("tipoMensaje", "success");
            return "redirect:/hechos/" + hechoActualizado.getId();
        }
        catch (NotFoundException ex) {
            redirectAttributes.addFlashAttribute("mensaje", ex.getMessage());
            return "redirect:/404";
        }
    }
}
