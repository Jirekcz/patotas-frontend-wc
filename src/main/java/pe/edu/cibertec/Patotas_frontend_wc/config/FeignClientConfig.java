package pe.edu.cibertec.Patotas_frontend_wc.config;

import feign.Request;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients
public class FeignClientConfig {

    @Bean
    public Request.Options requestOptions() {
        return new Request.Options(10000, 10000);
    }
}
