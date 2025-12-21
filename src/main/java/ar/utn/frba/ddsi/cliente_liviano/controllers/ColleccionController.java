package ar.utn.frba.ddsi.cliente_liviano.controllers;

import ar.utn.frba.ddsi.cliente_liviano.DTO.CategoriaDTO;
import ar.utn.frba.ddsi.cliente_liviano.DTO.HechoDTO;
import ar.utn.frba.ddsi.cliente_liviano.DTO.OrigenDTO;
import ar.utn.frba.ddsi.cliente_liviano.service.ColeccionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/colecciones")
public class ColleccionController {

    @Autowired
    private ColeccionService coleccionService;

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



}
