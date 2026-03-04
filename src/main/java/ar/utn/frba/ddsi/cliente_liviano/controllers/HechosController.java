package ar.utn.frba.ddsi.cliente_liviano.controllers;

import ar.utn.frba.ddsi.cliente_liviano.DTO.HechoDTO;
import ar.utn.frba.ddsi.cliente_liviano.exceptions.NotFoundException;
import ar.utn.frba.ddsi.cliente_liviano.models.Categoria;
import ar.utn.frba.ddsi.cliente_liviano.models.Hecho;
import ar.utn.frba.ddsi.cliente_liviano.models.Ubicacion;
import ar.utn.frba.ddsi.cliente_liviano.models.dto.HechoInputDTO;
import ar.utn.frba.ddsi.cliente_liviano.services.CategoriaService;
import ar.utn.frba.ddsi.cliente_liviano.services.HechosService;
import ar.utn.frba.ddsi.cliente_liviano.services.ArchivoService;
import ar.utn.frba.ddsi.cliente_liviano.servicesAgregador.AgregadorHechoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/hechos")
public class HechosController {
    @Autowired
    private HechosService hechosService;
    @Autowired
    private ArchivoService archivoService;
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
    public String mostrarFormularioEditar(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        return "redirect:/solicitudes-modificacion/nueva?hechoId=" + id;
    }

    @PostMapping("/{id}/actualizar")
    public String actualizarHecho(@PathVariable Long id,
                                   @ModelAttribute("hecho") HechoInputDTO hechoDTO,
                                  @RequestParam(value = "archivo", required = false) MultipartFile archivo,
                                   BindingResult bindingResult,
                                   Model model,
                                   RedirectAttributes redirectAttributes
    ){
        try {
            Hecho hechoActualizado = hechosService.actualizarHecho(id, hechoDTO);


            // Si adjunto imagen
            if (archivo != null && !archivo.isEmpty()) {
                try {
                    hechosService.subirMultimedia(id, archivo);
                } catch (IllegalArgumentException e) {
                    redirectAttributes.addFlashAttribute("mensaje",
                            "Hecho actualizado, pero la imagen no es válida: " + e.getMessage());
                    redirectAttributes.addFlashAttribute("tipoMensaje", "warning");
                    return "redirect:/hechos/" + id;
                } catch (Exception e) {
                    redirectAttributes.addFlashAttribute("mensaje",
                            "Hecho actualizado, pero no se pudo subir la imagen.");
                    redirectAttributes.addFlashAttribute("tipoMensaje", "warning");
                    return "redirect:/hechos/" + id;
                }
            }

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
            archivoService.almacenarArchivo(file);
            // Mensaje Flash: Sobrevive a la redirección y se borra después
            redirectAttributes.addFlashAttribute("mensajeExito",
                    "Archivo cargado correctamente: " + file.getOriginalFilename());

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensajeError", "Error: " + e.getMessage());
        }

        // Redirigimos de vuelta a la página de carga para limpiar el formulario
        return "redirect:/hechos/carga-masiva";
    }

    @GetMapping("/mapa")
    public String mapaDeHechos(Model model){
        List<Hecho> hechos = hechosService.obtenerHechos();
        List<Map<String, Object>> hechosParaElMapa = new ArrayList<>();

        for (Hecho h : hechos) {
            Map<String, Object> dto = new HashMap<>();
            dto.put("id", h.getId());
            dto.put("titulo", h.getTitulo());
            dto.put("categoria", h.getCategoria().getNombre());

            // Extraemos latitud y longitud manualmente y nos aseguramos de que no sean nulos
            if (h.getUbicacion() != null) {
                dto.put("lat", h.getUbicacion().getLatitud());
                dto.put("lng", h.getUbicacion().getLongitud());
                hechosParaElMapa.add(dto); // Solo enviamos los que tienen coordenadas
            }
        }

        // Enviamos esta lista simplificada a la vista
        model.addAttribute("listaHechos", hechosParaElMapa);

        return "mapa";
    }
    }

