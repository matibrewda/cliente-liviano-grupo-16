package ar.utn.frba.ddsi.cliente_liviano.controllers;

import ar.utn.frba.ddsi.cliente_liviano.models.Categoria;
import ar.utn.frba.ddsi.cliente_liviano.models.Hecho;
import ar.utn.frba.ddsi.cliente_liviano.models.Ubicacion;
import ar.utn.frba.ddsi.cliente_liviano.models.dto.LoginRequestDTO;
import ar.utn.frba.ddsi.cliente_liviano.models.usuario.Usuario;
import ar.utn.frba.ddsi.cliente_liviano.services.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

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
                        BindingResult bindingResult,
                        Model model,
                        RedirectAttributes redirectAttributes) {

        Usuario userLogeado = loginService.loginUser(req.toUser());

        redirectAttributes.addFlashAttribute("token", userLogeado.getToken());
        redirectAttributes.addFlashAttribute("username", userLogeado.getUsername());
        redirectAttributes.addFlashAttribute("nombreReal", userLogeado.getNombre());

        return "redirect:/";
    }

    @GetMapping("/registro")
    public String registro(Model model) {
        model.addAttribute("backendUrl", backendUrl);
        return "registro"; // registro.html
    }
}