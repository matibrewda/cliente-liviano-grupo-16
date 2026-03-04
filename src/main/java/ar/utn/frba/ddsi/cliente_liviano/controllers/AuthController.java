package ar.utn.frba.ddsi.cliente_liviano.controllers;

import ar.utn.frba.ddsi.cliente_liviano.models.dto.LoginRequestDTO;
import ar.utn.frba.ddsi.cliente_liviano.services.LoginService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthController {
    @Autowired
    private LoginService loginService;

    @Value("${login.base-url}")
    private String backendUrl;

    @GetMapping("/login")
    public String login(Model model) {
        LoginRequestDTO req = new LoginRequestDTO();
        model.addAttribute("user", req);
        return "login"; // login.html
    }

    @PostMapping("/auth/login")
    public String login(@ModelAttribute("user") LoginRequestDTO req,
                        HttpSession session) {

        var loginResponse = loginService.login(req);

        session.setAttribute("TOKEN", loginResponse.getToken());
        session.setAttribute("NOMBRE_REAL", loginResponse.getNombreReal());
        session.setAttribute("ROLES", loginResponse.getRoles().toArray());

        return "redirect:/";
    }

    @GetMapping("/registro")
    public String registro(Model model) {
        model.addAttribute("backendUrl", backendUrl);
        return "registro"; // registro.html
    }

    @PostMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();// Destruye la sesión y borra las variables
        return "redirect:/";
    }
}