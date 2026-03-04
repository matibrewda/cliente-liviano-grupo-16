package ar.utn.frba.ddsi.cliente_liviano.controllers;

import ar.utn.frba.ddsi.cliente_liviano.DTO.ActividadDTO;
import ar.utn.frba.ddsi.cliente_liviano.services.ActividadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/actividad")
public class ActividadController {

    @Autowired
    private ActividadService actividadService;

    @GetMapping
    public String listarActividad(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaDesde,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaHasta,
            Model model
    ) {

        if (fechaDesde == null) {
            fechaDesde = LocalDate.now().minusWeeks(1);
        }

        if (fechaHasta == null) {
            fechaHasta = LocalDate.now().plusDays(1);
        }

        List<ActividadDTO> actividades =
                actividadService.obtenerActividades(
                        fechaDesde.atStartOfDay(),
                        fechaHasta.atTime(23, 59, 59)
                );

        model.addAttribute("actividades", actividades);
        model.addAttribute("fechaDesde", fechaDesde);
        model.addAttribute("fechaHasta", fechaHasta);

        return "actividad/control-actividad";
    }
}
