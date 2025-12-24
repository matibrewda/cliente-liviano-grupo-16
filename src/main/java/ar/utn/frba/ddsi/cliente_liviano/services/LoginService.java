package ar.utn.frba.ddsi.cliente_liviano.services;

import ar.utn.frba.ddsi.cliente_liviano.models.usuario.Usuario;
import ar.utn.frba.ddsi.cliente_liviano.models.repositories.LoginRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginService {
    @Autowired
    private LoginRepository loginRepository;

    public Usuario loginUser(Usuario usuario) {
        return this.loginRepository.login(usuario);
    }
}
