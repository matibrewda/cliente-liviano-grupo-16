package ar.utn.frba.ddsi.cliente_liviano.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private AuthInterceptor authInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // Aplica el interceptor a TODAS las rutas (el interceptor ya decide cuáles ignorar)
        registry.addInterceptor(authInterceptor).addPathPatterns("/**");
    }
}
