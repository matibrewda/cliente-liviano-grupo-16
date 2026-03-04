package ar.utn.frba.ddsi.cliente_liviano.controllers;

import ar.utn.frba.ddsi.cliente_liviano.exceptions.NotFoundException;
import ar.utn.frba.ddsi.cliente_liviano.models.dto.HechoInputDTO;
import ar.utn.frba.ddsi.cliente_liviano.repositories.fuentedinamica.dto.SolicitudModificacionRequest;
import ar.utn.frba.ddsi.cliente_liviano.repositories.fuentedinamica.dto.SolicitudModificacionResponse;
import ar.utn.frba.ddsi.cliente_liviano.services.HechosService;
import ar.utn.frba.ddsi.cliente_liviano.services.SolicitudModificacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/solicitudes-modificacion")
public class SolicitudModificacionController {

    @Autowired
    private SolicitudModificacionService solicitudModificacionService;

    @Autowired
    private HechosService hechosService;

    @GetMapping
    public String listar(Model model) {
        List<SolicitudModificacionResponse> solicitudes = solicitudModificacionService.listar();
        model.addAttribute("titulo", "Solicitudes de modificación");
        model.addAttribute("solicitudes", solicitudes);
        return "solicitudes-modificacion/lista";
    }

    @GetMapping("/nueva")
    public String formularioNueva(
            @RequestParam(value = "hechoId", required = false) Long hechoId,
            Model model) {
        model.addAttribute("titulo", "Nueva solicitud de modificación");
        if (hechoId != null) {
            try {
                Optional<HechoInputDTO> hechoOpt = hechosService.obtenerHechoPorID(hechoId);
                hechoOpt.ifPresent(hecho -> {
                    model.addAttribute("idHecho", hecho.getId());
                    model.addAttribute("tituloHecho", hecho.getTitulo());
                    model.addAttribute("descripcion", hecho.getDescripcion());
                    model.addAttribute("categoriaNombre", hecho.getCategoria() != null ? hecho.getCategoria().getNombre() : "");
                    model.addAttribute("latitud", hecho.getUbicacion() != null ? hecho.getUbicacion().getLatitud() : "");
                    model.addAttribute("longitud", hecho.getUbicacion() != null ? hecho.getUbicacion().getLongitud() : "");
                    model.addAttribute("fechaAcontecimiento", hecho.getFechaAcontecimiento() != null ? hecho.getFechaAcontecimiento().toString() : "");
                });
            } catch (NotFoundException ignored) {
                model.addAttribute("idHecho", hechoId);
            }
        }
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
            return "redirect:/solicitudes-modificacion/nueva?hechoId=" + idHecho;
        }
        return "redirect:/solicitudes-modificacion";
    }

    @PutMapping("/aprobar/{idSolicitud}")
    public String aprobar(@PathVariable Long idSolicitud, RedirectAttributes redirectAttributes) {
        try {
            solicitudModificacionService.aprobar(idSolicitud);
            redirectAttributes.addFlashAttribute("mensaje", "Solicitud aprobada correctamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al aprobar: " + e.getMessage());
        }
        return "redirect:/solicitudes-modificacion";
    }
}
