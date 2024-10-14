package pe.edu.cibertec.Patotas_frontend_wc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class PatotasFrontendWcApplication {

	public static void main(String[] args) {
		SpringApplication.run(PatotasFrontendWcApplication.class, args);
	}

}
