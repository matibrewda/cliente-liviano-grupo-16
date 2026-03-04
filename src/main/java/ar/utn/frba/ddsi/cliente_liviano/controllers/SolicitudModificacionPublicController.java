package ar.utn.frba.ddsi.cliente_liviano.controllers;

import ar.utn.frba.ddsi.cliente_liviano.exceptions.NotFoundException;
import ar.utn.frba.ddsi.cliente_liviano.models.dto.HechoInputDTO;
import ar.utn.frba.ddsi.cliente_liviano.repositories.fuentedinamica.dto.SolicitudModificacionRequest;
import ar.utn.frba.ddsi.cliente_liviano.services.CategoriaService;
import ar.utn.frba.ddsi.cliente_liviano.services.HechosService;
import ar.utn.frba.ddsi.cliente_liviano.services.SolicitudModificacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/solicitudes-modificacion")
public class SolicitudModificacionPublicController {

    @Autowired
    private SolicitudModificacionService solicitudModificacionService;

    @Autowired
    private HechosService hechosService;

    @Autowired
    private CategoriaService categoriaService;

    @GetMapping("/nueva")
    public String formularioNueva(
            @RequestParam(value = "hechoId", required = false) Long hechoId,
            @RequestParam(value = "volverA", required = false) String volverA,
            Model model) {
        model.addAttribute("titulo", "Nueva solicitud de modificación");
        if (volverA != null && !volverA.isBlank()) {
            model.addAttribute("volverA", volverA);
        }
        if (hechoId == null) {
            model.addAttribute("tituloHecho", "");
            model.addAttribute("descripcion", "");
            model.addAttribute("categoriaNombre", "");
            model.addAttribute("latitud", "");
            model.addAttribute("longitud", "");
            model.addAttribute("fechaAcontecimiento", "");
        } else {
            try {
                HechoInputDTO hecho = hechosService.obtenerHechoPorID(hechoId).orElse(null);
                if (hecho != null) {
                    model.addAttribute("idHecho", hecho.getId());
                    model.addAttribute("tituloHecho", hecho.getTitulo() != null ? hecho.getTitulo() : "");
                    model.addAttribute("descripcion", hecho.getDescripcion() != null ? hecho.getDescripcion() : "");
                    model.addAttribute("categoriaNombre", hecho.getCategoria() != null && hecho.getCategoria().getNombre() != null ? hecho.getCategoria().getNombre() : "");
                    model.addAttribute("latitud", hecho.getUbicacion() != null ? hecho.getUbicacion().getLatitud() : "");
                    model.addAttribute("longitud", hecho.getUbicacion() != null ? hecho.getUbicacion().getLongitud() : "");
                    model.addAttribute("fechaAcontecimiento", hecho.getFechaAcontecimiento() != null ? hecho.getFechaAcontecimiento().toString() : "");
                } else {
                    model.addAttribute("idHecho", hechoId);
                    model.addAttribute("tituloHecho", "");
                    model.addAttribute("descripcion", "");
                    model.addAttribute("categoriaNombre", "");
                    model.addAttribute("latitud", "");
                    model.addAttribute("longitud", "");
                    model.addAttribute("fechaAcontecimiento", "");
                }
            } catch (NotFoundException e) {
                model.addAttribute("idHecho", hechoId);
                model.addAttribute("tituloHecho", "");
                model.addAttribute("descripcion", "");
                model.addAttribute("categoriaNombre", "");
                model.addAttribute("latitud", "");
                model.addAttribute("longitud", "");
                model.addAttribute("fechaAcontecimiento", "");
            }
        }
        model.addAttribute("categoriasDisponibles", categoriaService.getAll());
        return "solicitudes-modificacion/formulario";
    }

    @PostMapping("/crear")
    public String crear(
            @RequestParam Long idHecho,
            @RequestParam String titulo,
            @RequestParam String descripcion,
            @RequestParam String categoriaNombre,
            @RequestParam Double latitud,
            @RequestParam Double longitud,
            @RequestParam String fechaAcontecimiento,
            @RequestParam String motivo,
            @RequestParam(value = "volverA", required = false) String volverA,
            RedirectAttributes redirectAttributes) {

        SolicitudModificacionRequest request = new SolicitudModificacionRequest();
        request.setIdHecho(idHecho);
        request.setTitulo(titulo);
        request.setDescripcion(descripcion);
        request.setCategoriaNombre(categoriaNombre);
        request.setUbicacion(new SolicitudModificacionRequest.UbicacionDto(latitud, longitud));
        request.setFechaAcontecimiento(fechaAcontecimiento);
        request.setFechaCarga(null);
        request.setDatosMultimedia(null);
        request.setMultimediaRuta(null);
        request.setMultimediaUrl(null);
        request.setMotivo(motivo);

        try {
            solicitudModificacionService.crear(request);
            redirectAttributes.addFlashAttribute("mensaje", "Solicitud de modificación creada correctamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al crear la solicitud: " + e.getMessage());
            String back = (volverA != null && !volverA.isBlank() && volverA.startsWith("/") && !volverA.startsWith("//"))
                    ? volverA : "/solicitudes-modificacion/nueva?hechoId=" + idHecho;
            return "redirect:" + back;
        }
        if (volverA != null && !volverA.isBlank() && volverA.startsWith("/") && !volverA.startsWith("//")) {
            return "redirect:" + volverA;
        }
        return "redirect:/hechos";
    }
}
