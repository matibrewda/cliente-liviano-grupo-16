package ar.utn.frba.ddsi.cliente_liviano.controllers;

import ar.utn.frba.ddsi.cliente_liviano.DTO.CategoriaDTO;
import ar.utn.frba.ddsi.cliente_liviano.DTO.ColeccionDTO;
import ar.utn.frba.ddsi.cliente_liviano.DTO.HechoDTO;
import ar.utn.frba.ddsi.cliente_liviano.DTO.input.ColeccionInputDTO;
import ar.utn.frba.ddsi.cliente_liviano.exceptions.NotFoundException;
import ar.utn.frba.ddsi.cliente_liviano.servicesAgregador.AgregadorColeccionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
@Controller
@RequestMapping("/colecciones")
public class ColleccionController {
    @Autowired
    private AgregadorColeccionService coleccionService;

    @GetMapping
    public String listarColecciones(Model model) {
        List<ColeccionDTO> coleccionesDTO = coleccionService.obtenerColecciones();
        model.addAttribute("colecciones", coleccionesDTO);
        model.addAttribute("totalColecciones", coleccionesDTO.size());
        return "colecciones/lista";
    }

    @GetMapping("/{coleccionId}/hechos/{hechoId}")
    public String verHechoPorID(@PathVariable String coleccionId, @PathVariable String hechoId, Model model) {
        HechoDTO hecho = coleccionService.obtenerHechoPorColeccionId(coleccionId, hechoId);
        model.addAttribute("hecho", hecho);
        model.addAttribute("coleccionId", coleccionId);

        return "colecciones/detalle-hecho-coleccion";
    }

    @GetMapping("/{coleccionId}/hechos")
    public String listarHechosDeColeccion(
            @PathVariable String coleccionId,
            @RequestParam(required = false) String fechaIncidenteDesde,
            @RequestParam(required = false) String fechafechaIncidenteDesdeHasta,
            @RequestParam(required = false) String ubicacion,
            @RequestParam(required = false) String categoria,
            @RequestParam(required = false) String origen,
            @RequestParam(required = false) String modoNavegacion,
            Model model
    ) {
        List<HechoDTO> hechos = coleccionService.obtenerHechosPorColeccion(
                coleccionId, fechaIncidenteDesde, fechaIncidenteDesde, ubicacion, categoria, origen, modoNavegacion
        );

        model.addAttribute("coleccionId", coleccionId);
        model.addAttribute("hechos", hechos);

        List<CategoriaDTO> categoriasDisponibles = hechos.stream()
                .map(h -> h.getCategoriaDTO())
                .filter(Objects::nonNull)
                .distinct()
                .toList();
        List<String> origenesDisponibles = hechos.stream()
                .map(HechoDTO::getOrigen)
                .filter(Objects::nonNull)
                .distinct()
                .toList();

        model.addAttribute("categoriasDisponibles", categoriasDisponibles);
        model.addAttribute("origenesDisponibles", origenesDisponibles);
        model.addAttribute("modoActual", modoNavegacion);

        return "colecciones/hechos";
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioCrearColeccion(Model model) {
        ColeccionDTO coleccionDTO = new ColeccionDTO();
        model.addAttribute("coleccion", coleccionDTO);
        model.addAttribute("titulo", "Crear Nueva Colección");
        return "colecciones/ABM/crear-coleccion";
    }

    @PostMapping("/crear")
    public String crearColeccion(
            @ModelAttribute("coleccion") ColeccionInputDTO coleccion,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("titulo", "Crear Nueva Colección");
            return "colecciones/ABM/crear-coleccion";
        }

        ColeccionDTO coleccionCreada = this.coleccionService.crear(coleccion);

        redirectAttributes.addFlashAttribute(
                "mensaje",
                "Colección \"" + coleccionCreada.getTitulo() + "\" creada exitosamente"
        );
        redirectAttributes.addFlashAttribute("tipoMensaje", "success");

        return "redirect:/colecciones/" + coleccionCreada.getHandle();

    }

    @GetMapping("/{id}")
    public String verDetalleColeccion(
            @PathVariable Long id,
            Model model,
            RedirectAttributes redirectAttributes) {

        try {
            ColeccionDTO coleccion = coleccionService.obtenerColeccionPorId(id);

            model.addAttribute("coleccion", coleccion);
            model.addAttribute("titulo", "Detalle de la Colección");

            return "colecciones/ABM/detalle-coleccion";
        }
        catch (NotFoundException ex) {
            redirectAttributes.addFlashAttribute("mensaje", ex.getMessage());
            return "redirect:/404";
        }
    }



}