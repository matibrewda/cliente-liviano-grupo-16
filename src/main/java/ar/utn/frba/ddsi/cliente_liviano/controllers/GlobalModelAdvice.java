package ar.utn.frba.ddsi.cliente_liviano.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalModelAdvice {

    @Value("${backend.api.url}")
    private String backendUrl;

    @Value("${agregador.base-url}")
    private String agregadorUrl;

    @ModelAttribute("backendUrl")
    public String backendUrl() {
        return backendUrl;
    }

    @ModelAttribute("agregadorUrl")
    public String agregadorUrl() {
        return agregadorUrl;
    }
}