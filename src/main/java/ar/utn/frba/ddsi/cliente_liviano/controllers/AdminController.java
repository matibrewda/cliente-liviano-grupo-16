package ar.utn.frba.ddsi.cliente_liviano.controllers;

import ar.utn.frba.ddsi.cliente_liviano.DTO.ColeccionDTO;
import ar.utn.frba.ddsi.cliente_liviano.DTO.FiltroDto;
import ar.utn.frba.ddsi.cliente_liviano.DTO.ZonaDTO;
import ar.utn.frba.ddsi.cliente_liviano.DTO.input.ColeccionInputDTO;
import ar.utn.frba.ddsi.cliente_liviano.services.CategoriaService;
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

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AgregadorColeccionService coleccionService;
    @Autowired
    private CategoriaService categoriaService;

    @GetMapping("/colecciones/{coleccionId}")
    public String verDetalleColeccionAdmin(@PathVariable String coleccionId, Model model,
                                          RedirectAttributes redirectAttributes) {
        try {
            Long id = Long.parseLong(coleccionId);
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
            model.addAttribute("titulo", "Detalle de la Colección (Administración)");
            model.addAttribute("editable", true);
            return "colecciones/ABM/detalle-coleccion";
        } catch (NumberFormatException e) {
            return "redirect:/colecciones";
        }
    }

    @PostMapping("/colecciones/{coleccionId}/actualizar")
    public String actualizarTituloDescripcion(@PathVariable String coleccionId,
                                               @RequestParam String titulo,
                                               @RequestParam String descripcion,
                                               RedirectAttributes redirectAttributes) {
        if (titulo == null || titulo.isBlank()) {
            redirectAttributes.addFlashAttribute("error", "El título es obligatorio.");
            return "redirect:/admin/colecciones/" + coleccionId;
        }
        if (descripcion == null || descripcion.isBlank()) {
            redirectAttributes.addFlashAttribute("error", "La descripción es obligatoria.");
            return "redirect:/admin/colecciones/" + coleccionId;
        }
        try {
            Long id = Long.parseLong(coleccionId);
            ColeccionDTO actual = coleccionService.obtenerColeccionPorId(id);
            List<FiltroDto> filtros = actual.getFiltrosCriterioPertenencia();
            if (filtros == null || filtros.isEmpty()) {
                filtros = coleccionService.obtenerCriterioPertenencia(id);
            }
            ColeccionInputDTO input = new ColeccionInputDTO();
            input.setTitulo(titulo.trim());
            input.setDescripcion(descripcion.trim());
            input.setFiltros(filtros != null ? filtros : List.of());
            coleccionService.actualizarColeccion(id, input);
            redirectAttributes.addFlashAttribute("success", "Título y descripción actualizados.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al guardar: " + e.getMessage());
        }
        return "redirect:/admin/colecciones/" + coleccionId;
    }

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
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al guardar la configuración: " + e.getMessage());
            return "redirect:/admin/colecciones/" + coleccionId + "/consenso";
        }
        redirectAttributes.addFlashAttribute("success", "Configuración de consenso guardada exitosamente");
        return "redirect:/admin/colecciones/" + coleccionId;
    }

    @GetMapping("/colecciones/{coleccionId}/fuentes")
    public String configurarFuentes(@PathVariable String coleccionId, Model model) {
        try {
            ColeccionDTO coleccion = coleccionService.obtenerColeccionPorId(Long.parseLong(coleccionId));
            var fuentes = coleccionService.obtenerFuentes(coleccionId);
            model.addAttribute("titulo", "Configurar fuentes");
            model.addAttribute("coleccion", coleccion);
            model.addAttribute("fuenteProxy", fuentes.isFuenteProxy());
            model.addAttribute("fuenteEstatica", fuentes.isFuenteEstatica());
            model.addAttribute("fuenteDinamica", fuentes.isFuenteDinamica());
            return "colecciones/configurar-fuentes";
        } catch (NumberFormatException e) {
            return "redirect:/colecciones";
        }
    }

    @PostMapping("/colecciones/{coleccionId}/fuentes")
    public String guardarFuentes(@PathVariable String coleccionId,
                                 @RequestParam(name = "fuenteProxy", required = false) String fuenteProxy,
                                 @RequestParam(name = "fuenteEstatica", required = false) String fuenteEstatica,
                                 @RequestParam(name = "fuenteDinamica", required = false) String fuenteDinamica,
                                 RedirectAttributes redirectAttributes) {
        try {
            boolean proxy = fuenteProxy != null && "on".equalsIgnoreCase(fuenteProxy);
            boolean estatica = fuenteEstatica != null && "on".equalsIgnoreCase(fuenteEstatica);
            boolean dinamica = fuenteDinamica != null && "on".equalsIgnoreCase(fuenteDinamica);
            coleccionService.actualizarFuentes(coleccionId, proxy, estatica, dinamica);
            redirectAttributes.addFlashAttribute("success", "Configuración de fuentes guardada exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al guardar las fuentes: " + e.getMessage());
            return "redirect:/admin/colecciones/" + coleccionId + "/fuentes";
        }
        return "redirect:/admin/colecciones/" + coleccionId;
    }

    @GetMapping("/colecciones/{coleccionId}/criterio-pertenencia")
    public String configurarCriterioPertenencia(@PathVariable String coleccionId, Model model) {
        try {
            Long id = Long.parseLong(coleccionId);
            ColeccionDTO coleccion = coleccionService.obtenerColeccionPorId(id);
            List<FiltroDto> criterioPertenencia = coleccionService.obtenerCriterioPertenencia(id);

            String fechaDesde = null, fechaHasta = null;
            Long categoria = null;
            Double latitud = null;
            Double longitud = null;
            Double radioKm = null;
            if (criterioPertenencia != null) {
                for (FiltroDto f : criterioPertenencia) {
                    if ("filtroPorFechas".equals(f.getTipoFiltro())) {
                        if (f.getFechaAcontecimientoDesde() != null) fechaDesde = f.getFechaAcontecimientoDesde().toLocalDate().format(DateTimeFormatter.ISO_LOCAL_DATE);
                        if (f.getFechaAcontecimientoHasta() != null) fechaHasta = f.getFechaAcontecimientoHasta().toLocalDate().format(DateTimeFormatter.ISO_LOCAL_DATE);
                    } else if ("filtroPorCategoria".equals(f.getTipoFiltro()) && f.getCodigoCategoria() != null) {
                        categoria = f.getCodigoCategoria();
                    } else if ("filtroPorZona".equals(f.getTipoFiltro()) && f.getZona() != null) {
                        latitud = f.getZona().getLatitud();
                        longitud = f.getZona().getLongitud();
                        radioKm = f.getZona().getRadio();
                    }
                }
            }

            model.addAttribute("titulo", "Configurar criterios de pertenencia");
            model.addAttribute("coleccion", coleccion);
            model.addAttribute("criterioPertenencia", criterioPertenencia);
            model.addAttribute("categoriasDisponibles", categoriaService.getAll());
            model.addAttribute("fechaAcontecimientoDesde", fechaDesde);
            model.addAttribute("fechaAcontecimientoHasta", fechaHasta);
            model.addAttribute("categoria", categoria);
            model.addAttribute("latitud", latitud);
            model.addAttribute("longitud", longitud);
            model.addAttribute("radioKm", radioKm);
            return "colecciones/configurar-filtros";
        } catch (NumberFormatException e) {
            return "redirect:/colecciones";
        }
    }

    @PostMapping("/colecciones/{coleccionId}/criterio-pertenencia")
    public String guardarCriterioPertenencia(@PathVariable String coleccionId,
                                             @RequestParam(required = false) String fechaAcontecimientoDesde,
                                             @RequestParam(required = false) String fechaAcontecimientoHasta,
                                             @RequestParam(required = false) Long categoria,
                                             @RequestParam(required = false) Double latitud,
                                             @RequestParam(required = false) Double longitud,
                                             @RequestParam(required = false) Double radioKm,
                                             RedirectAttributes redirectAttributes) {
        try {
            Long id = Long.parseLong(coleccionId);
            List<FiltroDto> filtros = new ArrayList<>();

            if (fechaAcontecimientoDesde != null && !fechaAcontecimientoDesde.isBlank()
                    || fechaAcontecimientoHasta != null && !fechaAcontecimientoHasta.isBlank()) {
                FiltroDto filtroFecha = new FiltroDto();
                filtroFecha.setTipoFiltro("filtroPorFechas");
                filtroFecha.setCodigoCategoria(null);
                if (fechaAcontecimientoDesde != null && !fechaAcontecimientoDesde.isBlank())
                    filtroFecha.setFechaAcontecimientoDesde(LocalDate.parse(fechaAcontecimientoDesde).atStartOfDay());
                if (fechaAcontecimientoHasta != null && !fechaAcontecimientoHasta.isBlank())
                    filtroFecha.setFechaAcontecimientoHasta(LocalDate.parse(fechaAcontecimientoHasta).atTime(23, 59, 59));
                filtros.add(filtroFecha);
            }
            if (categoria != null && categoria > 0) {
                FiltroDto filtroCat = new FiltroDto();
                filtroCat.setTipoFiltro("filtroPorCategoria");
                filtroCat.setCodigoCategoria(categoria);
                filtros.add(filtroCat);
            }
            if (latitud != null && longitud != null && radioKm != null) {
                FiltroDto filtroZona = new FiltroDto();
                filtroZona.setTipoFiltro("filtroPorZona");
                filtroZona.setCodigoCategoria(null);
                filtroZona.setZona(new ZonaDTO(latitud, longitud, radioKm));
                filtros.add(filtroZona);
            }

            coleccionService.actualizarCriterioPertenencia(id, filtros);
            redirectAttributes.addFlashAttribute("success", "Criterios de pertenencia guardados exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al guardar: " + e.getMessage());
            return "redirect:/admin/colecciones/" + coleccionId;
        }
        return "redirect:/admin/colecciones/" + coleccionId;
    }
}

