package pe.edu.cibertec.Patotas_frontend_wc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import pe.edu.cibertec.Patotas_frontend_wc.dto.LoginRequestDTO;
import pe.edu.cibertec.Patotas_frontend_wc.dto.LoginResponseDTO;
import pe.edu.cibertec.Patotas_frontend_wc.dto.LogoutRequestDTO;
import pe.edu.cibertec.Patotas_frontend_wc.dto.LogoutResponseDTO;
import pe.edu.cibertec.Patotas_frontend_wc.viewmodel.LoginModel;
import reactor.core.publisher.Mono;

import java.util.Date;

@RestController
@RequestMapping("/login")
@CrossOrigin(origins = "http://localhost:5173")
public class LoginControllerAsync {

    @Autowired
    WebClient webClientAutenticacion;

    @GetMapping("/inicio")
    public String inicio(Model model) {
        LoginModel loginModel = new LoginModel("00", "", "");
        model.addAttribute("loginModel", loginModel);
        return "inicio";
    }

    @PostMapping("/autenticar-async")
    public Mono<LoginResponseDTO> autenticar(@RequestBody LoginRequestDTO loginRequestDTO) {

        // Validar campos de entrada
        if (loginRequestDTO.tipoDocumento() == null || loginRequestDTO.tipoDocumento().trim().length() == 0 ||
                loginRequestDTO.numeroDocumento() == null || loginRequestDTO.numeroDocumento().trim().length() == 0 ||
                loginRequestDTO.password() == null || loginRequestDTO.password().trim().length() == 0) {

            return Mono.just(new LoginResponseDTO("01", "Error: Debe completar correctamente los datos", "", ""));
        }

        try {
            // Invocar API de validacion de usuario del backend
            return webClientAutenticacion.post()
                    .uri("/login")
                    .body(Mono.just(loginRequestDTO), LoginRequestDTO.class)
                    .retrieve()
                    .bodyToMono(LoginResponseDTO.class)
                    .flatMap(response -> {
                        if(response.codigo().equals("00")) {
                            return Mono.just(new LoginResponseDTO("00", "", response.nombreUsuario(), ""));
                        } else {
                            return Mono.just(new LoginResponseDTO("02", "Error: Autenticacion fallida", "", ""));
                        }
                    });

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return Mono.just(new LoginResponseDTO("99", "Error: Autenticacion fallida", "", ""));
        }

    }

    @PostMapping("/logout-async")
    public Mono<LogoutResponseDTO> logout(@RequestBody LogoutRequestDTO logoutRequestDTO) {

        // Validar campos de entrada
        if (logoutRequestDTO.tipoDocumento() == null || logoutRequestDTO.tipoDocumento().trim().length() == 0 ||
                logoutRequestDTO.numeroDocumento() == null || logoutRequestDTO.numeroDocumento().trim().length() == 0) {

            return Mono.just(new LogoutResponseDTO(false, null, "No se contraron datos de usuario"));
        }

        try {
            return webClientAutenticacion.post()
                    .uri("/logout")
                    .body(Mono.just(logoutRequestDTO), LogoutRequestDTO.class)
                    .retrieve()
                    .bodyToMono(LogoutResponseDTO.class)
                    .flatMap(response -> {
                        if(response.resultado().equals(true)) {
                            return Mono.just(new LogoutResponseDTO(true, new Date(), "No error"));
                        } else {
                            return Mono.just(new LogoutResponseDTO(false, null, "Error"));
                        }
                    });

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return Mono.just(new LogoutResponseDTO(false, null, ""));
        }

    }
}
