package ar.utn.frba.ddsi.cliente_liviano.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String uri = request.getRequestURI();
        HttpSession session = request.getSession();

        if (uri.startsWith("/css") || uri.startsWith("/js") || uri.equals("/") || uri.startsWith("/login")) {
            return true;
        }


        // 3. Proteger rutas específicas para el ADMIN (ej. Carga Masiva)
        if (uri.startsWith("/hechos/carga-masiva")) {
            // 1. Extraemos el atributo con el nombre exacto que usaste ("ROLES") y lo tratamos como un Array
            Object[] roles = (Object[]) session.getAttribute("ROLES");
            boolean esAdmin = false;

            // 2. Recorremos el Array para ver si tiene el rol de administrador
            if (roles != null) {
                for (Object rolObj : roles) {
                    String nombreRol = String.valueOf(rolObj);
                    // Validamos "ADMIN" o "ROLE_ADMIN" por si Spring le agrega el prefijo automáticamente
                    if ("ADMIN".equals(nombreRol) || "ROLE_ADMIN".equals(nombreRol)) {
                        esAdmin = true;
                        break;
                    }
                }
            }

            // 3. Si terminó de buscar y no es ADMIN, lo pateamos
            if (!esAdmin) {
                response.sendRedirect("/hechos");
                return false;
            }
        }

        if (uri.startsWith("/admin")) {
            // 1. Extraemos el atributo con el nombre exacto que usaste ("ROLES") y lo tratamos como un Array
            Object[] roles = (Object[]) session.getAttribute("ROLES");
            boolean esAdmin = false;

            // 2. Recorremos el Array para ver si tiene el rol de administrador
            if (roles != null) {
                for (Object rolObj : roles) {
                    String nombreRol = String.valueOf(rolObj);
                    // Validamos "ADMIN" o "ROLE_ADMIN" por si Spring le agrega el prefijo automáticamente
                    if ("ADMIN".equals(nombreRol) || "ROLE_ADMIN".equals(nombreRol)) {
                        esAdmin = true;
                        break;
                    }
                }
            }

            // 3. Si terminó de buscar y no es ADMIN, lo pateamos
            if (!esAdmin) {
                response.sendRedirect("/colecciones");
                return false;
            }
        }

        // Si pasó todas las validaciones, lo dejamos seguir
        return true;
    }
}