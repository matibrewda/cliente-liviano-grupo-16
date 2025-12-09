package ar.utn.frba.ddsi.cliente_liviano.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthController {

    @GetMapping("/login")
    public String login() {
        return "login"; // login.html
    }

    @GetMapping("/registro")
    public String registro() {
        return "registro"; // registro.html
    }
}