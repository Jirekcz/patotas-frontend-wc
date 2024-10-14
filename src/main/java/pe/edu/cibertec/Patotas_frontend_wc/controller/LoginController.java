package pe.edu.cibertec.Patotas_frontend_wc.controller;

import org.apache.juli.logging.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import pe.edu.cibertec.Patotas_frontend_wc.client.AutenticacionClient;
import pe.edu.cibertec.Patotas_frontend_wc.dto.LoginRequestDTO;
import pe.edu.cibertec.Patotas_frontend_wc.dto.LoginResponseDTO;
import pe.edu.cibertec.Patotas_frontend_wc.dto.LogoutRequestDTO;
import pe.edu.cibertec.Patotas_frontend_wc.dto.LogoutResponseDTO;
import pe.edu.cibertec.Patotas_frontend_wc.viewmodel.LoginModel;
import pe.edu.cibertec.Patotas_frontend_wc.viewmodel.LogoutModel;
import reactor.core.publisher.Mono;

@Controller
@RequestMapping("/login")
@CrossOrigin(origins = "http://localhost:5173")
public class LoginController {

    @Autowired
    WebClient webClientAutenticacion;

    @GetMapping("/inicio")
    public String inicio(Model model) {
        LoginModel loginModel = new LoginModel("00", "", "");
        model.addAttribute("loginModel", loginModel);
        return "inicio";
    }

    @PostMapping("/autenticar")
    public String autenticar(@RequestParam("tipoDocumento") String tipoDocumento,
                             @RequestParam("numeroDocumento") String numeroDocumento,
                             @RequestParam("password") String password,
                             Model model) {

        // Validar campos de entrada
        if (tipoDocumento == null || tipoDocumento.trim().length() == 0 || numeroDocumento == null || numeroDocumento.trim().length() == 0 || password == null || password.trim().length() == 0) {
            LoginModel loginModel = new LoginModel("01", "Error: Debe completar correctamente sus credenciales", "");
            model.addAttribute("loginModel", loginModel);
            return "inicio";
        }

        try {
            // Invocar API de validacion de usuario
            LoginRequestDTO loginRequestDTO = new LoginRequestDTO(tipoDocumento, numeroDocumento, password);
            Mono<LoginResponseDTO> monoLoginResponseDTO = webClientAutenticacion.post()
                    .uri("/login")
                    .body(Mono.just(loginRequestDTO), LoginRequestDTO.class)
                    .retrieve()
                    .bodyToMono(LoginResponseDTO.class);

            // Recuperar resultado del mono (Synchrono o Bloqueante)
            LoginResponseDTO loginResponseDTO = monoLoginResponseDTO.block();

            //Validar respuesta
            if (loginResponseDTO.codigo().equals("00")) {
                LoginModel loginModel = new LoginModel("00", "", loginResponseDTO.nombreUsuario());
                model.addAttribute("loginModel", loginModel);
                return "principal";
            } else {
                LoginModel loginModel = new LoginModel("02", "Error: Autenticacion fallida", "");
                model.addAttribute("loginModel", loginModel);
                return "inicio";
            }
        } catch (Exception e) {
            LoginModel loginModel = new LoginModel("99", "Error: Autenticacion fallida", "");
            model.addAttribute("loginModel", loginModel);
            System.out.println(e.getMessage());
            return "inicio";
        }

    }

    @PostMapping("/logout")
    public String logout(@RequestParam("tipoDocumento") String tipoDocumento,
                             @RequestParam("numeroDocumento") String numeroDocumento,
                             Model model) {

        // Validar campos de entrada
        if (tipoDocumento == null || tipoDocumento.trim().length() == 0 || numeroDocumento == null || numeroDocumento.trim().length() == 0) {
            LogoutModel logoutModel = new LogoutModel("01", "Error: Debe completar correctamente sus credenciales", null);
            model.addAttribute("logoutModel", logoutModel);
            return "";
        }

        try {
            LogoutRequestDTO loginRequestDTO = new LogoutRequestDTO(tipoDocumento, numeroDocumento);
            Mono<LogoutResponseDTO> monoLogoutResponseDTO = webClientAutenticacion.post()
                    .uri("/logout")
                    .body(Mono.just(loginRequestDTO), LogoutRequestDTO.class)
                    .retrieve()
                    .bodyToMono(LogoutResponseDTO.class);

            // Recuperar resultado del mono (Synchrono o Bloqueante)
            LogoutResponseDTO logoutResponseDTO = monoLogoutResponseDTO.block();

            //Validar respuesta
            if (logoutResponseDTO.resultado().booleanValue() == true) {
                LogoutModel logoutModel = new LogoutModel("00", "", logoutResponseDTO.fecha());
                model.addAttribute("logoutModel", logoutModel);
                return "inicio";
            } else {
                LogoutModel logoutModel = new LogoutModel("02", "Error: Autenticacion fallida", null);
                model.addAttribute("logoutModel", logoutModel);
                return "";
            }
        } catch (Exception e) {
            LogoutModel logoutModel = new LogoutModel("99", "Error: Autenticacion fallida", null);
            model.addAttribute("logoutModel", logoutModel);
            System.out.println(e.getMessage());
            return "";
        }

    }

    // Un soldado caido :'v
//    @PostMapping("/logout")
//    public String logout(@RequestParam("tipoDocumento") String tipoDocumento, @RequestParam("numeroDocumento") String numeroDocumento, Model model) {
//        try {
//            LogoutRequestDTO logoutRequestDTO = new LogoutRequestDTO(tipoDocumento, numeroDocumento);
//            ResponseEntity<LogoutResponseDTO> responseEntity = autenticacionClient.logout(logoutRequestDTO);
//
//            // Validar la respuseta del servicio a traves de HTTP
//            if(responseEntity.getStatusCode().is2xxSuccessful()) {
//
//                LogoutResponseDTO logoutResponseDTO = responseEntity.getBody();
//
//                //Validar respuesta
//                if (logoutResponseDTO != null && logoutResponseDTO.resultado().equals(true)) {
//                    LogoutModel logoutModel = new LogoutModel("00", "", logoutResponseDTO.fecha());
//                    model.addAttribute("logoutModel", logoutModel);
//                    return "inicio";
//                } else {
//                    LogoutModel logoutModel = new LogoutModel("01", "Error al cerrar la sesion", null);
//                    model.addAttribute("logoutModel", logoutModel);
//                    return "";
//                }
//            } else {
//                LogoutModel logoutModel = new LogoutModel("99", "ERROR: Logout fallido", null);
//                model.addAttribute("logoutModel", logoutModel);
//                return "";
//            }
//
//        } catch (Exception e) {
//            LogoutModel logoutModel = new LogoutModel("99", "ERROR: Logout fallido", null);
//            model.addAttribute("logoutModel", logoutModel);
//            System.out.println(e.getMessage());
//            return "inicio";
//        }
//  }
}
