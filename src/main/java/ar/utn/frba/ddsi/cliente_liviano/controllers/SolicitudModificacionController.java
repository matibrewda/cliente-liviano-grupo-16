package ar.utn.frba.ddsi.cliente_liviano.controllers;

import ar.utn.frba.ddsi.cliente_liviano.exceptions.NotFoundException;
import ar.utn.frba.ddsi.cliente_liviano.models.dto.HechoInputDTO;
import ar.utn.frba.ddsi.cliente_liviano.repositories.fuentedinamica.dto.AprobarSolicitudRequest;
import ar.utn.frba.ddsi.cliente_liviano.repositories.fuentedinamica.dto.SolicitudModificacionRequest;
import ar.utn.frba.ddsi.cliente_liviano.repositories.fuentedinamica.dto.SolicitudModificacionResponse;
import ar.utn.frba.ddsi.cliente_liviano.services.CategoriaService;
import ar.utn.frba.ddsi.cliente_liviano.services.HechosService;
import ar.utn.frba.ddsi.cliente_liviano.services.SolicitudModificacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin/solicitudes-modificacion")
public class SolicitudModificacionController {

    @Autowired
    private SolicitudModificacionService solicitudModificacionService;

    @Autowired
    private HechosService hechosService;

    @Autowired
    private CategoriaService categoriaService;

    @GetMapping
    public String listar(Model model) {
        List<SolicitudModificacionResponse> solicitudes = solicitudModificacionService.listar();
        model.addAttribute("titulo", "Solicitudes de modificación");
        model.addAttribute("solicitudes", solicitudes);
        model.addAttribute("categoriasDisponibles", categoriaService.getAll());
        return "solicitudes-modificacion/lista";
    }

    @GetMapping("/nueva")
    public String formularioNueva(
            @RequestParam(value = "hechoId", required = false) Long hechoId,
            @RequestParam(value = "volverA", required = false) String volverA,
            Model model) {
        model.addAttribute("titulo", "Nueva solicitud de modificación");
        if (volverA != null && !volverA.isBlank()) {
            model.addAttribute("volverA", volverA);
        }
        // Sin hechoId no prellenamos; con hechoId cargamos siempre los datos actuales del hecho
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
                    ? volverA : "/admin/solicitudes-modificacion/nueva?hechoId=" + idHecho;
            return "redirect:" + back;
        }
        if (volverA != null && !volverA.isBlank() && volverA.startsWith("/") && !volverA.startsWith("//")) {
            return "redirect:" + volverA;
        }
        return "redirect:/admin/solicitudes-modificacion";
    }

    @RequestMapping(value = "/aprobar/{idSolicitud}", method = {RequestMethod.PUT, RequestMethod.POST})
    public String aprobar(@PathVariable Long idSolicitud,
                          @RequestParam(value = "comentario", required = false) String comentario,
                          @RequestParam(value = "idHecho") Long idHecho,
                          @RequestParam(value = "titulo", required = false) String titulo,
                          @RequestParam(value = "descripcion", required = false) String descripcion,
                          @RequestParam(value = "categoriaNombre", required = false) String categoriaNombre,
                          @RequestParam(value = "latitud", required = false) String latitudStr,
                          @RequestParam(value = "longitud", required = false) String longitudStr,
                          @RequestParam(value = "fechaAcontecimiento", required = false) String fechaAcontecimiento,
                          @RequestParam(value = "motivo", required = false) String motivo,
                          RedirectAttributes redirectAttributes) {
        if (comentario == null || comentario.isBlank()) {
            redirectAttributes.addFlashAttribute("error", "El comentario es obligatorio.");
            return "redirect:/admin/solicitudes-modificacion";
        }
        try {
            Double latitud = (latitudStr != null && !latitudStr.isBlank()) ? Double.valueOf(latitudStr.trim()) : null;
            Double longitud = (longitudStr != null && !longitudStr.isBlank()) ? Double.valueOf(longitudStr.trim()) : null;
            AprobarSolicitudRequest request = new AprobarSolicitudRequest();
            request.setComentario(comentario);
            request.setIdHecho(idHecho);
            request.setTitulo(titulo);
            request.setDescripcion(descripcion);
            request.setCategoriaNombre(categoriaNombre);
            request.setUbicacion(latitud != null && longitud != null ? new SolicitudModificacionRequest.UbicacionDto(latitud, longitud) : null);
            request.setFechaAcontecimiento(fechaAcontecimiento);
            request.setMotivo(motivo);
            solicitudModificacionService.aprobar(idSolicitud, request);
            redirectAttributes.addFlashAttribute("mensaje", "Solicitud aprobada correctamente.");
        } catch (NumberFormatException e) {
            redirectAttributes.addFlashAttribute("error", "Latitud o longitud inválida.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al aprobar: " + e.getMessage());
        }
        return "redirect:/admin/solicitudes-modificacion";
    }

    @RequestMapping(value = "/rechazar/{idSolicitud}", method = {RequestMethod.PUT, RequestMethod.POST})
    public String rechazar(@PathVariable Long idSolicitud,
                           @RequestParam(value = "comentario", required = false) String comentario,
                           RedirectAttributes redirectAttributes) {
        if (comentario == null || comentario.isBlank()) {
            redirectAttributes.addFlashAttribute("error", "El comentario es obligatorio.");
            return "redirect:/admin/solicitudes-modificacion";
        }
        try {
            solicitudModificacionService.rechazar(idSolicitud, comentario);
            redirectAttributes.addFlashAttribute("mensaje", "Solicitud rechazada correctamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al rechazar: " + e.getMessage());
        }
        return "redirect:/admin/solicitudes-modificacion";
    }
}
