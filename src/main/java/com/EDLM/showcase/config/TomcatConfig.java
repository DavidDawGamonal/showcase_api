package com.EDLM.showcase.config;

import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuración personalizada del servidor Tomcat embebido.
 * Aumenta el límite máximo de tamaño de las peticiones POST para permitir
 * la subida de archivos de audio e imágenes de portada desde el frontend.
 * Sin esta configuración, Tomcat rechaza peticiones multipart superiores a 2 MB.
 *
 * @author Elvis David Lara Manrique <a href="mailto:david.larman.1@educa.jcyl.es">david.larman.1@educa.jcyl.es</a>
 * @version 1.0
 * @since 1.0
 */
@Configuration
public class TomcatConfig {

    /**
     * Personaliza el conector HTTP de Tomcat para ampliar el límite de tamaño
     * máximo de las peticiones POST a 50 MB.
     * El valor se expresa en bytes: 52428800 = 50 * 1024 * 1024.
     *
     * @return El customizer que aplica la configuración al factory de Tomcat
     */
    @Bean
    public WebServerFactoryCustomizer<TomcatServletWebServerFactory> tomcatCustomizer() {
        return factory -> {
            factory.addConnectorCustomizers(connector -> {
                // 52428800 bytes = 50 MB — límite máximo para subida de instrumentales y portadas
                connector.setProperty("maxPostSize", "52428800");
                // connector.setProperty("maxPostSize", "-1"); // deshabilitar límite (solo para pruebas)
            });
        };
    }
}