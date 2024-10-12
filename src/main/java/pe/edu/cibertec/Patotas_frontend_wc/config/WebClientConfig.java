package pe.edu.cibertec.Patotas_frontend_wc.config;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Configuration
public class WebClientConfig {
    @Bean
    public WebClient webClientAutenticacion(WebClient.Builder builder) {

        // Configuracion timeout en HttpClient Netty
        HttpClient httpClient = HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10000) // TimeOut de conexion (10s == 10 000 milis)
                .responseTimeout(Duration.ofSeconds(10)) // TimeOut de lectura de respuesta
                .doOnConnected(conn -> conn.addHandlerLast(new ReadTimeoutHandler(10, TimeUnit.SECONDS))); // TimeOut de lectura de cada paquete 

        return builder
                .baseUrl("http://localhost:8081/autenticacion")
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
    }

    @Bean
    public WebClient webClientFinanzas(WebClient.Builder builder) {

        // Configuracion timeout en HttpClient Netty
        HttpClient httpClient = HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10000) // TimeOut de conexion (10s == 10 000 milis)
                .responseTimeout(Duration.ofSeconds(5)) // TimeOut de lectura de respuesta
                .doOnConnected(conn -> conn.addHandlerLast(new ReadTimeoutHandler(10, TimeUnit.SECONDS))); // TimeOut de lectura de cada paquete

        return builder
                .baseUrl("http://localhost:8081/autenticacion")
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
    }

    @Bean
    public WebClient webClientReportes(WebClient.Builder builder) {

        // Configuracion timeout en HttpClient Netty
        HttpClient httpClient = HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10000) // TimeOut de conexion (10s == 10 000 milis)
                .responseTimeout(Duration.ofSeconds(10)) // TimeOut de lectura de respuesta
                .doOnConnected(conn -> conn.addHandlerLast(new ReadTimeoutHandler(10, TimeUnit.SECONDS))); // TimeOut de lectura de cada paquete

        return builder
                .baseUrl("http://localhost:8081/autenticacion")
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
    }

}
