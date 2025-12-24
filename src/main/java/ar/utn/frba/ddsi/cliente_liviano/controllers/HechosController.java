package ar.utn.frba.ddsi.cliente_liviano.controllers;

import ar.utn.frba.ddsi.cliente_liviano.exceptions.NotFoundException;
import ar.utn.frba.ddsi.cliente_liviano.models.Categoria;
import ar.utn.frba.ddsi.cliente_liviano.models.Hecho;
import ar.utn.frba.ddsi.cliente_liviano.models.Ubicacion;
import ar.utn.frba.ddsi.cliente_liviano.models.dto.HechoDTO;
import ar.utn.frba.ddsi.cliente_liviano.services.HechosService;
import ar.utn.frba.ddsi.cliente_liviano.services.ArchivoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/hechos")
public class HechosController {
    @Autowired
    private HechosService hechosService;
    @Autowired
    private ArchivoService archivoService;

    @GetMapping
    public String listarHechos(Model model) {
        return "listar-hechos";
    }

    @GetMapping("/{id}")
    public String verDetalleHecho(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
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

    @GetMapping("/nuevo")
    public String mostrarFormularioCrear(Model model) {
        Hecho hecho = new Hecho();
        hecho.setUbicacion(new Ubicacion());
        hecho.setCategoria(new Categoria());
        model.addAttribute("hecho", hecho);
        model.addAttribute("titulo", "Crear Nuevo Hecho");
        return "/contribuyente/crear-hecho";
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

    @GetMapping("/{id}/editar")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            HechoDTO hechoDTO = hechosService.obtenerHechoPorID(id).get();
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
                                   @ModelAttribute("hecho") HechoDTO hechoDTO,
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

    @GetMapping("/carga-masiva")
    public String formArchivo(Model model){
        return "/contribuyente/carga-masiva";
    }

    @PostMapping("/carga-masiva")
    public String subirArchivo(Model model, @RequestParam("archivo") MultipartFile file,
                               RedirectAttributes redirectAttributes){
        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("mensajeError", "Por favor selecciona un archivo válido.");
            return "redirect:/hechos/carga";
        }

        try {
            // Llamada al servicio que creamos antes
            ArchivoService.almacenarArchivo(file);
            // Mensaje Flash: Sobrevive a la redirección y se borra después
            redirectAttributes.addFlashAttribute("mensajeExito",
                    "Archivo cargado correctamente: " + file.getOriginalFilename());

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensajeError", "Error: " + e.getMessage());
        }

        // Redirigimos de vuelta a la página de carga para limpiar el formulario
        return "redirect:/hechos/carga";
    }

}
