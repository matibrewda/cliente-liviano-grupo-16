package ar.utn.frba.ddsi.cliente_liviano.controllers;

import ar.utn.frba.ddsi.cliente_liviano.DTO.*;
import ar.utn.frba.ddsi.cliente_liviano.DTO.input.ColeccionInputDTO;
import ar.utn.frba.ddsi.cliente_liviano.DTO.input.ColeccionInputViewDTO;
import ar.utn.frba.ddsi.cliente_liviano.exceptions.AgregadorApiException;
import ar.utn.frba.ddsi.cliente_liviano.exceptions.NotFoundException;
import ar.utn.frba.ddsi.cliente_liviano.models.Coleccion;
import ar.utn.frba.ddsi.cliente_liviano.models.Hecho;
import ar.utn.frba.ddsi.cliente_liviano.services.CategoriaService;
import ar.utn.frba.ddsi.cliente_liviano.servicesAgregador.AgregadorColeccionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
@Controller
@RequestMapping("/colecciones")
public class ColleccionController {
    @Autowired
    private AgregadorColeccionService coleccionService;
    @Autowired
    private CategoriaService categoriaService;

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
            @RequestParam(required = false) String fechaIncidenteHasta,
            @RequestParam(required = false) String categoria,
            @RequestParam(required = false) String origen,
            @RequestParam(required = false) String modoNavegacion,
            @RequestParam(required = false) String latitud,
            @RequestParam(required = false) String longitud,
            @RequestParam(required = false) String radioKm,
            HttpServletRequest request,
            Model model
    ) {
            // ---- Validación fechas ----
        if (fechaIncidenteDesde != null && !fechaIncidenteDesde.isBlank() &&
                fechaIncidenteHasta != null && !fechaIncidenteHasta.isBlank()) {

            try {
                LocalDate desde = LocalDate.parse(fechaIncidenteDesde);
                LocalDate hasta = LocalDate.parse(fechaIncidenteHasta);
                LocalDate hoy = LocalDate.now();

                // Fecha Desde no puede ser mayor que Hasta
                if (desde.isAfter(hasta)) {
                    model.addAttribute("errorFechas",
                            "Fecha Acontecimiento Desde no puede ser posterior a la Fecha Acontecimiento Hasta.");
                    fechaIncidenteHasta = null;
                    fechaIncidenteDesde = null;
                }
                //Fecha Hasta no puede ser mayor que hoy
                else if (hasta.isAfter(hoy)) {
                    model.addAttribute("errorFechas",
                            "La Fecha Acontecimiento Hasta no puede ser posterior a la fecha actual.");

                    fechaIncidenteHasta = null;
                }

            } catch (Exception e) {
                model.addAttribute("errorFechas",
                        "Formato de fecha inválido.");

                fechaIncidenteDesde = null;
                fechaIncidenteHasta = null;
            }
        }

        // ---- Validación GEO: si completa uno, deben venir los 3 ----
        boolean hasLat = latitud != null && !latitud.trim().isEmpty();
        boolean hasLon = longitud != null && !longitud.trim().isEmpty();
        boolean hasRadio = radioKm != null && !radioKm.trim().isEmpty();

        boolean anyGeo = hasLat || hasLon || hasRadio;
        boolean allGeo = hasLat && hasLon && hasRadio;

        if (anyGeo && !allGeo) {
            model.addAttribute("errorGeo",
                    "Si completás latitud, longitud o radio, debés completar los tres campos.");
        }

        // ---- Construcción del filtro "ubicacion" en formato CSV: lat,long,radio ----
        // (solo si allGeo; si no, se manda vacío y no filtra por zona)
        String ubicacion = "";
        if (allGeo) {
            ubicacion = latitud.trim() + ";" + longitud.trim() + ";" + radioKm.trim();
        }

        // ---- Traer hechos filtrados ----
        List<HechoDTO> hechos;
        try {
            hechos = coleccionService.obtenerHechosPorColeccion(
                    coleccionId,
                    fechaIncidenteDesde,
                    fechaIncidenteHasta,
                    ubicacion,
                    categoria,
                    origen,
                    modoNavegacion
            );
        } catch (Exception e) {
            hechos = Collections.emptyList();
            model.addAttribute("errorHechos", "Error al cargar los hechos: " + e.getMessage());
        }
        if (hechos == null) {
            hechos = Collections.emptyList();
        }

        // ---- URL con filtros (para volver después de crear solicitud de modificación) ----
        String returnUrl = request.getRequestURI();
        if (request.getQueryString() != null && !request.getQueryString().isEmpty()) {
            returnUrl = returnUrl + "?" + request.getQueryString();
        }
        model.addAttribute("returnUrl", returnUrl);

        // ---- Model básico ----
        model.addAttribute("coleccionId", coleccionId);
        model.addAttribute("hechos", hechos);

        // ---- Opciones de filtros (según los hechos resultantes) ----
        List<CategoriaDTO> categoriasDisponibles = hechos.stream()
                .map(HechoDTO::getCategoriaDTO)
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
        model.addAttribute("fechaIncidenteDesde", fechaIncidenteDesde);
        model.addAttribute("fechaIncidenteHasta", fechaIncidenteHasta);
        model.addAttribute("latitud", latitud);
        model.addAttribute("longitud", longitud);
        model.addAttribute("radioKm", radioKm);

        return "colecciones/hechos";
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioCrearColeccion(Model model) {
        ColeccionInputViewDTO coleccionDTO = new ColeccionInputViewDTO();
        model.addAttribute("coleccion", coleccionDTO);
        model.addAttribute("titulo", "Crear Nueva Colección");

        var categoriasDisponibles = categoriaService.getAll();

        model.addAttribute("categoriasDisponibles", categoriasDisponibles);
        return "colecciones/ABM/crear-coleccion";
    }

    @PostMapping("/crear")
    public String crearColeccion(
            @ModelAttribute("coleccion") ColeccionInputViewDTO viewDto,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("titulo", "Crear Nueva Colección");
            model.addAttribute("categoriasDisponibles", categoriaService.getAll());
            return "colecciones/ABM/crear-coleccion";
        }

        List<FiltroDto> filtros = new ArrayList<>();
        String fechaDesde = viewDto.getFechaAcontecimientoDesde();
        String fechaHasta = viewDto.getFechaAcontecimientoHasta();
        boolean hasFechas = fechaDesde != null && !fechaDesde.isBlank() || fechaHasta != null && !fechaHasta.isBlank();
        if (hasFechas) {
            var filtroFecha = new FiltroDto();
            filtroFecha.setTipoFiltro("filtroPorFechas");
            filtroFecha.setCodigoCategoria(null);
            if (fechaDesde != null && !fechaDesde.isBlank()) {
                filtroFecha.setFechaAcontecimientoDesde(LocalDate.parse(fechaDesde).atStartOfDay());
            }
            if (fechaHasta != null && !fechaHasta.isBlank()) {
                filtroFecha.setFechaAcontecimientoHasta(LocalDate.parse(fechaHasta).atTime(23, 59, 59));
            }
            filtros.add(filtroFecha);
        }

        if (viewDto.getCategoria() != null && viewDto.getCategoria() > 0) {
            var filtroCategoria = new FiltroDto();
            filtroCategoria.setTipoFiltro("filtroPorCategoria");
            filtroCategoria.setCodigoCategoria(viewDto.getCategoria());
            filtros.add(filtroCategoria);
        }

        if (viewDto.getLatitud() != null && viewDto.getLongitud() != null && viewDto.getRadioKm() != null) {
            var filtroZona = new FiltroDto();
            filtroZona.setTipoFiltro("filtroPorZona");
            filtroZona.setCodigoCategoria(null);
            filtroZona.setZona(new ZonaDTO(viewDto.getLatitud(), viewDto.getLongitud(), viewDto.getRadioKm()));
            filtros.add(filtroZona);
        }

        var coleccion = new ColeccionInputDTO();
        coleccion.setTitulo(viewDto.getTitulo() != null ? viewDto.getTitulo().trim() : "");
        coleccion.setDescripcion(viewDto.getDescripcion() != null ? viewDto.getDescripcion().trim() : "");
        coleccion.setFiltros(filtros);

        try {
            ColeccionDTO coleccionCreada = this.coleccionService.crear(coleccion);
            String handle = coleccionCreada.getHandle();

            if (viewDto.getTipoConsenso() != null && !viewDto.getTipoConsenso().isBlank()) {
                try {
                    coleccionService.actualizarConsenso(handle, viewDto.getTipoConsenso());
                } catch (Exception ignored) { }
            }
            try {
                boolean proxy = Boolean.TRUE.equals(viewDto.getFuenteProxy());
                boolean estatica = Boolean.TRUE.equals(viewDto.getFuenteEstatica());
                boolean dinamica = Boolean.TRUE.equals(viewDto.getFuenteDinamica());
                coleccionService.actualizarFuentes(handle, proxy, estatica, dinamica);
            } catch (Exception ignored) { }

            redirectAttributes.addFlashAttribute("mensaje", "Colección \"" + coleccionCreada.getTitulo() + "\" creada exitosamente");
            redirectAttributes.addFlashAttribute("tipoMensaje", "success");
            return "redirect:/colecciones";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensaje", "No se pudo crear la colección. " + (e.getMessage() != null ? e.getMessage() : "Error en el servidor."));
            redirectAttributes.addFlashAttribute("tipoMensaje", "danger");
            return "redirect:/colecciones/nuevo";
        }
    }

    @GetMapping("/{id}")
    public String verDetalleColeccion(
            @PathVariable Long id,
            Model model,
            RedirectAttributes redirectAttributes) {

        try {
            ColeccionDTO coleccion = coleccionService.obtenerColeccionPorId(id);
            var fuentesConfig = coleccionService.obtenerFuentes(coleccion.getHandle());
            List<FiltroDto> criterioPertenencia = coleccion.getFiltrosCriterioPertenencia();
            if (criterioPertenencia == null || criterioPertenencia.isEmpty()) {
                criterioPertenencia = coleccionService.obtenerCriterioPertenencia(id);
            }

            String fechaAcontecimientoDesde = null, fechaAcontecimientoHasta = null;
            Long categoria = null;
            Double latitud = null;
            Double longitud = null;
            Double radioKm = null;
            if (criterioPertenencia != null) {
                for (FiltroDto f : criterioPertenencia) {
                    if ("filtroPorFechas".equals(f.getTipoFiltro())) {
                        if (f.getFechaAcontecimientoDesde() != null) fechaAcontecimientoDesde = f.getFechaAcontecimientoDesde().toLocalDate().format(DateTimeFormatter.ISO_LOCAL_DATE);
                        if (f.getFechaAcontecimientoHasta() != null) fechaAcontecimientoHasta = f.getFechaAcontecimientoHasta().toLocalDate().format(DateTimeFormatter.ISO_LOCAL_DATE);
                    } else if ("filtroPorCategoria".equals(f.getTipoFiltro()) && f.getCodigoCategoria() != null) {
                        categoria = f.getCodigoCategoria();
                    } else if ("filtroPorZona".equals(f.getTipoFiltro()) && f.getZona() != null) {
                        latitud = f.getZona().getLatitud();
                        longitud = f.getZona().getLongitud();
                        radioKm = f.getZona().getRadio();
                    }
                }
            }

            model.addAttribute("coleccion", coleccion);
            model.addAttribute("fuentesConfig", fuentesConfig);
            model.addAttribute("criterioPertenencia", criterioPertenencia);
            model.addAttribute("categoriasDisponibles", categoriaService.getAll());
            model.addAttribute("fechaAcontecimientoDesde", fechaAcontecimientoDesde);
            model.addAttribute("fechaAcontecimientoHasta", fechaAcontecimientoHasta);
            model.addAttribute("categoria", categoria);
            model.addAttribute("latitud", latitud);
            model.addAttribute("longitud", longitud);
            model.addAttribute("radioKm", radioKm);
            model.addAttribute("titulo", "Detalle de la Colección");
            model.addAttribute("editable", false);

            return "colecciones/ABM/detalle-coleccion";
        }
        catch (NotFoundException ex) {
            redirectAttributes.addFlashAttribute("mensaje", ex.getMessage());
            return "redirect:/404";
        }
    }

    @GetMapping("/{id}/editar")
    public String mostrarFormularioEditarColeccion(@PathVariable Long id,
                                                   Model model,
                                                   RedirectAttributes redirectAttributes) {

        try {
            ColeccionDTO coleccionDTO = coleccionService.obtenerColeccionPorId(id);

            model.addAttribute("coleccion", coleccionDTO);
            model.addAttribute("titulo", "Editar Coleccion");

            return "colecciones/ABM/editar-coleccion";
        }catch (NotFoundException ex) {
            redirectAttributes.addFlashAttribute("mensaje", ex.getMessage());
            return "redirect:/404";
        }

    }


    @PostMapping("/{id}/actualizar")
    public String actualizarColeccion(@PathVariable Long id,
                                      @ModelAttribute("coleccion") ColeccionInputDTO coleccionInputDTO,
                                      BindingResult bindingResult,
                                      Model model,
                                      RedirectAttributes redirectAttributes){

        try {
            ColeccionDTO coleccionActualizada = coleccionService.actualizarColeccion(id, coleccionInputDTO);

            redirectAttributes.addFlashAttribute("mensaje", "Coleccion actualizado exitosamente");
            redirectAttributes.addFlashAttribute("tipoMensaje", "success");
            return "redirect:/colecciones/" + coleccionActualizada.getHandle();
        }
        catch (NotFoundException ex) {
            redirectAttributes.addFlashAttribute("mensaje", ex.getMessage());
            return "redirect:/404";
        }
    }

    @GetMapping("/{id:\\d+}/eliminar")
    public String confirmarEliminar(@PathVariable Long id, Model model) {
        var coleccion = coleccionService.obtenerColeccionPorId(id);
        model.addAttribute("coleccion", coleccion);
        model.addAttribute("titulo", "Eliminar colección");
        return "colecciones/ABM/confirmar-eliminacion";
    }

    @PostMapping("/{id:\\d+}/eliminar")
    public String eliminar(@PathVariable Long id, RedirectAttributes redirectAttributes,Model model) {
        try {
            coleccionService.eliminar(id);
            redirectAttributes.addFlashAttribute("ok", "Colección eliminada correctamente.");
            return "redirect:/colecciones";

        } catch (AgregadorApiException e) {
            var coleccion = coleccionService.obtenerColeccionPorId(id);
            model.addAttribute("coleccion", coleccion);
            model.addAttribute("titulo", "Eliminar colección");
            model.addAttribute("error", "No se pudo eliminar la colección. Por favor, intente más tarde.");
            return "colecciones/ABM/confirmar-eliminacion";
        }
}


}